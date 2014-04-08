import java.awt.*;
import java.util.LinkedList;
/**
 * implement of Table to display
 * @author chenxiao
 *
 */
public class TableCanvas extends Canvas {

	// whether the dealer's second is Hidden
	private boolean dealersSecondFaceDown;

	// the image of the back of a card
	private Image cardBack;

	// a linked list of linked lists of cards to store all the cards for display
	private LinkedList<LinkedList<Card>> allCards;

	// the number of players
	private int numPlayers;

	// an offscreen image for double buffering to avoid flickering
	private Image offscreen;

	// the size of the offscreen image
	private Dimension offscreensize;

	// the graphics for the offscreen image
	private Graphics g2;

	// create a new TableCanvas and pass it the image of the back of a card
	public TableCanvas(int numPlayers, Image cardBack) {
		this.numPlayers = numPlayers;
		allCards = new LinkedList<LinkedList<Card>>();
		for (int i = 0; i < 5; i++)
			allCards.add(new LinkedList<Card>());
		this.cardBack = cardBack;
	}

	// this is called when window is uncovered or resized the same as update
	public void paint(Graphics g) {
		update(g);
	}

	// overrides default update method to double buffer and not flicker
	// draws the canvas and each player's hand in it's relevant position
	public void update(Graphics g) {
		Dimension d = getSize(); // size of canvas

		// initially (or when size changes) create new image
		if ((offscreen == null) || (d.width != offscreensize.width)
				|| (d.height != offscreensize.height)) {
			offscreen = createImage(d.width, d.height);
			offscreensize = d;
			g2 = offscreen.getGraphics();
			g2.setFont(getFont());
		}

		// erase old contents of the offscreen image:
		g2.setColor(new Color(0, 90, 0));
		g2.fillRect(0, 0, d.width, d.height);

		for (int player = 0; player < 5; player++) // go through all players
		{
			if (player == 0) // dealer, place cards at top
			{
				// top left corner of dealer's first card
				int x0 = d.width / 2 - allCards.get(0).size() * 28;
				int y0 = 20;
				// draw the dealer's cards
				for (int i = 0; i < allCards.get(0).size(); i++) {
					// if the dealer's second hasn't been flipped yet
					if (dealersSecondFaceDown && i == 1) {
						g2.drawImage(cardBack, x0 + 56 * i, y0, this);
					}
					// otherwise it's not the second, or the second has been
					// flipped
					else {
						g2.drawImage(allCards.get(0).get(i).getImage(), x0 + 56
								* i, y0, this);
					}
				}
			} else // not the dealer, either a player or a split hand
			{
				// count the split hands
				int splitPlayerCount = 0;
				for (int i = numPlayers + 1; i < 5; i++)
					if (!allCards.get(i).isEmpty())
						splitPlayerCount++;

				// top left corner of the first card in the hand
				int x0 = d.width / (numPlayers + splitPlayerCount + 1) * player
						- allCards.get(player).size() * 28;
				int y0 = d.height - 90;
				// draw the player's cards
				for (int i = 0; i < allCards.get(player).size(); i++) {
					g2.drawImage(allCards.get(player).get(i).getImage(), x0
							+ 56 * i, y0, this);
				}
			}
		}

		// finally, draw the image on top of the old one
		g.drawImage(offscreen, 0, 0, null);
	}

	// add the new card to the relevant player's hand and repaint
	public void newCard(Card newCard, int player) {
		allCards.get(player).add(newCard);
		repaint();
	}

	// flip over the dealer's second card and repaint
	public void flipDealersSecond() {
		dealersSecondFaceDown = false;
		repaint();
	}

	// start a new hand, clear all the old hands
	public void newHand() {
		for (int i = 0; i < 5; i++)
			allCards.get(i).clear();
		dealersSecondFaceDown = true;
		repaint();
	}

	// remove the second card from the hand that was just split
	public void split(int playerNumber) {
		allCards.get(playerNumber).remove(1);
	}
}