import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

/**
 * Keeps track of a Graphical Card (GCard). Inherits data and methods from Card.
 * Keeps track of a position and an Image for each GCard. Also keeps track of
 * some static variables for the background image and the width and height of
 * each GCard. Includes methods to construct a new GCard, look at and change a
 * GCard's position and draw this GCard. Overrides the abstract Movable methods
 * to check if this GCard intersects a given GHand, to check if a given point is
 * contained within this GCard, to check if the GCard can be placed on a given
 * GHand and to place the GCard on a given GHand.
 * 
 * @author Ridout and Jessica Jiang
 * @version November 2014
 * 
 */
public class GCard extends Card implements Movable
{
	public final static Image BACK_IMAGE = new ImageIcon("images\\blueback.png")
			.getImage();
	public final static int WIDTH = BACK_IMAGE.getWidth(null);
	public final static int HEIGHT = BACK_IMAGE.getHeight(null);

	private Point position;
	private Image image;

	/**
	 * Constructs a graphical Card
	 * 
	 * @param rank the rank of the Card
	 * @param suit the suit of the Card
	 * @param position the initial position of the Card
	 */
	public GCard(int rank, int suit, Point position)
	{
		super(rank, suit);
		this.position = position;
		// Load up the appropriate image file for this card
		String imageFileName = "" + " cdhs".charAt(suit) + rank + ".png";
		imageFileName = "images\\" + imageFileName;
		image = new ImageIcon(imageFileName).getImage();

	}

	/**
	 * Sets the current position of this GCard
	 * 
	 * @param position the Card's current position
	 */
	public void setPosition(Point position)
	{
		this.position = position;
	}

	/**
	 * Gets the current position of this GCard
	 * 
	 * @return the Card's current position
	 */
	public Point getPosition()
	{
		return position;
	}

	/**
	 * Draws a card in a Graphics context
	 * 
	 * @param g Graphics to draw the card in
	 */
	public void draw(Graphics g)
	{
		if (isFaceUp)
			g.drawImage(image, position.x, position.y, null);
		else
			g.drawImage(BACK_IMAGE, position.x, position.y, null);
	}

	/**
	 * Moves a Card by the amount between the initial and final position
	 * 
	 * @param initialPos the initial position to start dragging this Card
	 * @param initialPos the final position to keep dragging this Card
	 */
	public void move(Point initialPos, Point finalPos)
	{
		position.x += finalPos.x - initialPos.x;
		position.y += finalPos.y - initialPos.y;
	}

	/**
	 * Checks if the given point is contained within this Card
	 * 
	 * @param the given point to check
	 * @return true if the point is in this Card, false if not
	 */
	public boolean contains(Point point)
	{
		return (new Rectangle(position.x, position.y, WIDTH, HEIGHT))
				.contains(point);
	}

	/**
	 * Checks if this Card intersects a given Hand
	 * 
	 * @param the given Hand to check
	 * @return true if this Card is in the Hand, false if not
	 */
	public boolean intersects(GHand otherHand)
	{
		return new Rectangle(position.x, position.y, WIDTH, HEIGHT)
				.intersects(otherHand.getRectangle());
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
		Card otherCard = otherHand.getTopCard();

		// The GHand is a FreeCell
		if (otherHand instanceof FreeCell)
			return otherHand.cardsLeft() == 0;

		// The GHand is a Foundation
		if (otherHand instanceof Foundation)
		{
			// Foundation is empty
			if (otherHand.cardsLeft() == 0)
			{
				// Placing an Ace is valid
				if (this.isAce())
					return true;

				// Placing anything else is not valid
				else
					return false;
			}

			else
				return this.canPlaceOnFoundation(otherCard);
		}

		// The GHand is a Cascade
		if (otherHand.cardsLeft() == 0)
			return true;
		else
			return this.canPlaceOnCascade(otherCard);
	}

	/**
	 * Places this object on the given GHand this Card to the GHand
	 * 
	 * @param otherHand the GHand to place this Card on
	 * 
	 *            Precondition: canPlaceOn is called before placeOn to ensure
	 *            that the move is valid
	 */
	public void placeOn(GHand otherHand)
	{
		// Add this card to the other Hand
		otherHand.addCard(this);

	}
}
