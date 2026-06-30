import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MazeSolver implements Maze {

	private int rows = -1;
	private int cols = -1;
	private Set<Square> occupied = Set.of();

	private List<Integer> howFar;
	private List<Integer> area;
	private Set<Square> unreachable;
	private boolean computed;

	private static final int[][] STEPS = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

	@Override
	public void rows(int rows) {
		this.rows = rows;
		this.computed = false;
	}

	@Override
	public void cols(int cols) {
		this.cols = cols;
		this.computed = false;
	}

	@Override
	public void occupiedSquare(Set<Square> squares) {
		this.occupied = squares == null ? Set.of() : new HashSet<>(squares);
		this.computed = false;
	}

	@Override
	public List<Integer> howFar() {
		computeIfNeeded();
		return howFar;
	}

	@Override
	public List<Integer> area() {
		computeIfNeeded();
		return area;
	}

	@Override
	public Set<Square> unreachableSquares() {
		computeIfNeeded();
		return unreachable;
	}

	private void computeIfNeeded() {
		if (computed) {
			return;
		}
		if (rows < 0 || cols < 0) {
			throw new IllegalStateException("Board dimensions must be set before calculation");
		}

		howFar = new ArrayList<>(cols);
		area = new ArrayList<>(cols);
		Set<Square> reachable = new HashSet<>();
		Set<Square> blocked = new HashSet<>(occupied);

		for (int c = 0; c < cols; c++) {
			Square start = new Square(c, 0);
			if (!inBounds(start) || blocked.contains(start)) {
				howFar.add(0);
				area.add(0);
				continue;
			}

			FloodMetrics metrics = floodFill(start, reachable, blocked);
			howFar.add(metrics.deepestRow());
			area.add(metrics.exploredCount());
		}

		unreachable = new HashSet<>();
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				Square square = new Square(c, r);
				if (!blocked.contains(square) && !reachable.contains(square)) {
					unreachable.add(square);
				}
			}
		}

		computed = true;
	}

	private FloodMetrics floodFill(Square start, Set<Square> reachable, Set<Square> blocked) {
		Set<Square> visitedLocal = new HashSet<>();
		ArrayDeque<Square> fringe = new ArrayDeque<>();
		fringe.add(start);
		visitedLocal.add(start);

		int deepest = start.row();

		while (!fringe.isEmpty()) {
			Square current = fringe.removeLast();
			reachable.add(current);
			deepest = Math.max(deepest, current.row());

			for (int[] step : STEPS) {
				Square next = new Square(current.col() + step[0], current.row() + step[1]);
				if (inBounds(next) && !blocked.contains(next) && visitedLocal.add(next)) {
					fringe.add(next);
				}
			}
		}

		return new FloodMetrics(deepest, visitedLocal.size());
	}

	private boolean inBounds(Square square) {
		return square.col() >= 0 && square.col() < cols && square.row() >= 0 && square.row() < rows;
	}

	private record FloodMetrics(int deepestRow, int exploredCount) {
	}
}