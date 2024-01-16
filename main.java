import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class main {
    private static BufferedWriter writer;
    private static BufferedReader reader;
    static{ 
        try{
            writer = new BufferedWriter(new FileWriter("output.txt"));
            reader = new BufferedReader(new FileReader("output.txt"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private static realPlayer user = new realPlayer();
    static ArrayList<Card> deck = new ArrayList<>();
    private static house mrHouse = new house();

    private static JLabel playerBalance; //This block of code was instructed by chatGPT. I originally had these within the JFrame, and couldn't figure
    private static JLabel playerWager; // out how to get them to update
    private static JLabel playerHand;
    private static JLabel HouseHand;
    private static JTextArea textArea;
    private static JPanel buttonPanel;
    private static int wager;

    static void createDeck(){
        //creates the deck by adding 52 cards
        for(int x = 0; x<12; x++){ //12 Faces
            for(int y = 0; y < 4; y++){ //4 Suits
                deck.add(new Card(y,x));
            }
            Collections.shuffle(deck); //shuffling with collections because I don't want to write a shuffler --Will
        }
    }
    public static void main(String[] args) throws IOException{
       displayWagerInputScreen();
    }
    //Begin chatGPT menu GUI segment 
    private static void displayWagerInputScreen() { //asks the user how much they want to gamble. Doesn't influence long term gameplay, merely a suggestion
        JFrame wagerFrame = new JFrame("Enter Wager");
        wagerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel wagerPanel = new JPanel(new GridLayout(3, 1));

        JLabel wagerLabel = new JLabel("Enter your wager:");
        JTextField wagerTextField = new JTextField();
        JButton beginButton = new JButton("Begin");
        //adds the three wager options. Simple 1 by three horizontal grid. A modern classic
        wagerPanel.add(wagerLabel);
        wagerPanel.add(wagerTextField);
        wagerPanel.add(beginButton);

        beginButton.addActionListener(new ActionListener() { //Adds a listener. Cowritten with chatGPT (like a lot of the main class)
            @Override
            public void actionPerformed(ActionEvent e) {
                if (wagerTextField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a wager.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    wager = Integer.parseInt(wagerTextField.getText());

                    wagerFrame.dispose(); // Close the wager input screen
                    initializeGame();
                }
            }
        });
        wagerFrame.getContentPane().add(wagerPanel);
        wagerFrame.setSize(300, 150);
        wagerFrame.setLocationRelativeTo(null);
        wagerFrame.setVisible(true);
    }
    private static void initializeGame() { //Don't be fooled I wrote this part -- Will
        createDeck();
        mrHouse.hit(mrHouse.hand, deck);
        user.loadBalance(); //One of these isn't working as of 9:12 at night
        user.makeWager(wager); //User makes wager. From Will
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }
    //End of chatGPT JFrame segment
    private static void createAndShowGUI() {
        //adds a button panel on the Southern border. like Undertale. or any other turn based RPG
        JFrame frame = new JFrame("Blackjack Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JPanel buttonPanel = createButtonPanel();
        frame.add(buttonPanel, BorderLayout.SOUTH);
        //adds a text area. originally there would be cards. now there are not. tragic
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);
        //creates a label panel. wonderful for people who cannot count card values
        JPanel labelPanel = createLabelPanel();
        frame.add(labelPanel, BorderLayout.EAST);

        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
    private static JPanel createLabelPanel(){
        JPanel panel = new JPanel(new GridLayout(4, 1)); //vertical layout
        //labels that correspond to things you should know 
        playerBalance = new JLabel("Change in Balance: "+user.balance+" chips");
        playerWager = new JLabel("Wager: "+user.wager+" chips");
        playerHand = new JLabel("Hand Value: "+user.handValue());;
        HouseHand = new JLabel("House Card: "+mrHouse.hand.get(0).getCardName());
        //adds labels that display all relevant information. If we have to remake this it'll also include frames per second.
        panel.add(playerBalance);
        panel.add(playerWager);
        panel.add(playerHand);
        panel.add(HouseHand);

        return panel;
    }
    private static JPanel createButtonPanel() {
        buttonPanel = new JPanel(new GridLayout(1, 4));

        JButton hitButton = new JButton("Hit");
        JButton standButton = new JButton("Stand");
        JButton doubleDownButton = new JButton("Double Down");
        JButton surrenderButton = new JButton("Surrender");
        //add buttons that the player can interact with (until they can't)
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(doubleDownButton);
        buttonPanel.add(surrenderButton);

        // Add action listeners to buttons
        hitButton.addActionListener(e -> hitButton("Hit"));
        standButton.addActionListener(e -> standButton("Stand"));
        doubleDownButton.addActionListener(e -> doubleDownButton("Double Down"));
        surrenderButton.addActionListener(e -> surrenderButton("Surrender"));

        return buttonPanel;
    }
    private static void updateLabels(){
        SwingUtilities.invokeLater(() -> { //This line was instructed by chatGPT. I had no clue about invokeLater, and this came up in my quest to find out
        playerBalance.setText("Change in Balance: "+user.balance+" chips"); //how to update individual components of the GUI
        playerWager.setText("Wager: "+user.wager+" chips");
        playerHand.setText("Hand Value: "+user.handValue());
        HouseHand.setText("House Card: "+mrHouse.hand.get(0).getCardName());
        });
    }
    private static void hitButton(String action) { //These blocks of code were made by Will. They are the four buttons and their functions. This used to be a really ugly switch statement until someone told me how to do this on StackExchange
        user.hit(user.hand,deck);
        textArea.append(action + "! You drew "+ user.hand.get(0).getCardName()+ "\n");
        if(user.handValue()>21){
            endGame();
        }
        updateLabels();
    }
    private static void standButton(String action) {
        endGame();
    }
    private static void doubleDownButton(String action) {
        user.doubleDown(deck);
        textArea.append(action + "! \n");
        endGame();
    }
    private static void surrenderButton(String action) {
        user.surrender();
        user.balance-=wager/2;
        textArea.append("Player surrenders!");
        for (Component component : buttonPanel.getComponents()) { //Disables button components. Written by chatGPT, who is of no national origin
            if (component instanceof JButton) {
                ((JButton) component).setEnabled(false);
            }
        }
        updateLabels();
    }
    private static void endGame(){ //Evaluates and ends the game
        if(user.handValue()<=21){ //This block of code was made by a red blooded American (also Will)
            mrHouse.turn(deck);
            for(int i = 1; i < mrHouse.hand.size(); i++){
                textArea.append("House drew a "+mrHouse.hand.get(i).getCardName()+"!"+"\n");
            }
            if(mrHouse.handValue()>user.handValue()&&mrHouse.handValue()<=21){
                textArea.append("House wins! ");
                user.balance-=wager;
            }else if(mrHouse.handValue()>21){
                textArea.append("House busts! Player wins!");
                user.balance+=wager;
            }else if(user.handValue()>mrHouse.handValue()){
                textArea.append("Player Wins!");
                user.balance+=wager;
            }else if(user.handValue()==mrHouse.handValue()){
                textArea.append("Player and House tie!");
            }
        }else{
            textArea.append("Player busts!");
            user.balance-=wager;
        }
        try {
            user.saveBalance();
        } catch (IOException e) { // This block of code was instructed by chatGPT
            e.printStackTrace(); // Handle the exception appropriately
        } finally {
            try {
                // Close the streams in the finally block to ensure they are closed even if an exception occurs
                writer.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        } //Most of these try catches are from when we tried to incorporate file IO. Will we? Probably not, as we have two hours until submission. As far as I'm concerned, their existence isn't hurting anyone
        for (Component component : buttonPanel.getComponents()) { //Disables button components. Written by chatGPT, who is of no national origin
            if (component instanceof JButton) {
                ((JButton) component).setEnabled(false);
            }
        }
        updateLabels();
    }
}
