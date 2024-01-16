import java.io.*;
import java.util.ArrayList;

public class realPlayer extends player{
    int balance;
    int wager;
   
	void makeWager(int input){
		wager = input;
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

    public void saveBalance() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt", true))){
            writer.write(String.valueOf(balance));
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    public void createBalance(){
        try(BufferedReader localReader = new BufferedReader(new FileReader("output.txt"))) {
            String balanceString = localReader.readLine();
            if (balanceString == null) {
                // Set an initial balance of 1000 chips for new players
                balance = 1000;
            }else{
                balance = Integer.parseInt(balanceString);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    public void loadBalance() {
        try (BufferedReader localReader = new BufferedReader(new FileReader("output.txt"))) {
            String balanceString = localReader.readLine();
            if (balanceString != null) {
                balance = Integer.parseInt(balanceString);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}
