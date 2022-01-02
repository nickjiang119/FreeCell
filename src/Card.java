import java.util.Comparator;

/**
 * A Card class that stores the rank and suit of a Card Object and whether or
 * not the Card is face up. Can construct a Card given two integers (rank, suit)
 * or given a String. Includes methods to print out the Card as a String, to
 * flip the Card, check if the Card is face up, compare Cards by rank then suit
 * check if the Card is an ace, to get the Blackjack value of the card, to check
 * if this Card can be placed on a Cascade when given the top Card of the
 * Cascade in a game of FreeCell and to check if this Card can be placed on a
 * Foundation when given the top Card of the Foundation in a game of FreeCell.
 * Includes a Comparator to compare two Card Objects by suit then rank.
 * 
 * @author Jessica Jiang
 * @version November 8, 2014
 */
public class Card implements Comparable<Card>
{
	private int rank; // Ace - 1, 2 - 10, Jack - 11, Queen - 12, King - 13
	private int suit; // Clubs - 1, Diamonds - 2, Hearts - 3, Spades - 4
	protected boolean isFaceUp;

	private static final String SUITS = " CDHS";
	private static final String RANKS = " A23456789TJQK";

	public static final Comparator<Card> SUIT_ORDER = new SuitOrder();

	/**
	 * Constructs a new Card Object with a given rank and suit
	 * 
	 * @param rank the given rank as an integer
	 * @param suit the given suit as an integer
	 */
	public Card(int rank, int suit)
	{
		this.rank = rank;
		this.suit = suit;
		this.isFaceUp = false;
	}

	/**
	 * Constructs a new Card Object using a String in the form of RANKSUIT (ex.
	 * 2H). If the String is in upper case, the Card is face up; if the String
	 * is in lower case, the Card is face down.
	 * 
	 * @param card the Card as a String in the form of RANKSUIT
	 */
	public Card(String card)
	{
		if (Character.isUpperCase(card.charAt(1)))
		{
			this.rank = RANKS.indexOf(card.charAt(0));
			this.suit = SUITS.indexOf(card.charAt(1));
			this.isFaceUp = true;
		}
		else
		{
			card = card.toUpperCase();
			this.rank = RANKS.indexOf(card.charAt(0));
			this.suit = SUITS.indexOf(card.charAt(1));
			this.isFaceUp = false;
		}
	}

	/**
	 * Returns the Card as a String in the format of RANKSUIT. If the String is
	 * in upper case, the Card is face up; if the String is in lower case, the
	 * Card is face down.
	 * 
	 * @return the Card as a String
	 */
	public String toString()
	{
		// Create the String (will be in upper case)
		String card = "" + RANKS.charAt(rank) + SUITS.charAt(suit);

		// Face up card, return upper case
		if (isFaceUp)
			return String.format("%s", card);

		// Face down card, return lower case
		return String.format("%s", card.toLowerCase());
	}

	/**
	 * Flips the card
	 */
	public void flip()
	{
		this.isFaceUp = !isFaceUp;
	}

	/**
	 * Checks if the Card is face up
	 * 
	 * @return true if the Card is face up, false if the Card is face down
	 */
	public boolean isFaceUp()
	{
		return isFaceUp;
	}

	/**
	 * Compares two Card Objects by rank then suit (if ranks are equal, then it
	 * will compare the suits)
	 * 
	 * @param other the other Card to compare
	 * @return a value < 0 if this Card's rank is less than the other Card's
	 *         rank or if the ranks are the same, but this Card's suit is less
	 *         than the other Card's suit; a value > 0 if this Card's rank is
	 *         greater than the other Card's rank or if the ranks are the same,
	 *         but this Card's suit is greater than the other Card's suit; and 0
	 *         if the Cards are the same
	 */
	public int compareTo(Card other)
	{
		// Compare by rank if the ranks are not the same
		if (this.rank != other.rank)
			return this.rank - other.rank;

		// Compare by suit when the ranks are equal
		return this.suit - other.suit;
	}

	/**
	 * Checks if this Card is an ace
	 * 
	 * @return true if the Card is an ace, false if it is not an ace
	 */
	public boolean isAce()
	{
		if (this.rank == 1)
			return true;

		return false;
	}

	/**
	 * Gets the Blackjack value of the card (Ace - 1; 2 - 10; Jack, Queen and
	 * King - 10)
	 * 
	 * @return the Blackjack value of the card
	 */
	public int getValue()
	{
		if (this.rank >= 11)
			return 10;

		return rank;
	}

	/**
	 * Checks if the current Card can be placed on the Cascade, given the top
	 * Card of the Cascade
	 * 
	 * @param otherCard the top Card on the Cascade
	 * @return true if the Card can be placed on the Cascade (Cards are opposite
	 *         colours and the other Card is one rank higher than this Card),
	 *         false if not
	 */
	public boolean canPlaceOnCascade(Card otherCard)
	{

		if (otherCard.rank - this.rank == 1
				&& (this.suit + otherCard.suit != 5 && this.suit != otherCard.suit))
			return true;
		else
			return false;
	}

	/**
	 * Checks if the current Card can be placed on the Foundation, given the top
	 * Card of the Foundation
	 * 
	 * @param otherCard the top Card on the Foundation
	 * @return true if the Card can be placed on the Foundation (Cards are same
	 *         suit and the this Card is one rank higher than other Card), false
	 *         if not
	 */
	public boolean canPlaceOnFoundation(Card otherCard)
	{
		if (this.rank - otherCard.rank == 1 && this.suit == otherCard.suit)
			return true;

		return false;
	}

	/**
	 * An inner Comparator class that compares two Cards by their suit order
	 * 
	 */
	private static class SuitOrder implements Comparator<Card>
	{
		/**
		 * Compares two Card Objects by suit then rank (if suits are equal, then
		 * it will compare the ranks)
		 * 
		 * @param first the first Card to compare
		 * @param second the second Card to compare
		 * @return a value < 0 if this Card's suit is less than the other Card's
		 *         suit or if the suits are the same, but this Card's rank
		 *         is less than the other Card's rank; a value > 0 if this
		 *         Card's suit is greater than the other Card's suit or if the
		 *         suits are the same, but this Card's rank is greater than
		 *         the other Card's rank; and 0 if the Cards are the same
		 */
		public int compare(Card first, Card second)
		{
			// Compare by suit if the suits are not the same
			if (first.suit != second.suit)
				return first.suit - second.suit;

			// Compare by rank when the suits are equal
			return first.rank - second.rank;
		}
	}

}
