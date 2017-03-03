package GUI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import project2.*;

public class GUI extends JFrame implements ActionListener {
	private JLabel[][] grid;
	private NumberGame game;
	private JPanel centerPane; // Acts as the container for the game's grid
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem quit, newGame;
	private final ImageIcon BLANK_TILE = new ImageIcon("/home/measure/Dropbox/eclipseWorkspace/cis163/project2/src/GUI/tiles/blankTile.png");
	private final int HEIGHT = 4;
	private final int WIDTH = 4;
	private final int WINVAL = 1024;
	
	public static void main(String[] args) {
		GUI gui = new GUI();
	}
	




	public GUI() {
		setTitle("1024!");
		setLayout(new BorderLayout());
		game = new NumberGame();
		game.resizeBoard(HEIGHT, WIDTH, WINVAL);
		
		
		
		centerPane = new JPanel(new GridLayout(HEIGHT, WIDTH));
		add(centerPane, BorderLayout.CENTER);
		setupGrid();
		updateGrid();
		
		
		
		
		
		
		setupMenus();
		pack();
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		/*.....JMenu.....*/
		if(e.getSource() == quit) {
			System.exit(1);
		}
		
		if(e.getSource() == newGame) {
			game.reset();
			updateGrid();
		}
	}
	
	
	private void setupGrid() {
		grid = new JLabel[HEIGHT][WIDTH];
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				grid[i][j] = new JLabel();
				centerPane.add(grid[i][j]);
			}
		}
	}
	
	
	private void updateGrid() {
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				grid[i][j].setIcon(BLANK_TILE);
				//TODO: Keylistener and tile design.
			}
		}
		
		for (Cell c: game.getNonEmptyTiles()) {
			grid[c.row][c.column].setText("" + c.value);
		}
	}
	
	private void setupMenus() {
		menuBar = new JMenuBar();
		menu = new JMenu("Menu");
		
		quit = new JMenuItem("Quit");
		quit.addActionListener(this);
		
		newGame = new JMenuItem("New Game");
		newGame.addActionListener(this);
		
		menu.add(quit);
		menu.add(newGame);
		menuBar.add(menu);
		setJMenuBar(menuBar);
	}
	
	private void playLoop() {
		
	}
	
}
 