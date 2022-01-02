import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Time;

/**
 * A Statistics class that stores the number of wins, percentage of wins,
 * longest win streak, current win streak for a game of FreeCell.
 * 
 * @author Jessica Jiang
 * @version November 29, 2014
 */
public class Statistics implements Serializable
{
	private int noOfWins;
	private int totalGames;
	private double percentWins;
	private int currentStreak;
	private int longestStreak;

	/**
	 * Creates a new Statistics object with all statistics set to 0
	 */
	public Statistics()
	{
		noOfWins = 0;
		percentWins = 0;
		longestStreak = 0;
		currentStreak = 0;
	}

	/**
	 * Increases the number of wins, increases the current win streak, updates
	 * the longest streak if possible and updates the win percentage
	 */
	public void addWins()
	{
		noOfWins++;
		currentStreak++;
		if (currentStreak > longestStreak)
			longestStreak = currentStreak;
		percentWins = noOfWins * 1.0 / totalGames;
	}

	/**
	 * Increases the number of total games played as well as updates the win
	 * percentage
	 */
	public void increaseTotalGames()
	{
		totalGames++;
		percentWins = noOfWins * 1.0 / totalGames;
	}

	/**
	 * Resets the current win streak to 0
	 */
	public void resetCurrentWinStreak()
	{
		currentStreak = 0;
	}

	/**
	 * Prints out all the Statistics as a String
	 * 
	 * @return the Statistics as a String
	 */
	public String toString()
	{
		// return String.format("%s: %d%n%s: %d%n%s: %.2f%%%n%s: %d%n%s: %d%n",
		// "Number of Games", totalGames, "Number of Wins", noOfWins,
		// "Win Rate", percentWins * 100, "Current Win Streak",
		// currentStreak, "Longest Win Streak", longestStreak);

		return String
				.format("%95s %n%112s%n %-25s%-25s%-25s%-25s%-25s%n%18d%40d%32.2f%%%36d%43d%n%n",
						"STATISTICS", "-----------------------------------------------", "Number of Games",
						"Number of Wins", "Win Rate", "Current Win Streak",
						"Longest Win Streak", totalGames, noOfWins,
						percentWins * 100, currentStreak, longestStreak);
	}

	/**
	 * Writes all the Statistics object data to a file
	 * 
	 * @param fileName the name of the file
	 */
	public void writeToFile(String fileName)
	{
		try
		{
			// Write the entire Statistics object to a file
			ObjectOutputStream fileOut = new ObjectOutputStream(
					new FileOutputStream(fileName));
			fileOut.writeObject(this);
			fileOut.close();
		}
		catch (IOException exp)
		{
			System.out.println("Error writing to the file");
		}
	}

	/**
	 * Reads all the Statistics object data from a file and returns the
	 * Statistics
	 * 
	 * @param fileName the name of the file
	 * @return the Statistics object with all the data
	 */
	public static Statistics readFromFile(String fileName)
	{
		try
		{
			ObjectInputStream fileIn = new ObjectInputStream(
					new FileInputStream(fileName));
			Statistics stats = (Statistics) fileIn.readObject();
			fileIn.close();
			return stats;
		}
		catch (Exception exp)
		{
			return new Statistics();
		}
	}

}
