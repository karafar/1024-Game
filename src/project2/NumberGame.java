/**
 * @author Farid Karadsheh
 * @version 2-12-2017
 * Project 2 - 1024 Game - Winter 2017 GVSU CIS 163
 */

package project2;

import java.util.*;

public class NumberGame implements NumberSlider {
	// 2D array that represents the game's board. Determined by user input.
	private int[][] gameBoard;		
	// The value required to beat the game. Determined by user input.
	private int winningValue;	
	// Is the game in progress or has the user won/lost
	private GameStatus gameStatus = GameStatus.IN_PROGRESS;
	// Stack used in the undo operation
	private Stack<ArrayList<Cell>> boardHistory = new Stack<ArrayList<Cell>>();
	// Copy of the gameBoard for comparison.
	private int[][] board; 
	
	
	/**
	 * 
	 * @param height User defined height of the board.
	 * @param width User defined width of the board.
	 * @param winningValue User defined value necessary to win.
	 * 
	 * Checks to see if the height and width are greater than 0, and
	 * checks to see if the winning value is a power of two.
	 * 
	 * @see checkWinningValue()
	 * @see checkWidthHeight()
	 */
	private void checkBoard( int height, int width, int winningValue ) {
		try {
			this.checkWinningValue( winningValue );
			this.checkWidthHeight( height, width );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param winningValue User defined value necessary to win.
	 * @throws Exception IllegalArgumentException()
	 * 
	 * Checks if the winning value is a power of two by using a logarithm
	 * identity, change of base.
	 */
	private void checkWinningValue( int winningValue ) throws Exception {
		if ( winningValue <= 0 ) {
			System.out.println("The winning value is below or equal to 0.");
			throw new IllegalArgumentException();
		}
		// Log base 2 of the value, and checks if there is a remainder.
		double d = Math.log10( (double)winningValue ) / Math.log10( 2 );
		if ( ( d % 1) != 0 ) {
			System.out.println("The winning value must be a power of 2.");
			throw new IllegalArgumentException();
		}
		
	}
	
	/**
	 * 
	 * @param height User defined height of the board.
	 * @param width User defined width of the board.
	 * @throws Exception IllegalArgumentException()
	 * 
	 * Uses a simple conditional to determine if either of the values are 0 or
	 * less, and throws an exception if necessary.
	 */
	private void checkWidthHeight( int height, int width ) throws Exception {
		if ( height <= 0 || width <= 0 ) {
			System.out.println("The board's size is invalid. Use a number "
					+ "greater than 0.");
			throw new IllegalArgumentException();
		}
			
	}
	
	
	/**
     * Reset the game logic to handle a board of a given dimension
     *
     * @param height the number of rows in the board
     * @param width the number of columns in the board
     * @param winningValue the value that must appear on the board to
     *                     win the game
     * @throws IllegalArgumentException when the winning value is not power of 
     * two or negative
     */
	@Override
	public void resizeBoard(int height, int width, int winningValue) {	
		// Calls appropriate helper methods, then assigns values.
		checkBoard( height, width, winningValue );
		this.winningValue = winningValue;
		this.gameBoard = new int[height][width];

	}

	/**
     * Remove all numbered tiles from the board and place
     * TWO non-zero values at random location
     */
	@Override
	public void reset() {
		// Sets elements with gameBoard to 0.
		for (int row = 0; row < gameBoard.length; row++ ) {
			for (int col = 0; col < gameBoard[0].length; col++ ) {
				gameBoard[row][col] = 0;
				
			}
		}
		// Creates two values of 1, and places them randomly.
		Random rand = new Random();
		int row = rand.nextInt(gameBoard.length );
		int col = rand.nextInt(gameBoard[0].length);
		gameBoard[row][col] = 1;
		
		row = rand.nextInt(gameBoard.length);
		col = rand.nextInt(gameBoard[0].length);
		gameBoard[row][col] = 1;

	}

	/**
     * Set the game board to the desired values given in the 2D array.
     * This method should use nested loops to copy each element from the
     * provided array to your own internal array. Do not just assign the
     * entire array object to your internal array object. Otherwise, your
     * internal array may get corrupted by the array used in the JUnit
     * test file. This method is mainly used by the JUnit tester.
     * @param ref The gameBoard that is being referenced.
     */
	@Override
	public void setValues(int[][] ref) {
		// Resizes board
		this.resizeBoard(ref.length, ref[0].length, this.winningValue);
		// Places the values.
		for ( int row = 0; row < gameBoard.length; row++ ) {
			for ( int col = 0; col < gameBoard[0].length; col++ ) {
				gameBoard[row][col] = ref[row][col];
				
			}
		}
	}
	
	/**
	 * Fills the board with zeros, and then places a Cell
	 * in the corresponding location.
	 * @param cList This list comes from the undo operation.
	 * @see Undo
	 */
	private void setValues(ArrayList<Cell> cList) {
		// Sets 0s
		for ( int row = 0; row < gameBoard.length; row++ ) {
			for ( int col = 0; col < gameBoard[0].length; col++ ) {
				gameBoard[row][col] = 0;
			}
		}
		// Places cells.
		for(Cell c: cList) {
			gameBoard[c.row][c.column] = c.value;
		}
		
	}
	/**
	 * Copies the current gameBoard to a dummy board. The dummy board
	 * is used in the compareBoard method.
	 * @return A 2-d array of integers.
	 */
	private int[][] copyBoard() {
		// creates new board
		int [][] board = new int[gameBoard.length][gameBoard[0].length];
		// transcribes values.
		for ( int row = 0; row < gameBoard.length; row++ ) {
			for ( int col = 0; col < gameBoard[0].length; col++ ) {
				board[row][col] = gameBoard[row][col];
			}
		}
		return board;
	}
	
	/**
	 * Compares a dummy board to the current gameBoard
	 * @param board A copy of the gameBoard prior to sliding.
	 * @return True if and only if the gameBoard changes after sliding.
	 */
	private boolean compareBoard(int[][] board) {
		for ( int row = 0; row < gameBoard.length; row++ ) {
			for ( int col = 0; col < gameBoard[0].length; col++ ) {
				if (board[row][col] != gameBoard[row][col]) return true;
			}
		}
		return false;
	}
	
	/**
	 * @param row Row to Check
	 * @param col Column to check
	 * @return False when position is equal to zero, and true for anything else
	 */
	private boolean checkPosition(int row, int col) {
		if (gameBoard[row][col] == 0) return false;
		return true;
	}
	
	/**
	 * 
	 * @throws Exception IllegealStateException()
	 * Checks to see if the board is full by using nested for loops.
	 * If a free spot is detected, then the method immediately returns.
	 * Otherwise, an exception is thrown.
	 */
	private boolean isBoardFull() {
		
		for (int row = 0; row < gameBoard.length; row++ ) {
			for (int col = 0; col < gameBoard[0].length; col++ ) {
				// Returns when a free space is detected.
				if ( gameBoard[row][col] == 0) return false;
			}
		}
		
		return true;
	}
	
	/**
     * Insert one random tile into an empty spot on the board.
     *
     * @return a Cell object with its row, column, and value attributes
     *  initialized properly
     *
     * @throws IllegalStateException when the board has no empty cell
     * @see isBoardFull()
     * @see checkPosition()
     */
	@Override
	public Cell placeRandomValue() {
		int row;
		int col;
		
		if(!this.isBoardFull()) {
			Random rand = new Random();
			
			do {
				row = rand.nextInt(gameBoard.length);
				col = rand.nextInt(gameBoard[0].length);
			} while ( checkPosition( row, col ));
			
			gameBoard[row][col] = 1;
			return new Cell( row, col, 1 );
		} else {
			return null;
		}
	}	
	/**
	 * Begins in top left corner, loops through board, and moves
	 * necessary tiles while preserving the original values.
	 * @param dir Direction the tiles are sliding in.
	 * @param b Used to prevent infinite loop.
	 * @return True when board changes. False when no change occurs.
	 */
	private boolean slideUp(SlideDirection dir, boolean b) {
		for ( int col = 0; col < gameBoard[0].length; col++ ) {
			int newRow = 0;
			for ( int row = 0; row < gameBoard.length; row++ ) {
					
				/*If a non-zero value is detected, then move the value to the 
				 * assumed first open location. Change the new position.*/
				if (gameBoard[row][col] != 0) {
					gameBoard[newRow][col] = gameBoard[row][col];
					if (newRow != row) {
						gameBoard[row][col] = 0;
					}
					newRow++;
				}
			}
		}
		// Prevents an infinite loop, but allows for the combining of tiles,
		// a following slide, and the placement of a new value.
		if (b) {
			combine(dir);
			determineStatus();
		}
		if (b && compareBoard(board)){
			placeRandomValue();
			determineStatus();
			return true;
		}
		return false;
	}
	/**
	 * Begins in bottom right corner, loops through board, and moves
	 * necessary tiles while preserving the original values.
	 * @param dir Direction the tiles are sliding in.
	 * @param b Used to prevent infinite loop.
	 * @return True when board changes. False when no change occurs.
	 */
	private boolean slideDown(SlideDirection dir, boolean b) {
		for ( int col = gameBoard[0].length - 1; col >= 0; col-- ) {
			
			int newRow = gameBoard.length - 1;

			for ( int row = gameBoard.length - 1; row >= 0; row-- ) {
				/*If a non-zero value is detected, then move the value to the 
				 * assumed first open location. Change the new position.*/
				if (gameBoard[row][col] >= 1) {
					gameBoard[newRow][col] = gameBoard[row][col];
					if (newRow != row) {
						gameBoard[row][col] = 0;
					}
					newRow--;
				}
			}
		}
		if (b) {
			combine(dir);
			determineStatus();
		}
		if (b && compareBoard(board)){
			placeRandomValue();
			determineStatus();
			return true;
		}
		return false;
	}

	/**
	 * Begins in top left corner, loops through board, and moves
	 * necessary tiles while preserving the original values.
	 * @param dir Direction the tiles are sliding in.
	 * @param b Used to prevent infinite loop.
	 * @return True when board changes. False when no change occurs.
	 */
	private boolean slideLeft(SlideDirection dir, boolean b) {
		for ( int row = 0; row < gameBoard.length; row++ ) {

			int newCol = 0;

			for ( int col = 0; col < gameBoard[0].length; col++ ) {
					
				/*If a non-zero value is detected, then move the value to the 
				 * assumed first open location. Change the new position.*/
				if (gameBoard[row][col] != 0) {
					gameBoard[row][newCol] = gameBoard[row][col];
					if (newCol != col) {
						gameBoard[row][col] = 0;
					}
					newCol++;
				}
			}
		}
		if (b) {
			combine(dir);
			determineStatus();
		}	
		if (b && compareBoard(board)){
			placeRandomValue();
			determineStatus();
			return true;
		}
		return false;
	}
	
	/**
	 * Begins in top right corner, loops through board, and moves
	 * necessary tiles while preserving the original values.
	 * @param dir Direction the tiles are sliding in.
	 * @param b Used to prevent infinite loop.
	 * @return True when board changes. False when no change occurs.
	 */
	private boolean slideRight(SlideDirection dir, boolean b) {
		for ( int row = 0; row < gameBoard.length; row++ ) {

			int newCol = gameBoard[0].length -1;

			for ( int col = gameBoard[0].length - 1; col >= 0; col-- ) {
					
				/*If a non-zero value is detected, then move the value to the 
				 * assumed first open location. Change the new position.*/
				if (gameBoard[row][col] != 0) {
					gameBoard[row][newCol] = gameBoard[row][col];
					if (newCol != col) {
						gameBoard[row][col] = 0;
					}
					newCol--;
					
				}
			}
		}
		if (b) {
			combine(dir);
			determineStatus();
		}
		if (b && compareBoard(board)){
			placeRandomValue();
			determineStatus();
			return true;
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param dir Direction in which the tiles are sliding. 
	 * 
	 * Combines two values if they are equal, and sets the appropriate value
	 * to zero.
	 * 
	 * The combination starts from the "dir" side (e.g. dir = RIGHT, thus we 
	 * start on the right side), and work backwards (right to left, up to
	 * down, etc.). 
	 */
	private void combine ( SlideDirection dir ) {
		if ( dir == SlideDirection.UP ) {
			
			for ( int col = 0; col < gameBoard[0].length; col++ ) {
				for ( int row = 0; row < gameBoard.length - 1; row++ ) {
					if ( gameBoard[row][col] == gameBoard[row + 1][col] ){
						gameBoard[row][col] *= 2;
						gameBoard[row + 1][col] = 0;
					}
				}
			}
			slideUp(dir, false);
			
		} else if ( dir == SlideDirection.DOWN ) {
			
			for ( int col = gameBoard[0].length -1; col >= 0; col-- ) {
				for ( int row = gameBoard.length - 1; row >= 1; row -- ) {
					if ( gameBoard[row][col] == gameBoard[row - 1][col] && (row - 1 >= 0)){
						gameBoard[row][col] *= 2;
						gameBoard[row - 1][col] = 0;
					}
				}
			}
			slideDown(dir, false);
			
		} else if ( dir == SlideDirection.LEFT ) {
			
			for ( int row = 0; row < gameBoard.length; row++ ) {
				for ( int col = 0; col < gameBoard[0].length - 1; col++ ) {
					if ( gameBoard[row][col] == gameBoard[row][col + 1] ){
						gameBoard[row][col] *= 2;
						gameBoard[row][col + 1] = 0;
					}
				}
			}
			slideLeft(dir, false);
			
		} else {
			
			for ( int row = 0; row < gameBoard.length; row++ ) {
				for ( int col = gameBoard[0].length - 1; col >= 1; col-- ) {
					if ( gameBoard[row][col] == gameBoard[row][col - 1] ){
						gameBoard[row][col] *= 2;
						gameBoard[row][col - 1] = 0;
					}
				}
			}
			slideRight(dir, false);
			
		}
	}
	/**
	 * Checks adjacent tiles for a like value.
	 * @return True if a like tile is found (game is still in progress). 
	 * False when the game is over.
	 */
	private boolean combinationsAvailable() {
		// Loops through board
		for ( int row = 0; row < gameBoard.length; row++ ) {
			for ( int col = 0; col < gameBoard[0].length; col++ ) {
				// First tier of conditionals check boundaries.
				if ( row + 1 < gameBoard.length ) {
					// Nested conditional check for a like tile.
					if( gameBoard[row][col] == gameBoard[row + 1][col] ) 
						return true;
				}
				
				if ( row - 1 >= 0) {
					if( gameBoard[row][col] == gameBoard[row - 1][col] ) 
						return true;
				}
				
				if ( col + 1 < gameBoard.length ) {
					if ( gameBoard[row][col] == gameBoard[row][col + 1] ) 
						return true;
				}

				if ( col - 1 >= 0 ) {
					if (gameBoard[row][col] == gameBoard[row][col - 1] ) 
						return true;
				}
				
			}
		}
		return false;
	}
	
	/**
	 * Saves gameState for the undo operation.
	 * Prepares a dummy board for comparison.
	 * Calls appropriate slide method.
	 */
	@Override
	public boolean slide (SlideDirection dir) {
		boardHistory.push(getNonEmptyTiles());
		board =	copyBoard();
		switch (dir) {
		case UP:			
			return slideUp(dir, true);
		case DOWN:
			return slideDown(dir, true);
		case LEFT:
			return slideLeft(dir, true);
		case RIGHT:
			return slideRight(dir, true);
		}
		
		return false;

	}

	/**@return An ArrayList of Cells. Each cell holds the (row,column) and
    * value of a tile.*/
	@Override
	public ArrayList<Cell> getNonEmptyTiles() {
		
		ArrayList<Cell> cellList = new ArrayList<Cell>();
		
		for ( int row = 0; row < gameBoard.length; row++ ) {
			for ( int col = 0; col < gameBoard[0].length; col++ ) {
				
				if ( checkPosition( row, col ) ) 
					cellList.add(new Cell( row, col, gameBoard[row][col ] ) );	
			}
		}
		
		return cellList;
	}

	/**
	 * Changes the field, gameStatus.
	 */
	private void determineStatus() {
		// The game is always in progress unless the user lost/won.
		gameStatus = GameStatus.IN_PROGRESS;
		
		// Searches the board for the winning value.
		for ( int row = 0; row < gameBoard.length; row++ ) {
			for ( int col = 0; col < gameBoard[0].length; col++ ) {
				if ( gameBoard[row][col] == winningValue) 
					gameStatus = GameStatus.USER_WON;
			}
		}
		// The player loses when the board is full and no combos are available.
		if(isBoardFull() && !combinationsAvailable()) {
			gameStatus = GameStatus.USER_LOST;
		}
	}
	
	/**
	 * This method is used by the tester. determineStatus() is called to 
	 * ensure appropriate tests are passed.
	 */
	@Override
	public GameStatus getStatus() {
		determineStatus();
		return gameStatus;
	}

	/**
     * Undo the most recent action, i.e. Makes use of a stack to store state,
     * and ArrayLists to populate the board.
     */
	@Override
	public void undo() {
		if(boardHistory.empty()) {
			System.out.println("Cannot undo anymore!");
		} else {
			setValues(boardHistory.pop());
		}
	}
	
	
}
