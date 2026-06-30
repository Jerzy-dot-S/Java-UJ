import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaTetris implements Tetris {

	private int rows;
	private int cols;
	private boolean[][] grid;

	@Override
	public void rows(int rows) {
		this.rows = rows;
		resetGrid();
	}

	@Override
	public void cols(int cols) {
		this.cols = cols;
		resetGrid();
	}

	@Override
	public void drop(Block block) {
		ensureReady();
		Shape shape = buildShape(block);
		int baseCol = block.base().col();
		validateWidth(baseCol, shape);
		int baseRow = landingRow(baseCol, shape, grid);
		checkTopBound(baseRow, shape);
		applyAndClear(shape, baseCol, baseRow, grid);
	}

	@Override
	public void optimalDrop(Block block) {
		ensureReady();
		Shape shape = buildShape(block);
		int minCol = -shape.minColOffset;
		int maxCol = cols - 1 - shape.maxColOffset;
		boolean[][] bestBoard = null;
		int bestLeftCol = -1;
		int bestHeight = Integer.MAX_VALUE;
		for (int baseCol = minCol; baseCol <= maxCol; baseCol++) {
			int baseRow = landingRow(baseCol, shape, grid);
			if (exceedsTop(baseRow, shape)) {
				continue;
			}
			PlacementResult result = simulatePlacement(shape, baseCol, baseRow, grid);
			if (result == null) {
				continue;
			}
			int leftmost = result.leftmostColumn;
			if (result.highestBlockRow < bestHeight || (result.highestBlockRow == bestHeight && leftmost < bestLeftCol)) {
				bestHeight = result.highestBlockRow;
				bestLeftCol = leftmost;
				bestBoard = result.board;
			}
		}
		if (bestBoard != null) {
			grid = bestBoard;
		}
	}

	@Override
	public List<Integer> state() {
		ensureReady();
		int[] heights = columnHeights(grid);
		List<Integer> result = new ArrayList<>(cols);
		for (int h : heights) {
			result.add(h);
		}
		return result;
	}

	private void resetGrid() {
		if (rows > 0 && cols > 0) {
			grid = new boolean[rows][cols];
		}
	}

	private void ensureReady() {
		if (rows <= 0 || cols <= 0 || grid == null) {
			throw new IllegalStateException("Board dimensions not set");
		}
	}

	private Shape buildShape(Block block) {
		Shape shape = new Shape();
		shape.add(0, 0);
		for (Vector v : block.squares()) {
			shape.add(v.dCol(), v.dRow());
		}
		return shape;
	}

	private void validateWidth(int baseCol, Shape shape) {
		int left = baseCol + shape.minColOffset;
		int right = baseCol + shape.maxColOffset;
		if (left < 0 || right >= cols) {
			throw new IllegalArgumentException("Block does not fit horizontally");
		}
	}

	private int landingRow(int baseCol, Shape shape, boolean[][] board) {
		int[] heights = columnHeights(board);
		int row = 1 - shape.minRowOffset;
		for (Map.Entry<Integer, Integer> entry : shape.bottomByColumn.entrySet()) {
			int col = baseCol + entry.getKey();
			int needed = heights[col] + 1 - entry.getValue();
			if (needed > row) {
				row = needed;
			}
		}
		return row;
	}

	private boolean exceedsTop(int baseRow, Shape shape) {
		return baseRow + shape.maxRowOffset > rows;
	}

	private void checkTopBound(int baseRow, Shape shape) {
		if (exceedsTop(baseRow, shape)) {
			throw new IllegalArgumentException("Block exceeds board height");
		}
	}

	private void applyAndClear(Shape shape, int baseCol, int baseRow, boolean[][] board) {
		applyAndClear(shape, baseCol, baseRow, board, null);
	}

	private void applyAndClear(Shape shape, int baseCol, int baseRow, boolean[][] board, boolean[][] marker) {
		for (Offset offset : shape.offsets) {
			int col = baseCol + offset.dCol;
			int row = baseRow + offset.dRow;
			board[row - 1][col] = true;
			if (marker != null) {
				marker[row - 1][col] = true;
			}
		}
		clearFullRows(board, marker);
	}

	private void clearFullRows(boolean[][] board, boolean[][] marker) {
		for (int r = 0; r < rows; r++) {
			boolean full = true;
			for (int c = 0; c < cols; c++) {
				if (!board[r][c]) {
					full = false;
					break;
				}
			}
			if (full) {
				for (int shift = r; shift < rows - 1; shift++) {
					board[shift] = Arrays.copyOf(board[shift + 1], cols);
					if (marker != null) {
						marker[shift] = Arrays.copyOf(marker[shift + 1], cols);
					}
				}
				board[rows - 1] = new boolean[cols];
				if (marker != null) {
					marker[rows - 1] = new boolean[cols];
				}
				r--;
			}
		}
	}

	private int[] columnHeights(boolean[][] board) {
		int[] heights = new int[cols];
		for (int c = 0; c < cols; c++) {
			for (int r = rows - 1; r >= 0; r--) {
				if (board[r][c]) {
					heights[c] = r + 1;
					break;
				}
			}
		}
		return heights;
	}

	private boolean[][] copyGrid(boolean[][] source) {
		boolean[][] copy = new boolean[rows][cols];
		for (int r = 0; r < rows; r++) {
			copy[r] = Arrays.copyOf(source[r], cols);
		}
		return copy;
	}

	private PlacementResult simulatePlacement(Shape shape, int baseCol, int baseRow, boolean[][] source) {
		boolean[][] board = copyGrid(source);
		boolean[][] marker = new boolean[rows][cols];
		for (Offset offset : shape.offsets) {
			int col = baseCol + offset.dCol;
			int row = baseRow + offset.dRow;
			if (board[row - 1][col]) {
				return null; // overlap
			}
			board[row - 1][col] = true;
			marker[row - 1][col] = true;
		}
		clearFullRows(board, marker);
		int highest = 0;
		int leftmost = Integer.MAX_VALUE;
		for (int r = rows - 1; r >= 0; r--) {
			for (int c = 0; c < cols; c++) {
				if (marker[r][c]) {
					highest = Math.max(highest, r + 1);
					leftmost = Math.min(leftmost, c);
				}
			}
		}
		if (leftmost == Integer.MAX_VALUE) {
			leftmost = -1;
		}
		return new PlacementResult(board, highest, leftmost);
	}

	private static final class Offset {
		final int dCol;
		final int dRow;

		Offset(int dCol, int dRow) {
			this.dCol = dCol;
			this.dRow = dRow;
		}
	}

	private static final class Shape {
		final List<Offset> offsets = new ArrayList<>();
		final Map<Integer, Integer> bottomByColumn = new HashMap<>();
		int minColOffset = 0;
		int maxColOffset = 0;
		int minRowOffset = 0;
		int maxRowOffset = 0;

		void add(int dCol, int dRow) {
			offsets.add(new Offset(dCol, dRow));
			minColOffset = Math.min(minColOffset, dCol);
			maxColOffset = Math.max(maxColOffset, dCol);
			minRowOffset = Math.min(minRowOffset, dRow);
			maxRowOffset = Math.max(maxRowOffset, dRow);
			bottomByColumn.merge(dCol, dRow, Math::min);
		}
	}

	private static final class PlacementResult {
		final boolean[][] board;
		final int highestBlockRow;
		final int leftmostColumn;

		PlacementResult(boolean[][] board, int highestBlockRow, int leftmostColumn) {
			this.board = board;
			this.highestBlockRow = highestBlockRow;
			this.leftmostColumn = leftmostColumn;
		}
	}
}
