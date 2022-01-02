import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Looks after most of the FreeCell Game. Add your own comments to explain what
 * all of the methods and any new methods do
 * 
 * @author Ridout and Jessica Jiang
 * @version November 2014
 */
public class CardPanel extends JPanel implements MouseListener,
		MouseMotionListener
{
	// Constants for the table layout
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private final Color TABLE_COLOUR = new Color(140, 225, 140);
	private final int ANIMATION_FRAMES = 6;

	// Constants for layout of card area
	private final int NO_OF_CASCADES = 8;
	private final int NO_OF_FREECELLS = 4;
	private final int NO_OF_FOUNDATIONS = 4;
	private final int CASCADE_X = 30;
	private final int CASCADE_Y = 150;
	private final int CASCADE_SPACING = 95;
	private final int FREECELL_X = 30;
	private final int FREECELL_Y = 30;
	private final int TOP_SPACING = 90;
	private final int FOUNDATION_X = 425;
	private final int FOUNDATION_Y = 30;

	// Variables for the FreeCell Game
	private FreeCellMain parentFrame;
	private LinkedList<Move> moves;

	private GDeck myDeck;
	private ArrayList<GHand> allHands;
	private Movable selectedItem;
	private GHand sourceHand;
	private Point lastPoint;
	private GCard movingCard;
	private boolean animate = false;
	private Statistics stats;
	private boolean autoCompleteOn = true;
	private boolean isPlayingGame = false;

	/**
	 * Constructs a CardPanel by setting up the Panel and the Deck and all of
	 * required Hands to keep track of the free cells, foundations and cascades.
	 * Also sets up listeners for mouse events and a move list
	 * 
	 * @param parentFrame the main Frame that holds this panel
	 */
	public CardPanel(FreeCellMain parentFrame)
	{
		// Set up the size and background colour
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(TABLE_COLOUR);
		this.parentFrame = parentFrame;

		// Add mouse listeners to the card panel
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		// Set up the deck, cascades, foundations and free cells
		myDeck = new GDeck(400 - GCard.WIDTH / 2, 470);
		allHands = new ArrayList<GHand>();

		// Create Cascades
		int xCascade = CASCADE_X;
		int yCascade = CASCADE_Y;
		for (int i = 0; i < NO_OF_CASCADES; i++)
		{
			allHands.add(new Cascade(xCascade, yCascade));
			xCascade += CASCADE_SPACING;
		}

		// Create Free cells
		int xFreeCell = this.FREECELL_X;
		int yFreeCell = this.FREECELL_Y;
		for (int i = 0; i < this.NO_OF_FREECELLS; i++)
		{
			allHands.add(new FreeCell(xFreeCell, yFreeCell));
			xFreeCell += TOP_SPACING;
		}

		// Create Foundations
		int xFoundation = FOUNDATION_X;
		int yFoundation = FOUNDATION_Y;
		for (int i = 0; i < this.NO_OF_FOUNDATIONS; i++)
		{
			allHands.add(new Foundation(xFoundation, yFoundation));
			xFoundation += TOP_SPACING;
		}

		movingCard = null;
		moves = new LinkedList<Move>();

		// Read in the statistics
		stats = Statistics.readFromFile("stats.dat");

		// Set default settings (at beginning, user is not currently playing
		// game and auto complete is on)
		autoCompleteOn = true;
		isPlayingGame = false;
	}

	/**
	 * Starts up a new game by clearing all of the Hands, shuffling the Deck and
	 * dealing new Cards to the Cascades. Also resets the move list
	 */
	public void newGame()
	{
		// Clear out all of the Hands
		for (Hand next : allHands)
			next.clear();

		myDeck.shuffle();

		// Deal the Cards to the Cascades (first 8 Hands)
		int cascadeIndex = 0;
		while (myDeck.cardsLeft() > 0)
		{
			GCard dealtCard = myDeck.dealCard();
			Point pos = new Point(dealtCard.getPosition());
			allHands.get(cascadeIndex).addCard(dealtCard);
			Point finalPos = new Point(dealtCard.getPosition());
			if (animate)
				moveACard(dealtCard, pos, finalPos);
			if (!dealtCard.isFaceUp())
				dealtCard.flip();
			cascadeIndex++;
			if (cascadeIndex == NO_OF_CASCADES)
				cascadeIndex = 0;

		}

		moves.clear();
		parentFrame.setUndoOption(false);

		// Add to the number of games
		stats.increaseTotalGames();

		// Reset the number of empty freecells and cascades as well as setting
		// the in game status to true
		Cascade.reset();
		FreeCell.reset();

		// If user was previously in game, reset win streak
		if (isPlayingGame)
			stats.resetCurrentWinStreak();
		
		// The user is now in game
		isPlayingGame = true;

		repaint();
	}

	/**
	 * Undoes the last move
	 */
	public void undo()
	{
		if (canUndo())
		{
			Move lastMove = moves.removeLast();
			lastMove.undo();
			repaint();
		}
	}

	/**
	 * Gets the current Statistics for this FreeCell game
	 * 
	 * @return the current Statistics for this FreeCell game
	 */
	public Statistics getStats()
	{
		return stats;
	}

	/**
	 * Turns the Auto Complete on or off based on the given boolean
	 * 
	 * @param autoCompleteSetting the given boolean should be true if the
	 *            autoComplete is on, false if not
	 */
	public void setAutoComplete(boolean autoCompleteSetting)
	{
		autoCompleteOn = autoCompleteSetting;
	}

	/**
	 * Checks if there are any moves in the moves list so that we can see if it
	 * is ok to undo a move
	 * 
	 * @return true if we can undo, false if not
	 */
	public boolean canUndo()
	{
		return !moves.isEmpty();
	}

	/**
	 * Draws the information in this CardPanel. Draws the Deck, all of the
	 * hands, the moving Card in an animation and the selected Card or GHand
	 * 
	 * @param g the Graphics context to do the drawing
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// Draw the deck if there are cards left
		if (myDeck.cardsLeft() > 0)
			myDeck.draw(g);

		// Draw all of the Hands
		for (GHand next : allHands)
			next.draw(g);

		// For animation to draw the moving Card
		if (movingCard != null)
			movingCard.draw(g);

		// Draw selected GHand or Card on top
		if (selectedItem != null)
			selectedItem.draw(g);
	}

	/**
	 * Auto moves any Cards up to the Foundations when possible
	 */
	private void autoComplete()
	{
		// Keeps track of whether or not a Card has been moved during the auto
		// complete
		boolean cardMoved = true;

		while (cardMoved)
		{
			cardMoved = false;

			// Go through all the Cascades and FreeCells
			for (GHand hand : allHands.subList(0, NO_OF_CASCADES
					+ NO_OF_FREECELLS))
			{
				// Check every Foundation
				for (GHand foundation : allHands.subList(NO_OF_CASCADES
						+ NO_OF_FREECELLS, allHands.size()))
				{
					// Check if the top Card can be placed on the foundation (if
					// applicable)
					if (hand.cardsLeft() != 0)
					{
						GCard topCard = hand.getTopCard();

						if (topCard.canPlaceOn(foundation))
						{
							// If the Card can be placed on the Foundation,
							// check if there are any Cards in the Cascades or
							// FreeCells that can be placed on the current Hand
							boolean move = true;

							// Go through all the Cascades and FreeCells
							for (GHand handCheck : allHands.subList(0,
									NO_OF_CASCADES + NO_OF_FREECELLS))
							{
								// If the top Card of the Hand that is being
								// checked can be placed on top of the current
								// Hand, do not move the Card in the current
								// Hand to the Foundation
								// Also move any 2s by default
								if (handCheck.cardsLeft() != 0)
								{
									if (handCheck.canPlaceOnCard(topCard)
											&& topCard.getValue() != 2)
										move = false;
								}
							}

							// Move the Card if possible
							if (move)
							{
								cardMoved = true;

								Point startPos = topCard.getPosition();
								topCard.placeOn(foundation);

								Point endPos = topCard.getPosition();
								hand.removeTopCard();

								// Add the move to the list
								moves.add(new Move(hand, foundation, topCard));

								// Show the animation for the moving Card
								moveACard(topCard, startPos, endPos);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Checks to see if the player has won by completing all of the foundations
	 * 
	 * @return true if they have won, false if not
	 */
	private boolean checkForWinner()
	{
		for (GHand nextFoundation : allHands.subList(NO_OF_CASCADES
				+ NO_OF_FREECELLS, allHands.size()))
		{
			if (nextFoundation.cardsLeft() < 13)
				return false;
		}

		return true;
	}

	/**
	 * Checks if the user is currently playing a game
	 * 
	 * @return true if the user is playing, false if not
	 */
	public boolean isPlayingGame()
	{
		return isPlayingGame;
	}

	/**
	 * Moves a Card during the animation
	 * 
	 * @param cardToMove Card that you want to move
	 * @param fromPos initial position of the Card
	 * @param toPos final position of the Card
	 */
	public void moveACard(final GCard cardToMove, Point fromPos, Point toPos)
	{
		int dx = (toPos.x - fromPos.x) / ANIMATION_FRAMES;
		int dy = (toPos.y - fromPos.y) / ANIMATION_FRAMES;

		for (int times = 1; times <= ANIMATION_FRAMES; times++)
		{
			fromPos.x += dx;
			fromPos.y += dy;
			cardToMove.setPosition(fromPos);

			// Update the drawing area
			paintImmediately(0, 0, getWidth(), getHeight());
			delay(30);

		}
		cardToMove.setPosition(toPos);
	}

	/**
	 * Delays the given number of milliseconds
	 * 
	 * @param milliSec number of milliseconds to delay
	 */
	private void delay(int milliSec)
	{
		try
		{
			Thread.sleep(milliSec);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * Handles the mouse pressed events to pick a Card or a Tableau
	 * 
	 * @param event event information for mouse pressed
	 */
	public void mousePressed(MouseEvent event)
	{
		if (selectedItem != null)
			return;

		Point selectedPoint = event.getPoint();

		// Pick up one of cards from a Hand (FreeCell or Cascade)
		// Could also pick up from a Foundation if you want
		for (GHand nextHand : allHands)
			if (nextHand.contains(selectedPoint)
					&& nextHand.canPickUp(selectedPoint))
			{
				// Split off a section of the Cascade or pick up a Card
				selectedItem = nextHand.pickUp(selectedPoint);

				// In case our move is not valid, we want to return the
				// Card(s) to where they initially came from
				sourceHand = nextHand;
				lastPoint = selectedPoint;
				repaint();
				return;
			}
	}

	/**
	 * Handles the mouse released events to drop a Card or a Tableau
	 * 
	 * @param event event information for mouse released
	 */
	public void mouseReleased(MouseEvent event)
	{
		if (selectedItem != null)
		{
			// Check to see if we can add this to another cascade
			// foundation or free cell
			for (GHand nextHand : allHands)
				if (selectedItem.intersects(nextHand)
						&& selectedItem.canPlaceOn(nextHand))
				{
					selectedItem.placeOn(nextHand);

					// Count this move if you didn't place it on the same spot
					if (nextHand != sourceHand)
					{
						moves.addLast(new Move(sourceHand, nextHand,
								selectedItem));
						parentFrame.setUndoOption(true);
					}
					selectedItem = null;
					repaint();

					// Check these things after a Card is dropped
					if (autoCompleteOn)
						autoComplete();
					if (checkForWinner())
					{
						JOptionPane.showMessageDialog(parentFrame,
								"Congratulations!!", "You win!",
								JOptionPane.INFORMATION_MESSAGE);
						parentFrame.setUndoOption(false);

						// Update the number of wins, win percentage and the win
						// streak
						stats.addWins();

						// The user has won, therefore they are not currently
						// playing
						isPlayingGame = false;
					}
					return;
				}

			// Return to original spot if not a valid move
			selectedItem.placeOn(sourceHand);
			selectedItem = null;
			repaint();
		}
	}

	/**
	 * Handles the mouse dragged events to drag the moving card(s)
	 * 
	 * @param event event information for mouse dragged
	 */
	public void mouseDragged(MouseEvent event)
	{
		Point currentPoint = event.getPoint();

		if (selectedItem != null)
		{
			// We use the difference between the lastPoint and the
			// currentPoint to move the Cascade or Card so that the position of
			// the mouse on the Cascade/Card doesn't matter.
			// i.e. we can drag the card from any point on the card image
			selectedItem.move(lastPoint, currentPoint);
			lastPoint = currentPoint;
			repaint();
		}

	}

	/**
	 * Handles the mouse moved events to show which Cards can be picked up
	 * 
	 * @param event event information for mouse moved
	 */
	public void mouseMoved(MouseEvent event)
	{
		// Set the cursor to the hand if we are on a card that we can pick up
		Point currentPoint = event.getPoint();
		for (GHand nextHand : allHands)
			if (nextHand.contains(currentPoint)
					&& nextHand.canPickUp(currentPoint))
			{
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				return;
			}

		// Otherwise we just use the default cursor
		setCursor(Cursor.getDefaultCursor());

	}

	// Extra methods needed since we implemented MouseListener
	// Not implemented in this class

	@Override
	public void mouseClicked(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}
}
