/**
 * Keeps track of a Move
 * 
 * @author ICS4U and ...
 * @version November 2014
 */
public class Move
{
	private GHand from;
	private GHand to;
	private Movable moved; // What you moved

	public Move(GHand from, GHand to, Movable moved)
	{
		this.from = from;
		this.to = to;
		this.moved = moved;
	}

	/**
	 * Undoes the last moves
	 */
	public void undo()
	{
		// When the Object moved was a Card, move the Card directly back
		if (moved instanceof GCard)
		{
			from.addCard(to.removeTopCard());
		}
		else
		{
			// The Object moved was a Tableau
			Tableau movedTableau = (Tableau) moved;
			
			// Remove the Tableau from where it was placed
			for (int card = 0; card < movedTableau.cardsLeft(); card++)
				to.removeTopCard();

			// Place the Tableau on the Cascade it came from
			movedTableau.placeOn(from);
		}

	}
}
