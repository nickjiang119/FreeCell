import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main Frame for a Simple Free cell Game Sets up the menus and places a
 * CardPanel in the Frame You should update the about code to add your name You
 * will also need to add a bit more code to this class
 * 
 * @author Ridout and Jessica Jiang
 * @version November 2014
 */
public class FreeCellMain extends JFrame implements ActionListener
{
	private CardPanel cardArea;
	private JMenuItem newMenuItem, statisticsOption, quitMenuItem;
	private JMenuItem undoOption, aboutMenuItem;
	private JCheckBoxMenuItem autoCompleteMenuItem;

	/**
	 * Creates a FreeCellMain from object
	 */
	public FreeCellMain()
	{
		super("FreeCell");
		setResizable(false);

		// Add in an Icon - Ace of Spades
		setIconImage(new ImageIcon("images\\ace.png").getImage());
		addWindowListener(new CloseWindow());

		// Add in a Simple Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('G');
		newMenuItem = new JMenuItem("New Game");
		newMenuItem.addActionListener(this);

		statisticsOption = new JMenuItem("Statistics");
		statisticsOption.addActionListener(this);

		quitMenuItem = new JMenuItem("Exit");
		quitMenuItem.addActionListener(this);

		undoOption = new JMenuItem("Undo Move");
		undoOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				InputEvent.CTRL_MASK));
		undoOption.addActionListener(this);
		undoOption.setEnabled(false);

		autoCompleteMenuItem = new JCheckBoxMenuItem("Auto Complete", true);
		autoCompleteMenuItem.addActionListener(this);

		gameMenu.add(newMenuItem);
		gameMenu.add(statisticsOption);
		gameMenu.add(undoOption);
		gameMenu.add(autoCompleteMenuItem);
		gameMenu.addSeparator();
		gameMenu.add(quitMenuItem);
		menuBar.add(gameMenu);

		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		aboutMenuItem = new JMenuItem("About...");
		aboutMenuItem.addActionListener(this);
		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);

		// Set up the layout and add in a CardPanel for the card area
		setLayout(new BorderLayout());
		cardArea = new CardPanel(this);
		add(cardArea, BorderLayout.CENTER);

		// Centre the frame in the middle (almost) of the screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setVisible(true);
		setLocation((screen.width - CardPanel.WIDTH) / 2 - this.getWidth(),
				(screen.height - CardPanel.HEIGHT) / 2 - this.getHeight());
	}

	/**
	 * Method that deals with the menu options
	 * 
	 * @param event the event that triggered this method
	 */
	public void actionPerformed(ActionEvent event)
	{
		Statistics stats = cardArea.getStats();

		if (event.getSource() == newMenuItem)
		{
			cardArea.newGame();
		}
		else if (event.getSource() == statisticsOption)
		{
			JOptionPane.showMessageDialog(cardArea,stats, "Statistics",
					JOptionPane.INFORMATION_MESSAGE);
		}
		else if (event.getSource() == quitMenuItem)
		{
			// Check if the user is sure they want to quit
			int option = JOptionPane
					.showConfirmDialog(
							cardArea,
							"If you exit now, any game in progress will count as a loss.\n\nAre you sure you want to quit?",
							"Warning!", JOptionPane.YES_NO_OPTION);

			// Reset the win streak if the user has not won and is quitting
			if (option == JOptionPane.YES_OPTION)
			{
				// Reset the win streak if the user has not won and is in game
				// and is quitting
				if (cardArea.isPlayingGame())
				{
					stats.resetCurrentWinStreak();
				}
				stats.writeToFile("stats.dat");
				System.exit(0);
			}
		}
		else if (event.getSource() == undoOption)
		{
			cardArea.undo();
			if (!cardArea.canUndo())
				setUndoOption(false);
		}

		else if (event.getSource() == aboutMenuItem)
		{
			JOptionPane.showMessageDialog(cardArea,
					"FreeCell by Ridout\nand Jessica Jiang\n\u00a9 2014",
					"About FreeCell", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (event.getSource() == autoCompleteMenuItem)
		{
			cardArea.setAutoComplete(autoCompleteMenuItem.isSelected());
		}
	}

	/**
	 * Sets the Undo object in the Menu
	 * 
	 * @param canUndo if you can undo or not
	 */
	public void setUndoOption(boolean canUndo)
	{
		this.undoOption.setEnabled(canUndo);
	}

	private class CloseWindow extends WindowAdapter
	{
		// Deals with window closing
		public void windowClosing(WindowEvent event)
		{
			Statistics stats = cardArea.getStats();

			int option = JOptionPane
					.showConfirmDialog(
							cardArea,
							"If you exit now, any game in progress will count as a loss.\n\nAre you sure you want to quit?",
							"Warning!", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION)
			{
				// Reset the win streak if the user is currently playing the
				// game and is quitting
				if (cardArea.isPlayingGame())
				{
					stats.resetCurrentWinStreak();
				}
				stats.writeToFile("stats.dat");
				System.exit(0);
			}
		}
	}

	public static void main(String[] args)
	{
		FreeCellMain frame = new FreeCellMain();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
