package solver;

public class FudokuSolver implements SudokuSolver {
	int[][] sudoku;
	int[][] choices;
	int[][][] neighbour;

	public FudokuSolver() {
		sudoku = new int[9][9];
		choices = new int[9][9];
		neighbour = new int[9][9][10];
		clearAll();
	}

	@Override
	public boolean solve() {
		Square square = candidate();
		int row = square.row;
		int col = square.col;

		if (row == -1)
			return false;
		if (col == -1)
			return true;

		for (int digit = 1; digit <= 9; digit++) {
			if (neighbour[row][col][digit] > 0)
				continue;

			set(row, col, digit);
			if (solve())
				return true;

			clear(row, col);
		}

		return false;
	}

	private Square candidate() {
		int best_row = 0;
		int best_col = -1;
		int least_choices = 10;

		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				if (sudoku[row][col] == 0 && choices[row][col] == 0)
					return new Square(-1, 0);

				if (sudoku[row][col] != 0)
					continue;

				if (choices[row][col] < least_choices) {
					least_choices = choices[row][col];
					best_row = row;
					best_col = col;
				}
			}
		}

		return new Square(best_row, best_col);
	}

	@Override
	public void set(int row, int col, int digit) {
		if (row < 0 || row > 8 || col < 0 || col > 8 || digit < 0 || digit > 9)
			throw new IllegalArgumentException();
		if (digit == 0 || sudoku[row][col] != 0)
			return;
		sudoku[row][col] = digit;

		for (int i = 0; i < 9; i++) {
			setNeighbour(row, i, digit);
			setNeighbour(i, col, digit);
		}

		row = 3 * (row / 3);
		col = 3 * (col / 3);

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				setNeighbour(row + i, col + j, digit);
	}

	@Override
	public int get(int row, int col) {
		if (row < 0 || row > 8 || col < 0 || col > 8)
			throw new IllegalArgumentException();
		return sudoku[row][col];
	}

	@Override
	public void clear(int row, int col) {
		if (row < 0 || row > 8 || col < 0 || col > 8)
			throw new IllegalArgumentException();
		int digit = get(row, col);
		if (digit == 0)
			return;
		sudoku[row][col] = 0;
		for (int i = 0; i < 9; i++) {
			clearNeighbour(row, i, digit);
			clearNeighbour(i, col, digit);
		}

		row = 3 * (row / 3);
		col = 3 * (col / 3);

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				clearNeighbour(row + i, col + j, digit);
	}

	private void clearNeighbour(int row, int col, int digit) {
		neighbour[row][col][digit] -= 1;

		if (neighbour[row][col][digit] == 0)
			choices[row][col] += 1;
	}

	private void setNeighbour(int row, int col, int digit) {
		neighbour[row][col][digit] += 1;
		
		if (neighbour[row][col][digit] == 1)
			choices[row][col] -= 1;
	}

	@Override
	public void clearAll() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				sudoku[i][j] = 0;
				choices[i][j] = 9;
				for (int k = 0; k < 10; k++)
					neighbour[i][j][k] = 0;
			}
		}
	}

	@Override
	public boolean isValid(int row, int col) {
		if (row < 0 || row > 8 || col < 0 || col > 8)
			throw new IllegalArgumentException();
		int digit = get(row, col);
		if (digit == 0)
			return true;
		for (int i = 0; i < 9; i++) {
			if (i != col && get(row, i) == digit)
				return false;
			if (i != row && get(i, col) == digit)
				return false;
		}

		int r = 3 * (row / 3);
		int c = 3 * (col / 3);

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if ((r + i != row || c + j != col) && get(r + i, c + j) == digit)
					return false;

		return true;
	}

	@Override
	public boolean isAllValid() {
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (!isValid(i, j))
					return false;
		return true;
	}

	@Override
	public void setGrid(int[][] m) {
		if (m.length != 9)
			throw new IllegalArgumentException();
		for (int i = 0; i < 9; i++) {
			if (m[i].length != 9)
				throw new IllegalArgumentException();
			for (int j = 0; j < 9; j++) {
				int digit = m[i][j];
				if (digit < 0 || digit > 9)
					throw new IllegalArgumentException();
				set(i, j, m[i][j]);
			}
		}
	}

	@Override
	public int[][] getGrid() {
		return sudoku;
	}

	private class Square {
		int row, col;

		public Square(int row, int col) {
			this.row = row;
			this.col = col;
		}
	}
}
