/**
 * A Deck class that stores the Cards in the Deck and the top card. Includes
 * methods to print out the Deck as a String, to shuffle the Deck, to find the
 * number of Cards left in the Deck, and to deal a Card from the Deck.
 * 
 * @author Jessica Jiang
 * @version November 8, 2014
 */
public class Deck
{
	protected Card[] deck;
	protected int topCard;

	/**
	 * Constructs the given number of Decks
	 * 
	 * @param noOfDecks the number of Decks
	 */
	public Deck(int noOfDecks)
	{
		deck = new Card[52 * noOfDecks];
		int cardIndex = 0;

		// Generate the Cards
		for (int currentDeck = 1; currentDeck <= noOfDecks; currentDeck++)
		{
			for (int rank = 1; rank <= 13; rank++)
			{
				for (int suit = 1; suit <= 4; suit++)
				{
					deck[cardIndex] = new Card(rank, suit);
					cardIndex++;
				}
			}
		}

		this.topCard = deck.length;
	}

	/**
	 * Constructs a new Deck of 52 cards
	 */
	public Deck()
	{
		this(1);
	}

	/**
	 * Shuffles the cards in the deck using the Fisher-Yates shuffle. All cards
	 * will be face down and top card is reset to the top of the deck.
	 */
	public void shuffle()
	{
		for (int i = 0; i < deck.length; i++)
		{
			// If the Card is face up, flip it so it becomes face down
			if (deck[i].isFaceUp())
				deck[i].flip();
		}

		// Go through the Deck backwards
		for (int i = deck.length - 1; i > 0; i--)
		{
			// Generate a random number from 0 to the current card
			int card = (int) (Math.random() * (i + 1));

			// Swap the current Card with the random Card (from 1 to i)
			Card swap = deck[i];
			deck[i] = deck[card];
			deck[card] = swap;
		}

		// Reset topCard
		topCard = deck.length;
	}

	/**
	 * Finds the number of Cards left in the Deck
	 * 
	 * @return the number of Cards left in the Deck
	 */
	public int cardsLeft()
	{
		return topCard;
	}

	/**
	 * Deals a Card
	 * 
	 * @return the Card dealt
	 */
	public Card dealCard()
	{
		if (topCard == 0)
			return null;

		topCard--;
		return deck[topCard];
	}

	/**
	 * Returns the remaining Cards in the Deck as a String
	 * 
	 * @return the Deck as a String
	 */
	public String toString()
	{
		StringBuilder strDeck = new StringBuilder(topCard * 3);
		for (int i = topCard - 1; i >= 0; i--)
		{
			strDeck.append(deck[i]);
			strDeck.append(' ');
		}

		return strDeck.toString();
	}
}
