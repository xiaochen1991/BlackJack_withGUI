import java.awt.*;

/**
 * subclass for an implement of dealer
 * 
 * @author xiaochen
 * 
 */
public class Dealer extends Player {

	protected Deck deck; // the deck of cards

	// create a dealer and give it the images of the cards
	public Dealer(TableCanvas tableCanvas, Image[] cards) {
		this(tableCanvas, 1, cards);
	}

	// create a dealer using the specified number of decks
	// and give it the images of the cards
	public Dealer(TableCanvas tableCanvas, int numDecks, Image[] cards) {
		super(tableCanvas, 0);
		deck = new Deck(numDecks, cards);
	}

	// take the next card off the deck
	public Card deal() {
		return deck.dealNextCard();
	}

	// play out hand (happens once all players have stayed or busted)
	// hits until sum reaches at least 17
	public void play() {
		tableCanvas.flipDealersSecond(); // show the second card
		while (true) {
			// if the total is greater than 17, stop
			if (getHandSum() >= 17) {
				break;
			}
			Hit(deck.dealNextCard()); // if hasn't stopped, take another card
		}
	}
}