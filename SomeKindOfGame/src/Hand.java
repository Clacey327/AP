import java.util.ArrayList;
import java.util.Collections;

public class Hand {

	public ArrayList<Card> Hand;
	
	public Hand(){

		this.Hand = new ArrayList<Card>();
	
	}
	
	public void newHand(){

		for(Suit suit : Suit.values()){
			for(Values value : Values.values()){
				this.Hand.add(new Card (suit, value));

			}
		}
	}

	public void shuffle(){

		Collections.shuffle(this.Hand);
	}

	public Card getCard(int i){
		return this.Hand.get(i);
	}

	public void addCard(Card add){
		this.Hand.add(add);
	}

	public void removeCard(int i){
		this.Hand.remove(i);
	}

	public void dealCard(Hand playerHand){
		this.Hand.add(playerHand.getCard(0));
		playerHand.removeCard(0);
	}


	public void clearHand(Hand clear){

		int handSize = this.Hand.size();
		for(int i = 0; i < handSize; i++){
			clear.addCard(this.getCard(i));

		}

		for(int i = 0; i < handSize; i++){
			this.removeCard(0);

		}
	}

	public int cardsValue(){

		int value = 0;

		for (Card cardValue : this.Hand){

			switch (cardValue.getValue()){

				case Ace:
					value += 1;
					break;

				case Two:
					value += 2;
					break;

				case Three:
					value += 3;
					break;

				case Four:
					value += 4;
					break;

				case Five:
					value += 5;
					break;

				case Six:
					value += 6;
					break;

				case Seven:
					value += 7;
					break;

				case Eight:
					value += 8;
					break;

				case Nine:
					value += 9;
					break;

				case Ten:
					value += 10;
					break;

				case Jack:
					value += 10;
					break;

				case Queen:
					value += 10;
					break;

				case King:
					value += 10;
					break;
			}			
		}

		return value;

	}

	public String toString(){
		String playerHand = " ";

		for(Card card : this.Hand){
			playerHand += "\n" + card.toString();

		}

		return playerHand;
	}

	public int deckSize(){
		return this.Hand.size();

	}
}
