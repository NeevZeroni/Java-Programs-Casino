// Import libraries for Swing, AWT, and ArrayList
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

// The main class represents the Blackjack game
public class main {
    // Create objects of realPlayer, house, and the deck
    private static realPlayer user = new realPlayer();
    static ArrayList<Card> deck = new ArrayList<>();
    private static house mrHouse = new house();

    // Create JLabels for displaying player's balance, wager, hand value, and house card
    private static JLabel playerBalance;
    private static JLabel playerWager;
    private static JLabel playerHand;
    private static JLabel HouseHand;

    // JTextArea for displaying game messages
    private static JTextArea textArea;

    // createDeck method creates the initial deck for the game
    static void createDeck() {
        // Creates the deck by adding 52 cards
        for (int x = 0; x < 12; x++) { // 12 Faces
            for (int y = 0; y < 4; y++) { // 4 Suits
                deck.add(new Card(y, x));
            }
            Collections.shuffle(deck); // Shuffling with collections because I don't want to write a shuffler
        }
    }

    // Main method to start the game
    public static void main(String[] args) {
        createDeck();
        mrHouse.hit(mrHouse.hand, deck);
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    // Method to create and show the GUI
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Blackjack Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JPanel buttonPanel = createButtonPanel();
        frame.add(buttonPanel, BorderLayout.SOUTH);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel labelPanel = createLabelPanel();
        frame.add(labelPanel, BorderLayout.EAST);

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Method to create the label panel displaying player information
    private static JPanel createLabelPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1));

        playerBalance = new JLabel("Balance: " + user.balance + " chips");
        playerWager = new JLabel("Wager: " + user.wager + " chips");
        playerHand = new JLabel("Hand Value: " + user.handValue());
        HouseHand = new JLabel("House Card: " + mrHouse.hand.get(0).getCardName());

        panel.add(playerBalance);
        panel.add(playerWager);
        panel.add(playerHand);
        panel.add(HouseHand);
        return panel;
    }

    // Method to create the button panel with game options
    private static JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4));

        JButton hitButton = new JButton("Hit");
        JButton standButton = new JButton("Stand");
        JButton doubleDownButton = new JButton("Double Down");
        JButton surrenderButton = new JButton("Surrender");

        panel.add(hitButton);
        panel.add(standButton);
        panel.add(doubleDownButton);
        panel.add(surrenderButton);

        // Add action listeners to buttons
        hitButton.addActionListener(e -> hitButton("Hit"));
        standButton.addActionListener(e -> standButton("Stand"));
        doubleDownButton.addActionListener(e -> doubleDownButton("Double Down"));
        surrenderButton.addActionListener(e -> surrenderButton("Surrender"));

        return panel;
    }

    // Method to update JLabels with player information
    private static void updateLabels() {
        SwingUtilities.invokeLater(() -> {
            playerBalance.setText("Balance: " + user.balance + " chips");
            playerWager.setText("Wager: " + user.wager + " chips");
            playerHand.setText("Hand Value: " + user.handValue());
            HouseHand.setText("House Card: " + mrHouse.hand.get(0).getCardName());
        });
    }

    // Method for the "Hit" button action for when a player choses to hit
    private static void hitButton(String action) {
        user.hit(user.hand, deck);
        textArea.append(action + "! You drew " + user.hand.get(0).getCardName() + "\n");
        updateLabels();
    }

    // Method for the "Stand" button action for when a player chooses to stand
    private static void standButton(String action) {
        endGame();
    }

    // Method for the "Double Down" button action for when a player chooses to duble down
    private static void doubleDownButton(String action) {
        user.doubleDown(deck);
        textArea.append(action + "! Your card total is now " + user.handValue() + "\n");
        endGame();
    }

    // Method for the "Surrender" button action for when the player chooses to surrender
    private static void surrenderButton(String action) {
        user.surrender();
        textArea.append("Player surrenders!");
        updateLabels();
    }

    // Method to handle the end of the game
    private static void endGame() {
        if (user.handValue() <= 21) {
            mrHouse.turn(deck);
            String houseCards;
            for (int i = 1; i < mrHouse.hand.size(); i++) {
                textArea.append("House drew a " + mrHouse.hand.get(i).getCardName() + "!" + "\n");
            }
            // Determines who wins by seeing if the house or the player is closer to 21
            if (mrHouse.handValue() > user.handValue() && mrHouse.handValue() <= 21) {
                textArea.append("House wins! ");
            } else if (mrHouse.handValue() > 21) {
                textArea.append("House busts! Player wins!");
            } else if (user.handValue() > mrHouse.handValue()) {
                textArea.append("Player Wins!");
            // Else if statement that displayes a tie if the house and player have the same number
            } else if (user.handValue() == mrHouse.handValue()) {
                textArea.append("Player and House tie!");
            }
        } else {
            textArea.append("Player busts!");
        }
        updateLabels();
    }
}
