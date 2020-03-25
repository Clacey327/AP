public class Card {

	private Suit suit;
	private Values value;
	
	public Card(Suit suit, Values value){

		this.suit = suit;
		this.value = value;

	}
	
	public String toString(){
		return this.getValue().toString()  + " of " + this.suit.toString();

	}

	public Values getValue(){
		return this.value;

	}
}
