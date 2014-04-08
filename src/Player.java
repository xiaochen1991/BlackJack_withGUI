import java.util.ArrayList;

/**
 * An implement of a blackjack player
 * 
 * @author xiaochen
 * 
 */
public abstract class Player {

	/**
	 * a canvas to display the table and cards
	 */
	protected TableCanvas tableCanvas;
	protected int playerNumber;

	/**
	 * the cards in the player's hand.
	 */

	protected ArrayList<Card> hand;

	public Player(TableCanvas tableCanvas, int playerNumber) {

		this.tableCanvas = tableCanvas;
		this.playerNumber = playerNumber;
		hand = new ArrayList<Card>();
		EmptyHand();

	}

	/**
	 * add a card to the player's hand and check if it burst
	 * 
	 */
	public void Hit(Card aCard) {

		// add new card in next slot
		hand.add(aCard);

		// tell the table about the new card
		tableCanvas.newCard(aCard, playerNumber);

	}

	// returns whether the player is busted
	public boolean isBusted() {
		return (this.getHandSum() > 21);
	}

	/**
	 * Empty the cards in hand
	 */
	public void EmptyHand() {
		hand.clear();
	}

	/**
	 * get the sum of the cards in the player's hand
	 */
	public int getHandSum() {

		int handSum = 0;
		int cardNum;
		int numAces = 0;

		// sum of cards in hand
		for (int i = 0; i < hand.size(); i++) {

			cardNum = hand.get(i).getNumber();

			if (cardNum == 1) {
				// Ace
				numAces++;
				handSum += 11;
			} else if (cardNum > 10) {
				// face card
				handSum += 10;
			} else {
				handSum += cardNum;
			}
		}

		// if we have aces and our sum is > 21, set some/all of them to value 1
		while (handSum > 21 && numAces > 0) {
			handSum -= 10;
			numAces--;
		}
		return handSum;
	}

	// return the player number
	public int getPlayerNumber() {
		return playerNumber;
	}

}