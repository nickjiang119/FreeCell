import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * A Hand class that stores the Cards in the Hand. Can construct a Hand given no
 * parameters (an empty Hand) or a String. Includes methods to print out the
 * Hand as a String, to add a Card to the Hand, to sort the Hand by rank or by
 * suit, to get the Blackjack value of the Hand, to check if the current Hand is
 * a Blackjack, to clear this Hand and to check the number of Cards left in the
 * Hand.
 * 
 * @author Jessica Jiang
 * @version November 8, 2014
 * 
 */
public class Hand
{
	protected ArrayList<Card> hand;

	/**
	 * Creates an empty Hand
	 */
	public Hand()
	{
		hand = new ArrayList<Card>();
	}

	/**
	 * Creates a Hew hand using the given Cards in the form of a String
	 * 
	 * @param hand the Cards in the form of a String
	 */
	public Hand(String hand)
	{
		this.hand = new ArrayList<Card>(hand.length() / 3 + 1);

		Scanner sc = new Scanner(hand);

		while (sc.hasNext())
		{
			this.hand.add(new Card(sc.next()));
		}

		sc.close();
	}

	/**
	 * Adds the given Card to the Hand
	 * 
	 * @param card the given Card
	 */
	public void addCard(Card card)
	{
		hand.add(card);
	}

	/**
	 * Sorts the Hand by rank
	 */
	public void sortByRank()
	{
		Collections.sort(hand);
	}

	/**
	 * Sorts the Hand by suit
	 */
	public void sortBySuit()
	{
		Collections.sort(hand, Card.SUIT_ORDER);
	}

	/**
	 * Returns the Cards in the Hand as a String
	 * 
	 * @return the Hand as a String
	 */
	public String toString()
	{
		StringBuilder strHand = new StringBuilder(hand.size() * 3);

		for (Card nextCard : hand)
		{
			strHand.append(nextCard);
			strHand.append(' ');
		}

		return strHand.toString();
	}

	/**
	 * Returns the highest blackjack value without busting (if possible)
	 * 
	 * @return the highest blackjack value without busting (if possible)
	 */
	public int getValue()
	{
		int totalValue = 0;
		int aceCount = 0;

		// Go through the entire hand
		for (Card nextCard : hand)
		{
			// Count the aces
			if (nextCard.isAce())
				aceCount++;

			// Add the value
			totalValue += nextCard.getValue();
		}

		// Change the value of Aces to 11 if possible
		while (aceCount > 0 && totalValue + 10 <= 21)
		{
			aceCount--;
			totalValue += 10;
		}

		return totalValue;
	}

	/**
	 * Checks if the current Hand is a blackjack(two cards add up to 21) or not
	 * 
	 * @return true if it is a blackjack, false if it is not
	 */
	public boolean isBlackJack()
	{
		if (hand.size() != 2 || this.getValue() != 21)
			return false;

		return true;

	}

	/**
	 * Clears the Hand (removes all Cards from the Hand)
	 */
	public void clear()
	{
		hand.clear();
	}

	/**
	 * Finds the number of Cards left in the Hand
	 * 
	 * @return the number of Cards left in the Hand
	 */
	public int cardsLeft()
	{
		return hand.size();
	}
}
