import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import solver.FudokuSolver;
import solver.SudokuSolver;

public class Fudoku {
	public Fudoku() {
		SwingUtilities.invokeLater(() -> createWindow());
	}

	private void createWindow() {
		JFrame frame = new JFrame("Fudoku");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container pane = frame.getContentPane();
		Container sudoku = new Container();
		sudoku.setLayout(new GridLayout(9, 9));

		JTextField[][] square = new JTextField[9][9];
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				Color color = (row / 3 + col / 3) % 2 == 0 ? new Color(100, 100, 255) : new Color(255, 255, 255);

				square[row][col] = new JTextField();
				square[row][col].setPreferredSize(new Dimension(48, 48));
				Font font = square[row][col].getFont();
				square[row][col].setFont(font.deriveFont(font.getStyle(), 32));
				square[row][col].setHorizontalAlignment(JTextField.CENTER);
				((AbstractDocument)square[row][col].getDocument()).setDocumentFilter(new SingleDigitDocumentFilter());
				square[row][col].setBackground(color);
				sudoku.add(square[row][col]);
			}
		}

		JButton solve = new JButton("Solve");
		JButton clear = new JButton("Clear");
		solve.addActionListener((e) -> {
			int[][] m = new int[9][9];
			for (int row = 0; row < 9; row++) {
				for (int col = 0; col < 9; col++) {
					String text = square[row][col].getText();
					if (text.equals(""))
						continue;
					m[row][col] = Integer.parseInt(text);
				}
			}

			SudokuSolver solver = new FudokuSolver();
			solver.setGrid(m);
			if (!solver.isAllValid() || !solver.solve()) {
				JOptionPane.showMessageDialog(null, "No solution found!", "No solution", JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			m = solver.getGrid();
			for (int row = 0; row < 9; row++) {
				for (int col = 0; col < 9; col++) {
					square[row][col].setText(((Integer)m[row][col]).toString());
				}
			}
		});
		clear.addActionListener((e) -> {
			for (int row = 0; row < 9; row++)
				for (int col = 0; col < 9; col++)
					square[row][col].setText("");
		});

		Container buttons = new Container();
		buttons.setLayout(new FlowLayout());
		buttons.add(solve, BorderLayout.WEST);
		buttons.add(clear, BorderLayout.EAST);

		pane.add(sudoku, BorderLayout.NORTH);
		pane.add(buttons, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);
	}
	public static void main(String[] args) {
		new Fudoku();
	}

	private static class SingleDigitDocumentFilter extends DocumentFilter {
		@Override
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			if (isValid(fb, text, length))
				super.replace(fb, offset, length, text, attrs);
		}

		@Override
		public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs) throws BadLocationException {
			if (isValid(fb, text, 0))
				super.insertString(fb, offset, text, attrs);
		}

		private boolean isValid(FilterBypass fb, String text, int length) {
			if (fb.getDocument().getLength() + text.length() - length > 1)
				return false;
			for (int i = 0; i < text.length(); i++) {
				char c = text.charAt(i);
				if (c < '1' || c > '9')
					return false;
			}
			return true;
		}
	}
}
