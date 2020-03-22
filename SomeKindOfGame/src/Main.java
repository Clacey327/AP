import java.util.Scanner;

public class Main {

    public static void main(String[] args){

        System.out.println("Welcome to Blackjack!");

        Deck deck = new Deck();

        deck.FillDeck();
        deck.shuffleDeck();

        Deck playerHand = new Deck();
        int balance = 100;

        Deck dealerHand = new Deck();
        Scanner userInput = new Scanner(System.in);

        while(balance > 0){

            System.out.println("You have $" + balance + ". How much would you like to wager?");
            int bet = userInput.nextInt();
            boolean endRound = false;

            if(bet > balance) {
                System.out.println("Insufficient Funds");
                break;

            }

            playerHand.draw(deck);
            playerHand.draw(deck);

            dealerHand.draw(deck);
            dealerHand.draw(deck);

            while(true)

            {

                System.out.println("Your Hand: " + playerHand.toString());
                System.out.println("Current Value: " + playerHand.cardsValue());

                System.out.println("Dealers Hand: " + dealerHand.getCard(0).toString() + " and [hidden]");
                System.out.println("Would you like to (1) Hit or (2) Hold");

                int move = userInput.nextInt();

                if(move == 1){
                    playerHand.draw(playerHand);
                    System.out.println("You drew a:" + playerHand.getCard(playerHand.deckSize()-1).toString());

                    if(playerHand.cardsValue() > 21){
                        System.out.println("Bust: " + playerHand.cardsValue());

                        balance -= bet;
                        endRound = true;
                        break;

                    }
                }

                if(move == 2){
                    break;

                }
            }

            System.out.println("Dealer Cards:" + dealerHand.toString());
            if((dealerHand.cardsValue() > playerHand.cardsValue()) && endRound == false){

                System.out.println("Dealer wins " + dealerHand.cardsValue() + " to " + playerHand.cardsValue());
                balance -= bet;
                endRound = true;

            }

            while((dealerHand.cardsValue() < 17) && endRound == false) {
                dealerHand.draw(playerHand);
                System.out.println("Dealer draws: " + dealerHand.getCard(dealerHand.deckSize()-1).toString());

            }

            System.out.println("Dealers hand value: " + dealerHand.cardsValue());
            if((dealerHand.cardsValue() > 21)&& endRound == false){

                System.out.println("Dealer Busts. You win!");
                balance += bet;
                endRound = true;

            }

            if((playerHand.cardsValue() > dealerHand.cardsValue()) && endRound == false){

                System.out.println("You win the hand.");
                balance += bet;
                endRound = true;

            }

            else if(endRound == false) {

                System.out.println("Dealer wins.");
                balance -= bet;

            }

            playerHand.newDeck(deck);
            dealerHand.newDeck(deck);
            deck.shuffleDeck();
            System.out.println("End of Hand.");

        }

        System.out.println("Game over! You lost all your money. :(");
        userInput.close();

    }
}
