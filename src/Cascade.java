import java.awt.Point;
import java.util.ArrayList;

/**
 * Keeps track of a Cascade. Inherits data and methods from GHand. Contains a
 * variable and methods to update the number of open Cascades (by adding a GCard
 * or removing a GCard), get the number of open Cascades, and to reset the
 * number of open Cascades. Overrides the abstract GHand methods to check if you
 * can pick up a Movable Object from this Cascade when given a point and to pick
 * up a Movable Object from this Cascade when given a point.
 * 
 * @author Ridout and Jessica Jiang
 * @version November 2014
 */
public class Cascade extends GHand
{
	private static int noOfOpenCascades = 0;

	/**
	 * Creates a new Cascade
	 * 
	 * @param x the x position of the Cascade
	 * @param y the y position of the Cascade
	 */
	public Cascade(int x, int y)
	{
		super(x, y, 20);
	}

	/**
	 * Overrides the GHand addCard method to handle the noOfOpenCascades
	 * 
	 * @param card the GCard to add
	 */
	public void addCard(GCard card)
	{

		// Decrease the number of open Cascades if this Cascade is currently
		// empty
		// (it will become non-empty)
		if (this.cardsLeft() == 0 && !(this instanceof Tableau))
			noOfOpenCascades--;

		super.addCard(card);
	}

	/**
	 * Overrides the GHand removeCard method to handle the noOfOpenCascades
	 * 
	 * @param index the index of the GCard to remove
	 */
	public GCard removeCard(int index)
	{
		// Increase the number of open Cascades if this Cascade will become
		// empty
		if (this.cardsLeft() == 1)
			noOfOpenCascades++;

		return super.removeCard(index);

	}

	/**
	 * Resets the number of open Cascades to 0
	 */
	public static void reset()
	{
		noOfOpenCascades = 0;
	}

	/**
	 * Checks if you can pick up a single Card or a tableau of Cards from this
	 * Cascade based on the given point that is being selected.
	 * 
	 * @param point the point of the Cascade that is selected
	 * @return true if you can pick up a single Card or a tableau of Cards based
	 *         on the given point, false if not
	 * 
	 *         Precondition: The given Point is contained within the Cascade
	 */
	public boolean canPickUp(Point point)
	{
		// Cannot pick up from empty Hand
		if (this.cardsLeft() == 0)
			return false;

		// Check if clicking top Card
		if (this.getTopCard().contains(point))
			return true;

		// Find the Card that is being selected
		for (int card = hand.size() - 1; card >= 1
				&& hand.get(card).canPlaceOnCascade(hand.get(card - 1)); card--)
		{
			// Stop searching once the Card that is being clicked has been
			// reached
			if (((GCard) hand.get(card - 1)).contains(point))
				return true;
		}

		return false;
	}

	/**
	 * Based on the point of selection returns the Movable Card or Tableau that
	 * you can pick up.
	 * 
	 * @param point the point of the Cascade that is selected
	 * @return the Movable Card or Tableau based on the point of selection
	 *         Precondition: canPickUp() is always called before pickUp() to
	 *         ensure that the pickUp point is valid
	 */
	public Movable pickUp(Point point)
	{
		// If the mouse is clicking the top Card, return the top Card
		if (this.getTopCard().contains(point))
		{
			return this.removeTopCard();
		}

		else
		{
			// Create the Tableau of Cards
			int card = hand.size() - 1;

			// Find the Card that is being selected
			while (card >= 1
					&& hand.get(card).canPlaceOnCascade(hand.get(card - 1))
					&& !((GCard) hand.get(card - 1)).contains(point))
				card--;

			card--;

			// Initialize the position of the Tableau
			GCard startCard = (GCard) hand.get(card);
			Tableau cards = new Tableau(startCard.getPosition().x,
					startCard.getPosition().y, this);

			// Add in all the Cards
			while (card < hand.size())
				cards.addCard(removeCard(card));

			// Return the Tableau
			return cards;
		}
	}

	/**
	 * Returns the number of open Cascades
	 * 
	 * @return the number of open Cascades
	 */
	public static int getNoOfOpenCascades()
	{
		return noOfOpenCascades;
	}
}
