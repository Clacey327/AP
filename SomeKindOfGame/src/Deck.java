import java.util.ArrayList;
import java.util.Collections;

public class Deck {

	private ArrayList<Card> deck;
	
	public Deck(){
		this.deck = new ArrayList<Card>();
	
	}

	public void FillDeck(){
		for(Suits cardSuit : Suits.values()){
			for(Values cardValue : Values.values()){
				this.deck.add(new Card(cardSuit,cardValue));
			}
		}
	}

public void shuffleDeck(){

	Collections.shuffle(this.deck);

}
	

	public void removeCard(int i) {
		this.deck.remove(i);

	}

	public Card getCard(int i){

		return this.deck.get(i);

	}

	public void addCard(Card addCard){
		this.deck.add(addCard);

	}

	public void draw(Deck deck){
		this.deck.add(deck.getCard(0));
		deck.removeCard(0);

	}

	public String toString(){
		String cardString = " ";
		int i = 0;

		for(Card card : this.deck){
			cardString += "\n" + card.toString();
			i++;

		}

		return cardString;
	}

	public void newDeck (Deck moveInto){
		int thisDeckSize = this.deck.size();
		for(int i = 0; i < thisDeckSize; i++){
			moveInto.addCard(this.getCard(i));
		}

		for(int i = 0; i < thisDeckSize; i++){
			this.removeCard(0);

		}
	}
	
	public int deckSize(){
		return this.deck.size();
	}

	public int cardsValue(){
		int totalValue = 0;

		for(Card card : this.deck){

			switch(card.getValue()){

			case Two:
				totalValue = 2;
				break;

			case Three:
				totalValue += 3;
				break;

			case Four:
				totalValue += 4;
				break;

			case Five:
				totalValue += 5;
				break;

			case Six:
				totalValue += 6;
				break;

			case Seven:
				totalValue += 7;
				break;

			case Eight:
				totalValue += 8;
				break;

			case Nine:
				totalValue += 9;
				break;

			case Ten:
				totalValue += 10;
				break;

			case Jack:
				totalValue += 10;
				break;

			case Queen:
				totalValue += 10;
				break;

			case King:
				totalValue += 10;
				break;

			case Ace:
				totalValue += 11;
				break;

			}			
		}

		return totalValue;
	
	}
}
