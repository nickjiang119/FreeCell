import java.awt.Point;

/**
 * Keeps track of a Foundation. Inherits data and methods from GHand. Overrides
 * the abstract GHand methods to check if you can pick up a Movable Object from
 * this Foundation when given a point and to pick up a Movable Object from this
 * Foundation when given a point.
 * 
 * @author Ridout and Jessica Jiang
 * @version November 2014
 */
public class Foundation extends GHand
{
	/**
	 * Creates a new Foundation
	 * 
	 * @param x the x position of the Foundation
	 * @param y the y position of the Foundation
	 */
	public Foundation(int x, int y)
	{
		super(x, y, 0);
	}

	/**
	 * Checks if the Card at the given point can be picked up
	 * 
	 * @param the given point to check
	 * @return false, a Card cannot be removed from the Foundation
	 */
	public boolean canPickUp(Point point)
	{
		return false;
	}

	/**
	 * Based on the point of selection returns the Movable Card that you can
	 * pick up.
	 * 
	 * @param point the point of the Hand that is selected
	 * @return null, a Card cannot be removed from the Foundation
	 */
	public Movable pickUp(Point point)
	{
		return null;
	}
}
