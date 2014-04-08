import java.awt.*;
import java.util.Random;
/**
 * An implementation of a deck of cards.
 * 
 * @author xiaochen
 *
 */
public class Deck {
	/**
	 * The array of cards in the deck, where the top cards is in the first index.
	 */
	private Card[] myCards;
	private Image[] cards;
	private int topIndex = 0;
	
	//default constructor
	public Deck(Image[] cards){
		this(1,cards);
	}
	
	/**
	 * The number of cards left in the deck.
	 */
	
	public Deck(int numOfDecks, Image[] cards){
		
		this.cards = cards;	
		int numCards = numOfDecks * 52;
		this.myCards = new Card[numCards];
		
		//init card index
		int c = 0;
		//for each deck
		for(int  d = 0; d < numOfDecks; d++){
			
			//for each suit
			for(int s = 0; s < 4; s++){
				
				//for each number
				for(int n = 1; n <= 13; n++){
					this.myCards[c] = new Card(Suit.values()[s], n);
					c++;
				}
			}
		}
		
		//shuffle the deck
		this.shuffle();
		
	}
	
	public void shuffle(){
		
		topIndex = 0;
		
		//inti random number generator
		Random rng = new Random();
		
		//temporary card;
		Card temp;
		
		int j;
		for(int i = 0; i < myCards.length; i++){
			
			//get a random card to swap i's value
			j = rng.nextInt(myCards.length);
			
			//do swap
			temp = myCards[i];
			myCards[i] = myCards[j];
			myCards[j] = temp;
			
		}
	}
	
	public Card dealNextCard(){
		
		try{
			//get the top card of the deck
			Card top = myCards[topIndex];
			topIndex++;
			
			return top;
		}
		catch (ArrayIndexOutOfBoundsException a)
		{
			System.err.println("No more cards in the deck!");
			return null;
		}
	}
	
	/**
	 * Print the top cards in the deck.
	 * 
	 * numToPrint  the number of cards from the top of the deck to print
	 */
	public void printDeck(int numToPrint){
		for (int i = 0; i < numToPrint; i++){
			System.out.printf("% 3d/%d %s\n", i+1, myCards.length, this.myCards[i].toString());
		}
		System.out.printf("\t\t[%d other]\n", myCards.length - numToPrint);
	}

}