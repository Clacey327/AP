import java.io.Serializable;

public class Card implements Serializable {

	// Class to create the Card object

	// Card instance variables
	private Suit suit;
	private Values value;


	public Card(Suit suit, Values value){  //Card constructor


		this.suit = suit;
		this.value = value;

	}

	// Creates the String to display the suit and value of the cards

	public String toString(){
		return this.getValue().toString()  + " of " + this.suit.toString();

	}

	// Returns the numeric value of each card

	public Values getValue(){
		return this.value;

	}
}
