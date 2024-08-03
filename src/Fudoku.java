import solver.FudokuSolver;
import solver.SudokuSolver;

public class Fudoku {
	public static void main(String[] args) {
		SudokuSolver sudokuSolver = new FudokuSolver();

		int[][] m = {
			{0, 0, 5, 0, 2, 4, 0, 1, 3},
			{0, 0, 6, 0, 3, 1, 0, 0, 0},
			{0, 0, 1, 0, 8, 9, 5, 0, 7},
			{1, 6, 0, 0, 9, 7, 0, 0, 5},
			{7, 5, 8, 3, 0, 0, 0, 9, 0},
			{0, 0, 9, 8, 0, 5, 0, 0, 0},
			{5, 0, 7, 0, 6, 0, 3, 2, 4},
			{0, 1, 0, 4, 5, 0, 9, 7, 0},
			{0, 4, 3, 0, 0, 2, 0, 0, 0},
		};

		sudokuSolver.setGrid(m);
		sudokuSolver.solve();
		m = sudokuSolver.getGrid();

		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				System.out.print(m[row][col]);
				if (col == 2 || col == 5)
					System.out.print(" ");
			}
			System.out.println();
			if (row == 2 || row == 5)
				System.out.println();
		}
	}
}
