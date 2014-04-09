import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class BlackJack extends Applet implements ActionListener, ItemListener,
		KeyListener {

	/**
	 * Info About the Game:
	 */

	// whether the player is playing a hand
	private boolean[] playingHand;

	// whether the player got blackjack
	private boolean[] gotBlackjack;

	// number of players
	private int numPlayers;

	// number of splits there have been in the current hand
	private int numSplits;

	// current player
	private int currentUser;

	// an int array of each player's wager
	private int[] betSize;

	/**
	 * Players
	 */
	// the dealer
	private Dealer dealer;
	// the players
	private ConcretPlayer[] allUsers;

	/**
	 * Display Parts
	 */
	private Button hit, stand, bet, // buttons to hit, stand, bet,
			split; // and split
	private Choice playerCount, // menus to select the number of players
			deckCount; // and number of decks
	private Label playerCountQuestion, // label to ask how many players to have
			deckCountQuestion, // label to ask how many decks to use
			infoBar, // label to display each player's available chip
			whoIsCurrentPlayer; // label to display whose turn it is
	private TableCanvas tableCanvas; // a canvas that displays the table
	private TextField textField; // a textbox to get the wager

	// init method, called when the applet is first started
	public void init() {
		setBackground(Color.LIGHT_GRAY); // use a light gray background
		deckCountQuestion = new Label("How many decks do you want to have?");
		add(deckCountQuestion); // add the label asking how many decks to use
		deckCount = new Choice();
		deckCount.addItem("1");
		deckCount.addItem("2");
		deckCount.addItem("3");
		deckCount.addItem("4");
		deckCount.addItem("5");
		deckCount.addItem("6");
		deckCount.addItem("7");
		deckCount.addItem("8");
		add(deckCount); // add the choice for how many decks

		playerCountQuestion = new Label("How many players do you want to have?");
		add(playerCountQuestion);

		playerCount = new Choice();
		playerCount.addItem("");
		playerCount.addItem("1");
		playerCount.addItem("2");
		playerCount.addItem("3");
		playerCount.addItem("4");
		playerCount.addItemListener(this);

		add(playerCount); // add the choice for how many players
	}

	/**
	 * formats the applet for play called after the number of players is chosen
	 */
	public void formatForGame() {
		int numDecks = deckCount.getSelectedIndex() + 1; // get the number of
															// decks to use
		// create a table canvas for the relevant number of players
		// and give it the back of card image
		tableCanvas = new TableCanvas(numPlayers, getImage(getDocumentBase(),
				"Cards/back.png"));

		// create the dealer for the relevant table and number of decks
		// and give it the card images
		dealer = new Dealer(tableCanvas, numDecks, getCardImages());

		// create the array of players
		allUsers = new ConcretPlayer[4];

		// initialize the relevant number of players
		for (currentUser = 0; currentUser < numPlayers; currentUser++) {
			// players get 100 chips, the relevant table, and an ID number
			allUsers[currentUser] = new ConcretPlayer(100, tableCanvas,
					currentUser + 1);
		}

		// remove the questions about deck and player counts
		remove(deckCountQuestion);
		remove(deckCount);
		remove(playerCountQuestion);
		remove(playerCount);

		// create new layout
		setLayout(new BorderLayout()); // use the border layout
		whoIsCurrentPlayer = new Label("", Label.CENTER); // top label to say
															// whose turn it is
		add("North", whoIsCurrentPlayer); // place it at the top
		add("Center", tableCanvas); // place the table canvas at the center

		// create the info bar and buttons and place them at the bottom
		add("South", makeInfoAndButtons());
		validate(); // redraw the applet with the new formatting

		/**
		 * initialize variables
		 */
		betSize = new int[4]; // betsize array initialized
		currentUser = 0; // currentUser set to 0
		playingHand = new boolean[4]; // playingHand array initialized
		// gotBlackjack array initialized (last entry is whether dealer got
		// blackjack)
		gotBlackjack = new boolean[5];
		// set it so no player is playing
		for (int i = 0; i < 4; i++) {
			playingHand[i] = false;
		}
		updateInfo(); // update the info label and the who is current player
						// label
	}

	/**
	 * method to load the card images and scale them
	 */
	private Image[] getCardImages() {
		Image[] images = new Image[52]; // create an array of images
		// load the clubs
		for (int i = 1; i <= 13; i++) {
			images[i - 1] = getImage(getDocumentBase(), "Cards/c" + i + ".png")
					.getScaledInstance(56, 70, Image.SCALE_SMOOTH);
		}
		// load the diamonds
		for (int i = 1; i <= 13; i++) {
			images[i + 12] = getImage(getDocumentBase(), "Cards/d" + i + ".png")
					.getScaledInstance(56, 70, Image.SCALE_SMOOTH);
		}
		// load the hearts
		for (int i = 1; i <= 13; i++) {
			images[i + 25] = getImage(getDocumentBase(), "Cards/h" + i + ".png")
					.getScaledInstance(56, 70, Image.SCALE_SMOOTH);
		}
		// load the spades
		for (int i = 1; i <= 13; i++) {
			images[i + 38] = getImage(getDocumentBase(), "Cards/s" + i + ".png")
					.getScaledInstance(56, 70, Image.SCALE_SMOOTH);
		}
		return images; // return the images
	}

	/**
	 * create the bottom panel of info and buttons
	 */
	private Panel makeInfoAndButtons() {
		Panel newPanel = new Panel(); // create a new panel
		newPanel.setLayout(new GridLayout(2, 1)); // use the grid layout
		infoBar = new Label("", Label.CENTER); // create the infoBar label and
												// center it
		newPanel.add(infoBar); // add the infoBar label
		newPanel.add(makeButtonPanel()); // add the buttons
		return newPanel; // return the panel
	}

	/**
	 * update the info on whose turn it is and each player's cash
	 */
	private void updateInfo() {
		String infoText = ""; // make a blank string
		// add every player's info and a gap
		for (int i = 0; i < numPlayers; i++) {
			infoText = infoText + "Player " + Integer.toString(i + 1)
					+ " has chips" + allUsers[i].getChip()
					+ "                                     ";
		}
		infoBar.setText(infoText.trim()); // remove the trailing whitespace
		// update the who is current player label
		if (numSplits != 0) {
			whoIsCurrentPlayer.setText("Hand "
					+ Integer.toString(currentUser + 1)
					+ " of player 1 is being played");
		} else {
			// numPlayers > 1
			whoIsCurrentPlayer.setText("It is currently player "
					+ Integer.toString(currentUser + 1) + "'s turn");
		}
	}

	/**
	 * create the buttons
	 */
	private Panel makeButtonPanel() {
		Panel newPanel = new Panel(); // create a new panel

		// create the hit button, make it an ActionListener, and add it
		hit = new Button("Hit");
		hit.addActionListener(this);
		newPanel.add(hit);

		// create the stay button, make it an ActionListener, and add it
		stand = new Button("Stand");
		stand.addActionListener(this);
		newPanel.add(stand);

		// create the bet button, make it an ActionListener, and add it
		bet = new Button("Bet");
		bet.addActionListener(this);
		newPanel.add(bet);

		// create a text field to take the bet, make it a KeyListener, and add
		// it
		textField = new TextField("0", 5);
		textField.addKeyListener(this); // used to get the cheat code
		newPanel.add(textField);

		// if there is only one player, then the player can split
		if (numPlayers == 1) {
			// create the split button, make it an ActionListener, and add it
			split = new Button("Split pair");
			split.addActionListener(this);
			newPanel.add(split);
		}

		return newPanel; // return the panel
	}

	/**
	 * This method is called by java to handle button clicks
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == hit) // if the hit button was pressed
		{
			// if the current player is playing the hand and does not have 21
			if (playingHand[currentUser]
					&& allUsers[currentUser].getHandSum() != 21) {
				hit(); // then hit
			}
		}

		else if (e.getSource() == stand) // if the stand button was pressed
		{
			// if the current player is playing the hand
			if (playingHand[currentUser]) {
				stand(); // then stand
			}
		}

		else if (e.getSource() == bet) // if the bet button was pressed
		{
			// if the current user is not playing the hand
			if (!playingHand[currentUser]) {
				bet(); // then bet
			}
		}

		else if (e.getSource() == split) // if the split button was pressed
		{
			/**
			 * if the current user is playing a hand and has two cards and there
			 * have been less than 3 splits and the user has at least his
			 * original bet and the cards are allowed to be split
			 */
			if (playingHand[currentUser]
					&& allUsers[currentUser].hand.size() == 2 && numSplits < 3
					&& allUsers[0].hasCash(betSize[currentUser])
					&& allUsers[0].splitable()) {
				split(); // then split
			}
		}
	}

	/**
	 * ItemListener list
	 */
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == playerCount) // make sure the selection was from
											// playerCount
		{
			// make the number of players the selection
			numPlayers = playerCount.getSelectedIndex();
			if (numPlayers == 0) { // assume accidental selection
				numPlayers = 1; // use one player
			}
			playerCount.removeItemListener(this); // remove the item listener
			formatForGame(); // format the applet for the game
		}
	}

	public void keyPressed(KeyEvent e) {
	}// needed to implement KeyListener

	public void keyReleased(KeyEvent e) {
	} // needed to implement KeyListener

	public void keyTyped(KeyEvent e) {
	} // needed to implement KeyListener

	/**
	 * start a new hand
	 */
	public void newHand() {
		tableCanvas.newHand(); // prepare the table for a new hand
		// reset all the user's hands and set all the user's gotBlackjacks to
		// false
		for (currentUser = 0; currentUser < numPlayers; currentUser++) {
			allUsers[currentUser].EmptyHand();
			gotBlackjack[currentUser] = false;
		}
		// reset the dealer's hand and set the dealer's gotBlackjack to false
		dealer.EmptyHand();
		gotBlackjack[4] = false;

		numSplits = 0; // set the number of splits back to 0

		// deal all the first cards to each player and the dealer
		for (currentUser = 0; currentUser < numPlayers; currentUser++)
			allUsers[currentUser].Hit(dealer.deal());
		dealer.Hit(dealer.deal());

		// deal all the second cards to each player and the dealer
		for (currentUser = 0; currentUser < numPlayers; currentUser++) {
			allUsers[currentUser].Hit(dealer.deal());
			// if a user has blackjack
			if (allUsers[currentUser].getHandSum() == 21)
				gotBlackjack[currentUser] = true; // set their gotBlackjack to
													// true
		}
		dealer.Hit(dealer.deal());
		// if the dealer has blackjack
		if (dealer.getHandSum() == 21)
			// set its gotBlackjack to true (dealer gotBlackjack is at the end
			// of the array)
			gotBlackjack[4] = true;

		currentUser = 0; // set the currentUser to 0
	}

	// hit - take a new card
	public void hit() {
		allUsers[currentUser].Hit(dealer.deal()); // get a new card from
													// the dealer
		// if the currentUser is now busted
		if (allUsers[currentUser].isBurst())
			stand(); // then he stays
		updateInfo(); // update displays
	}

	// stand - take no more cards
	public void stand() {
		// if this is the last player
		if (currentUser == numPlayers + numSplits - 1)
			dealersTurn(); // then it is the dealer's turn
		else
			// otherwise
			currentUser++; // it is the next player's turn
		updateInfo(); // update displays
	}

	// bet - wager the specified amount on the next hand
	public void bet() {
		try {
			// get number, check if user has enough chips
			betSize[currentUser] = allUsers[currentUser].bet(Integer
					.parseInt(textField.getText()));
		} catch (NumberFormatException n) // the text input was not an integer
		{
			betSize[currentUser] = 0; // make the bet 0
			System.err.println("YOU FAIL IT");
		}
		// the current user is now playing the hand
		playingHand[currentUser] = true;
		// if this is the last player
		if (currentUser == numPlayers - 1)
			newHand(); // then play the hand
		else
			// otherwise
			currentUser++; // let the next player bet
		updateInfo(); // update displays
	}

	// split - make two hands out of cards with the same number
	public void split() {
		numSplits++; // increment the number of splits
		playingHand[numSplits] = true; // the split player is playing the hand
		// make a new player by splitting the hand
		allUsers[numSplits] = allUsers[currentUser].split(numSplits);
		// make his bet the same as the original player's
		betSize[numSplits] = allUsers[currentUser].bet(betSize[currentUser]);
		// splitting aces means each hand gets one more card
		if (allUsers[currentUser].getHandSum() == 11) {
			hit(); // take one more card
			stand(); // increments currentUser to the second hand
			hit(); // take one more card
			stand(); // dealers turn
		}
		updateInfo(); // update displays
	}

	// the player loses the hand
	public void playerLost() {
		resetBet(); // reset their bet
	}

	// the player ties the hand
	public void playerTied() {
		// pay their bet back (if they were a split hand, pay the original user)
		allUsers[(numSplits == 0) ? currentUser : 0]
				.WinChip(betSize[currentUser]);
		resetBet(); // reset their bet
	}

	// the player wins the hand
	public void playerWon() {
		// pay their winnings (if they were a split hand, pay the original user)
		// if they got blackjack, pay out 3:2, otherwise pay out 1:1
		allUsers[(numSplits == 0) ? currentUser : 0]
				.WinChip((gotBlackjack[currentUser]) ? 5 * betSize[currentUser] / 2
						: betSize[currentUser] * 2);
		resetBet(); // reset their bet
	}

	// set the current user's bet back to 0
	public void resetBet() {
		betSize[currentUser] = 0;
	}

	/**
	 * dealer's turn
	 */
	public void dealersTurn() {
		dealer.play(); // let the dealer play out its hand
		int dealerTotal = dealer.getHandSum(), // set an int for the dealer's
												// total
		userTotal; // make an int for the user's total
		// for every hand
		for (currentUser = 0; currentUser < numPlayers + numSplits; currentUser++) {
			userTotal = allUsers[currentUser].getHandSum(); // get the user's
															// total
			// player busted or dealer has a blackjack and player doesn't
			if (userTotal > 21
					|| (gotBlackjack[4] && !gotBlackjack[currentUser]))
				playerLost(); // the player lost

			// player has a blackjack and dealer doesn't
			// or the player has more than dealer, or dealer busted
			else if ((gotBlackjack[currentUser] && !gotBlackjack[4])
					|| dealerTotal < userTotal || dealer.isBurst())
				playerWon(); // the player wins
			// player and dealer have the same total
			// (and from above conditions same gotBlackjack state)
			else if (dealerTotal == userTotal)
				playerTied(); // the player tied
			else
				// dealerTotal > userTotal, player has less than dealer
				playerLost(); // the player lost
			playingHand[currentUser] = false; // the player is no longer playing
												// the hand
		}
		currentUser = 0; // set currentUser back to 0
	}
}