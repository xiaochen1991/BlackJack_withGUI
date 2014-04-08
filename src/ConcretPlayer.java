/**
 * subclass for an implement of real player
 * 
 * @author xiaochen
 * 
 */

public class ConcretPlayer extends Player {

	protected int chip;

	public ConcretPlayer(int chip, TableCanvas tableCanvas, int playerNumber) {
		super(tableCanvas, playerNumber);
	}

	/**
	 * return the chip left
	 */
	public int getChip() {
		return this.chip;
	}

	// whether the player has the specified amount of chip
	public boolean hasCash(int wager) {
		return (this.chip >= wager);
	}

	// set the wager and check if player has enough chip for wager
	public int bet(int wager) {
		if (hasCash(wager)) {
			chip -= wager;
			return wager;
		} else {
			// the player doesn't have the enough then give all left as wager
			int temp = chip;
			chip = 0;
			return temp;
		}
	}

	/**
	 * win chips
	 */
	public void WinChip(int wager) {

		this.chip += wager;
	}

	/**
	 * lose chips
	 */
	public void LoseChip(int wager) {

		this.chip -= wager;
	}

	// create a new user who has one of the original cards in his hand
	public ConcretPlayer split(int numSplits) {
		// create a new player with no chip
		ConcretPlayer splitPlayer = new ConcretPlayer(0, tableCanvas,
				1 + numSplits);
		splitPlayer.Hit(hand.get(1)); // give the new user one of the split
										// cards
		hand.remove(1); // remove the card split to splitPlayer
		tableCanvas.split(playerNumber); // tell the table about the split
		return splitPlayer;
	}
	
	// whether the player's cards are the same number and can be split
		public boolean splitable ()
		{
			return hand.get(0).getNumber() == hand.get(1).getNumber();
		}

}