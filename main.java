package Casino;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
public class main{
    static Deck cardDeck = new Deck();
    int size = cardDeck.deck.size();
    static realPlayer user = new realPlayer();
    static Screen Screen = new Screen();
    house mrHouse = new house();
	public static void main(String[] args) {
        playerTurn();
		houseTurn();
	}
    void debugDeck(){
        for(int x = 0; x < size; x++){
            System.out.print(cardDeck.deck.get(x).suit);
        }
    }
	void playerTurn(){
		boolean gamePlay = true;
		makeWager(100); //replace 100 with inputDevice.nextInt();
        while(gamePlay = true){
            switch(inputScanner()){ //input will be implemented later
                case 1: user.hit(cardDeck);
                case 2: gamePlay = user.stand;
                case 3: user.doubleDown(cardDeck);
                case 4: user.split(); //I think this one's already getting cut
                case 5: gamePlay = user.surrender(cardDeck);
            }
            if(user.handValue()>21){
                gamePlay = false;
            }
        }
	}
	void houseTurn(){
		while(mrHouse.cardValue < 17){
			mrHouse.hit(cardDeck);
		}
	}
	void evaluateGame(){
		if(user.cardValue>mrHouse.cardValue){
			user.balance+=user.wager*2;
		}
	}
}