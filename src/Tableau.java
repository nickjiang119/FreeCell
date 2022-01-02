import java.awt.Point;
import java.awt.Rectangle;

/**
 * Keeps track of a moving Cascade known as a Tableau. Inherits data and methods
 * from Cascade. Contains a variable to keep track of the Cascade the Tableau
 * came from. Overrides the abstract Movable methods to move this Tableau
 * (change position), to check if this Tableau intersects another GHand, to
 * check if this Tableau can be placed on a given GHand and to place this
 * Tableau on a given GHand.
 * 
 * @author Ridout and Jessica Jiang
 * @version November 2014
 */
public class Tableau extends Cascade implements Movable
{
	private Cascade sourceHand;

	/**
	 * Creates a new Tableau
	 * 
	 * @param x the x position of the Tableau
	 * @param y the y position of the Tableau
	 * @param sourceHand the Cascade the Tableau came from
	 */
	public Tableau(int x, int y, Cascade sourceHand)
	{
		super(x, y);
		this.sourceHand = sourceHand;
	}

	/**
	 * Moves a Tableau by the amount between the initial and final position
	 * 
	 * @param initialPos the initial position to start dragging this Tableau
	 * @param initialPos the final position to keep dragging this Tableau
	 */
	public void move(Point initialPos, Point finalPos)
	{
		// Move the Tableau
		position.x += finalPos.x - initialPos.x;
		position.y += finalPos.y - initialPos.y;

		// Move the Cards
		for (Card next : hand)
		{
			GCard nextGCard = (GCard) next;
			nextGCard.move(initialPos, finalPos);
		}

	}

	/**
	 * Checks if this Tableau intersects a given Hand
	 * 
	 * @param the given Hand to check
	 * @return true if this Tableau is in the Hand, false if not
	 */
	public boolean intersects(GHand otherHand)
	{
		return (this.getRectangle()).intersects(otherHand.getRectangle());
	}

	/**
	 * Checks to see if you can place this object on the given GHand.
	 * 
	 * @param otherHand the GHand we want to place this object on
	 * @return true if this object can be placed on the given GHand, false if
	 *         not
	 */
	public boolean canPlaceOn(GHand otherHand)
	{
		// Cannot place Tableau on FreeCell or Foundation
		if (otherHand instanceof FreeCell || otherHand instanceof Foundation)
			return false;

		Card thisCard = this.hand.get(0);
		Card otherCard = otherHand.getTopCard();

		// The GHand is a Cascade
		// Calculate the maximum number of Cards you can move at once
		int maxCards = FreeCell.getNoOfFreeCells() + 1;
		int noOfOpenCascades = Cascade.getNoOfOpenCascades();

		// Make sure you do not count the sourceHand as an open Cascade
		if (sourceHand.cardsLeft() == 0)
			noOfOpenCascades--;

		// Make sure you do not count the Hand you are placing the Tableau on as
		// an open Cascade as well
		if (otherHand.cardsLeft() == 0)
			noOfOpenCascades--;

		for (int i = 0; i < noOfOpenCascades; i++)
			maxCards *= 2;

		// Return true if the size of the Tableau is not too large and if you
		// can
		// place the Tableau on the Cascade
		if (this.cardsLeft() <= maxCards)
		{
			if (otherHand.cardsLeft() == 0)
				return true;

			return thisCard.canPlaceOnCascade(otherCard);
		}

		// The size of the Tableau is too large
		return false;
	}

	/**
	 * Places this Tableau on the given GHand adding all of the Cards in this
	 * object to the GHand
	 * 
	 * @param otherHand the GHand to place this object on
	 * 
	 *            Precondition: canPlaceOn is called before placeOn to ensure
	 *            that the move is valid
	 */
	public void placeOn(GHand otherHand)
	{
		for (Card card : hand)
		{
			otherHand.addCard((GCard) card);
		}
	}
}
