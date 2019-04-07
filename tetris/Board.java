// Board.java
package tetris;

import java.util.Arrays;

/**
 * CS108 Tetris Board. Represents a Tetris board -- essentially a 2-d grid of
 * booleans. Supports tetris pieces and row clearing. Has an "undo" feature that
 * allows clients to add and remove pieces efficiently. Does not do any drawing
 * or have any idea of pixels. Instead, just represents the abstract 2-d board.
 */
public class Board {
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;

	private int widths[];
	private int heights[];
	int maxHeight = 0;

	private int xWidths[];
	private int xHeights[];
	int xMaxHeight = 0;

	private boolean[][] xGrid;

	// Here a few trivial methods are provided:

	/**
	 * Creates an empty board of the given width and height measured in blocks.
	 */
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		xGrid = new boolean[width][height];
		committed = true;

		widths = new int[height];
		heights = new int[width];
		xWidths = new int[height];
		xHeights = new int[width];

		// initializeBoard();
		Arrays.fill(widths, 0);
		Arrays.fill(heights, 0);
		Arrays.fill(xWidths, 0);
		Arrays.fill(xHeights, 0);
		maxHeight = xMaxHeight = 0;
		
		doBackup();
		// YOUR CODE HERE
	}

	/*
	 * This method initializes empty board In other words, fills board with zeros
	 */
