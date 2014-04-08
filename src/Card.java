import java.awt.*;

/**
 * An implementation of a card type.
 * 
 * @author xiaochen
 * 
 */

public class Card {
	/**
	 * Should be one of four possible suits
	 */
	private Suit mySuit;
	private int myNumber;

	/**
	 * an image of the card
	 */
	private Image img;

	public Card(Suit aSuit, int aNumber) {

		this.mySuit = aSuit;

		if (aNumber >= 1 && aNumber <= 13) {
			this.myNumber = aNumber;
		} else {
			System.err.println(aNumber + " is not a valid Card number");
			System.exit(1);
		}
	}

	/**
	 * return the number of the card.
	 */

	public int getNumber() {
		return this.myNumber;
	}

	/**
	 *  get the card's image
	 */
	public void loadImage(Image[] images) {
		// first calculate what part of the array to look in from the suit
		int shift = 0;
		if (mySuit.toString().equals("Clubs") )
			shift = -1;
		else if (mySuit.toString().equals("Diamonds"))
			shift = 12;
		else if (mySuit.toString().equals("Spades"))
			shift = 25;
		else if (mySuit.toString().equals("Hearts"))
			shift = 38;

		img = images[shift + getNumber()]; // then get the relevant image
	}

	/**
	 *  return the card's image
	 */
		public Image getImage()
		{
			return img;
		}

}
