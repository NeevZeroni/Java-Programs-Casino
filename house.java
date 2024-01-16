// Import ArrayList library
import java.util.ArrayList;

// The house class extends the player class
public class house extends player {
    
    // This method represents the house's turn in the game. The game is house VS player.
    void turn(ArrayList deck) {
        // The house keeps hitting until its hand value is 17 or higher
        while (handValue() < 17) {
            hit(hand, deck);
        }
    }

    // This overrides the hit method from the player class
    @Override
    void hit(ArrayList hand, ArrayList deck) {
        // This adds the top card from the deck to the house's hand
        hand.add(deck.get(deck.size() - 1));
        // This removes the added card from the deck to ensure no double draws
        deck.remove(deck.size() - 1);
    }
}