//	private void initializeBoard() {
//		for(int row = 0; row < height; row++) {
//			for(int col = 0; col < width; col++) {
//				grid[col][row] = false;
//				xGrid[col][row] = false;
//			}
//		}
//	}

	/**
	 * Returns the width of the board in blocks.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the board in blocks.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the max column height present in the board. For an empty board this
	 * is 0.
	 */
	public int getMaxHeight() {
		return maxHeight; // YOUR CODE HERE
	}

	/**
	 * Checks the board for internal consistency -- used for debugging.
	 */
	public void sanityCheck() {
		if (DEBUG) {
			int[] tmpWidths = new int[height];
			int[] tmpHeights = new int[width];
			int tmpMaxHeight = 0;
			Arrays.fill(tmpWidths, 0);
			Arrays.fill(tmpHeights, 0);
			
			for(int col = 0; col < width; col++) {
				for(int row = 0; row < height; row++) {
					if(grid[col][row]) {
						tmpWidths[row]++;
						tmpHeights[col] = row + 1;
						if(tmpHeights[col] > tmpMaxHeight) {
							tmpMaxHeight = tmpHeights[col];
						}
					}
				}
			}
		
			if(!Arrays.equals(widths, tmpWidths)) { 
				throw new RuntimeException("Problem with widths");
			}
			
			if(!Arrays.equals(heights, tmpHeights)) { 
				throw new RuntimeException("Problem with heights");
			}
			if(tmpMaxHeight != maxHeight) {
				throw new RuntimeException("Problem with maxHeight");
			}
		}
//					System.out.println(c);
//					System.out.println(heights[col]);
//					throw new RuntimeException("Problem with heights");
//				}
//				throw new RuntimeException("Problem with maxHeight");
//			throw new RuntimeException("Problem with widths");
			// It	erate over whole grid
			// Rows
//			for (int row = 0; row < height; row++) {
//				int r = 0;
//				for (int col = 0; col < width; col++) {
//					if (grid[col][row])
//						r++;
//				}
//				if (r != widths[row])
//					throw new RuntimeException("Problem with widths");
//			}
//
//			// Heights
//			int max = 0;
//			for (int col = 0; col < width; col++) {
//				int c = 0;
//				for (int row = 0; row < height; row++) {
//					if (grid[col][row]) {
//						if (row + 1 > c) {
//							c = row + 1;
//						}
//					}
//				}
//				if (c != heights[col]) {
//					System.out.println(c);
//					System.out.println(heights[col]);
//					throw new RuntimeException("Problem with heights");
//				}
//				if (c > max) {
//					max = c;
//				}
//			}
//			System.out.println(max);
//			System.out.println(maxHeight);
//			if (max != maxHeight)
//				throw new RuntimeException("Problem with maxHeight");
//		}
	}

	/**
	 * Given a piece and an x, returns the y value where the piece would come to
	 * rest if it were dropped straight down at that x.
	 * 
	 * <p>
	 * Implementation: use the skirt and the col heights to compute this fast --
	 * O(skirt length).
	 */
	public int dropHeight(Piece piece, int x) {
		int[] currSkirt = piece.getSkirt();
		int max = 0;
		for (int i = 0; i < piece.getWidth(); i++) {
			int currHeight = heights[x + i];
			int ans = currHeight - currSkirt[i]; //????
			if (ans > max) {
				max = ans;
			}
		}
		return max; // YOUR CODE HERE
	}

	/**
	 * Returns the height of the given column -- i.e. the y value of the highest
	 * block + 1. The height is 0 if the column contains no blocks.
	 */
	public int getColumnHeight(int x) {
		return heights[x]; // YOUR CODE HERE
	}

	/**
	 * Returns the number of filled blocks in the given row.
	 */
	public int getRowWidth(int y) {
		return widths[y]; // YOUR CODE HERE
	}

	/**
	 * Returns true if the given block is filled in the board. Blocks outside of the
	 * valid width/height area always return true.
	 */
	public boolean getGrid(int x, int y) {
		return isOutOfBounds(x, y) || grid[x][y]; // YOUR CODE HERE
	}

	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;

	/**
	 * Attempts to add the body of a piece to the board. Copies the piece blocks
	 * into the board grid. Returns PLACE_OK for a regular placement, or
	 * PLACE_ROW_FILLED for a regular placement that causes at least one row to be
	 * filled.
	 * 
	 * <p>
	 * Error cases: A placement may fail in two ways. First, if part of the piece
	 * may falls out of bounds of the board, PLACE_OUT_BOUNDS is returned. Or the
	 * placement may collide with existing blocks in the grid in which case
	 * PLACE_BAD is returned. In both error cases, the board may be left in an
	 * invalid state. The client can use undo(), to recover the valid, pre-place
	 * state.
	 */
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed)
			throw new RuntimeException("place commit problem");
		committed = false;
	
		int result = PLACE_OK;
		doBackup();
		TPoint[] body = piece.getBody();
		
		for(int i = 0; i < body.length; i++) {
			TPoint currPoint = body[i];
			// Error case 1 - Out of bounds
			if (isOutOfBounds(x + currPoint.x, y + currPoint.y)) {
				result = PLACE_OUT_BOUNDS;
				return result;
			}
			
			// Error case 2 - Overlap
			if(grid[x + currPoint.x][y + currPoint.y]) {
				result = PLACE_BAD;
				return result;
			}
			
			widths[y + currPoint.y]++;
			
			if(heights[x + currPoint.x] < y + currPoint.y + 1) {
				heights[x + currPoint.x] = y + currPoint.y + 1;
				if(maxHeight < heights[x + currPoint.x]) {
					maxHeight = heights[x + currPoint.x];
				}
			}
			
			if(widths[y + currPoint.y] == width) {
				result = PLACE_ROW_FILLED;
			}
			grid[x + currPoint.x][y + currPoint.y] = true;
			
		}
