import java.io.*;
import java.util.ArrayList;

public class realPlayer extends player{
    int balance;
    int wager;
    static BufferedWriter write;
    static BufferedReader read;
    public realPlayer(BufferedWriter writer, BufferedReader reader){
        write=writer;
        read=reader;
    }
    public void createBalance(){
        try {
            String balanceString = read.readLine();
            if (balanceString == null) {
                // Set an initial balance of 1000 chips for new players
                balance = 1000;
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
	void makeWager(int input){
		wager = input;
		balance -= input;
	}
    boolean doubleDown(ArrayList deck){
        hit(hand, deck);
        wager*=2;
        return stand;
    }
    boolean surrender(){
        wager=0;
        return stand;
    }
    void turn(ArrayList deck){
        boolean gamePlay = true;
		makeWager(100); //replace 100 with inputDevice.nextInt();
        while(gamePlay = true){
            switch(1){ //input will be implemented later
                case 1: hit(hand, deck);
                case 2: gamePlay = stand;
                case 3: doubleDown(deck);
                // We removed split it would be option # 4
                case 5: gamePlay = surrender();
            }
            if(handValue()>21){
                gamePlay = false;
            }
        }
    }
    public void saveBalance() throws IOException {
        write.write(String.valueOf(balance));
        write.newLine();
        write.flush();
    }
    public void loadBalance() {
        try {
            String balanceString = read.readLine();
            if (balanceString != null) {
                balance = Integer.parseInt(balanceString);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}
