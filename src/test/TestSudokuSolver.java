package test;

import java.util.Arrays;
import solver.SudokuSolver;
import solver.FudokuSolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TestSudokuSolver {
	private SudokuSolver solver;

	@BeforeEach
	void setUp() {
		solver = new FudokuSolver();
	}

	@AfterEach
	void tearDown() {
		solver = null;
	}

	@Test
	void TestEmptySolve() {
		assertTrue(solver.solve());
	}

	@Test
	void TestSolve() {
		int[][] problem = {
			{ 0, 0, 8, 0, 0, 9, 0, 6, 2 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 5 },
			{ 1, 0, 2, 5, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 2, 1, 0, 0, 9, 0 },
			{ 0, 5, 0, 0, 0, 0, 6, 0, 0 },
			{ 6, 0, 0, 0, 0, 0, 0, 2, 8 },
			{ 4, 1, 0, 6, 0, 8, 0, 0, 0 },
			{ 8, 6, 0, 0, 3, 0, 1, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 4, 0, 0 },
		};

		int[][] solution = {
			{ 5, 4, 8, 1, 7, 9, 3, 6, 2 },
			{ 3, 7, 6, 8, 2, 4, 9, 1, 5 },
			{ 1, 9, 2, 5, 6, 3, 8, 7, 4 },
			{ 7, 8, 4, 2, 1, 6, 5, 9, 3 },
			{ 2, 5, 9, 3, 8, 7, 6, 4, 1 },
			{ 6, 3, 1, 9, 4, 5, 7, 2, 8 },
			{ 4, 1, 5, 6, 9, 8, 2, 3, 7 },
			{ 8, 6, 7, 4, 3, 2, 1, 5, 9 },
			{ 9, 2, 3, 7, 5, 1, 4, 8, 6 },
		};

		solver.setGrid(problem);
		assertTrue(solver.isAllValid());
		assertTrue(solver.solve());
		assertTrue(solver.isAllValid());
		problem = solver.getGrid();

		assertTrue(Arrays.deepEquals(problem, solution));
	}

	@Test
	void TestNoSolution() {
		solver.set(0, 0, 1);
		solver.set(5, 0, 1);
		assertFalse(solver.isValid(0, 0));
		assertFalse(solver.isValid(5, 0));
		assertFalse(solver.isAllValid());
		solver.clearAll();
		solver.set(1, 0, 1);
		solver.set(0, 1, 1);
		assertFalse(solver.isValid(1, 0));
		assertFalse(solver.isValid(0, 1));
		assertFalse(solver.isAllValid());
	}

	@Test
	void TestSetGetClear() {
		solver.set(0, 5, 8);
		assertEquals(solver.get(0, 5), 8);
		solver.clear(0, 5);
		assertEquals(solver.get(0, 5), 0);
	}

	@Test
	void TestClearAll() {
		for (int row = 0; row < 9; row++)
			for (int col = 0; col < 9; col++)
				solver.set(row, col, 5);
		solver.clearAll();
		for (int row = 0; row < 9; row++)
			for (int col = 0; col < 9; col++)
				assertEquals(solver.get(row, col), 0);
	}
}
