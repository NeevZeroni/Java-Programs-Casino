// The Card class represents a playing card
public class Card {
    // These arrays hold the suits, faces, and values of the cards
    String suits[] = {"Hearts", "Diamonds", "Clubs", "Spades"};
    String faces[] = {"2", "3", "4", "5", "6", "7", "8", "9", "Jack", "Queen", "King", "Ace"};
    static int values[] = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10};

    // These variables are declared to store the suit, face, and wether a card is face up or face down
    int suit;
    int face;
    boolean hidden; // Hidden will be true when a card is face down and false when a card is face up

    // This constructor initialies a card with a given suit number and face number
    public Card(int suitNum, int faceNum) {
        suit = suitNum;
        face = faceNum;
        hidden = true; // At the start, the card is set to be hidden because it is face down until the user acts on it
    }

    // This method gets the name of the card in the format "face of suit" i.e. "King of Clubs"
    public String getCardName() {
        return faces[face] + " of " + suits[suit];
    }
}
