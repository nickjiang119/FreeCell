import java.awt.Point;

/**
 * Keeps track of a FreeCell. Inherits data and methods from GHand. Contains a
 * variable and methods to update the number of FreeCells (by adding a GCard or
 * removing a GCard), get the number of FreeCells, and to reset the number of
 * FreeCells. Overrides the abstract GHand methods to check if you can pick up a
 * Movable Object from this FreeCell when given a point and to pick up a Movable
 * Object from this FreeCell when given a point.
 * 
 * @author Ridout and Jessica Jiang
 * @version November 2014
 */
public class FreeCell extends GHand
{
	private static int noOfFreeCells = 4;

	/**
	 * Creates a new FreeCell
	 * 
	 * @param x the x position of the FreeCell
	 * @param y the y position of the FreeCell
	 */
	public FreeCell(int x, int y)
	{
		super(x, y, 0);
	}

	/**
	 * Adds a GCard to this Hand updating the position of the new Card and
	 * adjusting the spacing of the Cards accordingly as well as decreasing the
	 * number of FreeCells if the FreeCell becomes filled
	 * 
	 * @param card the GCard to add
	 */
	public void addCard(GCard card)
	{

		// Decrease the number of FreeCells if this FreeCell is currently empty
		// (it will become non-empty)
		if (this.cardsLeft() == 0)
			noOfFreeCells--;

		super.addCard(card);
	}

	/**
	 * Removes a GCard from this Hand at the given index as well as increasing
	 * the number of FreeCells if the FreeCell becomes empty
	 * 
	 * @param index the index of the GCard to remove
	 */
	public GCard removeCard(int index)
	{
		// Increase the number of FreeCells if this FreeCell will become empty
		if (this.cardsLeft() == 1)
			noOfFreeCells++;

		return super.removeCard(index);

	}

	/**
	 * Checks if you can pick up a single Card from this FreeCell based on the
	 * given point that is being selected.
	 * 
	 * @param point the point of the FreeCell that is selected
	 * @return true if you can pick up a single Card based on the given point,
	 *         false if not
	 * 
	 *         Precondition: The given Point is contained within the Cascade
	 */
	public boolean canPickUp(Point point)
	{
		// If the FreeCell is empty, return false
		if (this.cardsLeft() == 0)
			return false;

		// Return true if there is a Card in the FreeCell
		else
			return true;
	}

	/**
	 * Based on the point of selection returns the Movable Card that you can
	 * pick up.
	 * 
	 * @param point the point of the FreeCell that is selected
	 * @return the Movable Card based on the point of selection
	 * 
	 *         Precondition: canPickUp() is always called before pickUp() to
	 *         ensure that the pickUp point is valid
	 */
	public Movable pickUp(Point point)
	{
		// If the mouse is clicking the top card, return the top card, otherwise
		// return nothing
		if (this.getTopCard().contains(point))
		{
			return removeTopCard();
		}
		else
			return null;
	}

	/**
	 * Returns the number of FreeCells available
	 * 
	 * @return the number of FreeCells available
	 */
	public static int getNoOfFreeCells()
	{
		return noOfFreeCells;
	}

	/**
	 * Resets the number of FreeCells to 4
	 */
	public static void reset()
	{
		noOfFreeCells = 4;
	}
}