////		// Error case 1 - Out of bounds
////		if (isOutOfBounds(x + body., y + piece.getHeight())) {
////			result = PLACE_OUT_BOUNDS;
////			return result;
////		}
//
//		// Error case 2 - Overlap
//		if (doOverlap(body, x, y)) {
//			result = PLACE_BAD;
//			return result;
//		}
//
//		boolean rowIsFilled = updateVariables(piece, x, y);
//		// Check if any row is filled
//		if (rowIsFilled) {
//			result = PLACE_ROW_FILLED;
//		}

		 sanityCheck();
		return result;
	}

	/*
	 * This method updates variables and also checks if any row is filled or not
	 */
	private boolean updateVariables(Piece p, int x, int y) {
		boolean rowFilled = false;
		TPoint[] body = p.getBody();
		for (int i = 0; i < body.length; i++) {
			TPoint currPoint = body[i];
			grid[x + currPoint.x][y + currPoint.y] = true;
			widths[y + currPoint.y]++;
			// if(y + currPoint.y + 1 > heights[x + currPoint.x]) {
			heights[x + currPoint.x] = y + currPoint.y + 1;

			// }

			// Update maxHeight
			if (heights[x + currPoint.x] > maxHeight) {
				maxHeight = heights[x + currPoint.x];
			}

			// Check if row is filled
			if (widths[y + currPoint.y] == width) {
				rowFilled = true;
			}
		}
		return rowFilled;
	}

	/*
	 * This method checks weather current piece overalps already filled spot or not
	 */
	private boolean doOverlap(TPoint[] body, int x, int y) {
		for (int i = 0; i < body.length; i++) {
			TPoint currPoint = body[i];
			if (grid[x + currPoint.x][y + currPoint.y]) {
				return true;
			}
		}
		return false;
	}

	/*
	 * This method checks whether given coordinates is out of bounds or not
	 */
	private boolean isOutOfBounds(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return false;
		return true;
	}

	/**
	 * Deletes rows that are filled all the way across, moving things above down.
	 * Returns the number of rows cleared.
	 */
	public int clearRows() {
		int rowsCleared = 0;
		committed = false;
		int to = 0;
		for (int from = 0; from < height; from++) {
			/*
			 * if row isn't filled copy "from" row to "to" row
			 */
			if (widths[from] != width) {
				// Copy
				for (int i = 0; i < width; i++) {
					grid[i][to] = grid[i][from];
				}
				widths[to] = widths[from];
				to++;

			} else {
				rowsCleared++;
			}
		}

		for (int start = to; to < maxHeight; to++) {
			for (int g = 0; g < width; g++) {
				grid[g][start] = false;
			}
			widths[start] = 0;
		}

		// Heights//Heights
		for (int col = 0; col < width; col++) {
			int c = 0;
			for (int row = 0; row < height; row++) {
				if (grid[col][row]) {
					heights[col] = row + 1;
					break;
				}  else {
					heights[col] = 0;
				}
			}
		}
		
		//calculate maxHeight
		int m = 0;
		for(int col = 0; col < width; col++) {
			if(heights[col] > m) {
				m = heights[col];
			}
		}
		maxHeight -= rowsCleared;
		System.out.println(m);
		System.out.println(maxHeight);
		maxHeight = m;
		
		sanityCheck();
		return rowsCleared;
	}

	/**
	 * Reverts the board to its state before up to one place and one clearRows(); If
	 * the conditions for undo() are not met, such as calling undo() twice in a row,
	 * then the second undo() does nothing. See the overview docs.
	 */
	public void undo() {
		System.out.println(committed);
		if (!committed) {
			int[] widthTemp = widths;
			widths = xWidths;
			xWidths = widthTemp;

			int[] heightTemp = heights;
			heights = xHeights;
			xHeights = heightTemp;

			maxHeight = xMaxHeight;

			boolean[][] tmpGrid = grid;
			grid = xGrid;
			xGrid = tmpGrid;
			//doBackup2();
		}
		committed = true;
	}

	private void doBackup() {
		System.arraycopy(widths, 0, xWidths, 0, height);
		System.arraycopy(heights, 0, xHeights, 0, width);
		xMaxHeight = maxHeight;
		// Copy grid
		for (int col = 0; col < width; col++) {
			System.arraycopy(grid[col], 0, xGrid[col], 0, height);
		}
	}
	
	private void doBackup2() {
		System.arraycopy(xWidths, 0, widths, 0, height);
		System.arraycopy(xHeights, 0, heights, 0, width);
		maxHeight = xMaxHeight;
		// Copy grid
		for (int col = 0; col < width; col++) {
			System.arraycopy(xGrid[col], 0, grid[col], 0, height);
		}
	}

	/**
	 * Puts the board in the committed state.
	 */
	public void commit() {
		committed = true;
	}

	/*
	 * Renders the board state as a big String, suitable for printing. This is the
	 * sort of print-obj-state utility that can help see complex state change over
	 * time. (provided debugging utility)
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height - 1; y >= 0; y--) {
			buff.append('|');
			for (int x = 0; x < width; x++) {
				if (getGrid(x, y))
					buff.append('+');
				else
					buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x = 0; x < width + 2; x++)
			buff.append('-');
		return (buff.toString());
	}
}
