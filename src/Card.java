import java.awt.*;

/**
 * An implementation of a card type.
 * 
 * @author xiaochen
 * 
 */

public class Card {
	/**
	 * Should be one of four possible suits, in specific, from 1-13
	 */
	private int number, deckNumber;
	private Suit mySuit;

	// an image of the card
	private Image img;

	// create the card with number n, suit aSuit (deck 0 by default)
	public Card(int n, Suit aSuit) {
		this(n, aSuit, 0);
	}

	// create the card with number n, suit s, deck k
	public Card(int n, Suit aSuit, int k) {
		number = n;
		mySuit = aSuit;
		deckNumber = k;
	}

	// get the card's image
	public void loadImage(Image[] images) {
		// first calculate what part of the array to look in from the suit
		int shift = 0;
		if (mySuit.toString().equals("Clubs"))
			shift = -1;
		else if (mySuit.toString().equals("Diamonds"))
			shift = 12;
		else if (mySuit.toString().equals("Spades"))
			shift = 25;
		else if (mySuit.toString().equals("Hearts"))
			shift = 38;

		img = images[shift + getNumber()]; // then get the relevant image
	}

	// return the card's number
	public int getNumber() {
		return number;
	}

	// return the card's deck number
	public int getDeckNum() {
		return deckNumber;
	}

	// return the card's image
	public Image getImage() {
		return img;
	}
}