package game.tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import game.Action;
import game.State;

public class TicTacToeGameState implements State {
	
	private final int X = 1;
	private final int O = -1;

	private int[][] board;
	private int board_size;
	private int next_to_move;

	public TicTacToeGameState(int next_to_move) {
		this( new int[3][3],next_to_move);
	}

	public TicTacToeGameState(int[][] board, int next_to_move) {
		super();
		
		this.next_to_move = next_to_move;
		this.board = board;
		this.board_size = this.board.length;
	}

	@Override
	public List<Action> get_legal_actions() {
		
		List<Couple> indices = indices();
		List<Action> actions = new ArrayList<Action>();
		
		for (Couple c : indices) {
			actions.add(new TicTacToeMove(c.x, c.y, next_to_move));
		}
		

		return actions;
	}

	@Override
	public int next_to_move() {
		return this.next_to_move;
	}

	@Override
	public State move(Action action) throws Exception {
		TicTacToeMove move = (TicTacToeMove) action;
		if (!is_move_legal(move)) {
			throw new Exception(String.format("move %s on board %d is not legal", move, this.board_size));
		}

		int[][] new_board = copy();
		new_board[move.getX_coordinate()][move.getY_coordinate()] = move.getValue();

		int _next_to_move = this.next_to_move == X ? O : X;

		return new TicTacToeGameState(new_board, _next_to_move);
	}

	@Override
	public boolean is_game_over() {
		return game_result() != null;
	}

	@Override
	public Integer game_result() {
		
		
	

		List<Integer> rowsum = Arrays.asList(board).parallelStream().map(t -> Arrays.stream(t).sum()).collect(Collectors.toList());
		List<Integer> colsum = Arrays.asList(transpose().apply(board)).parallelStream().map(t -> Arrays.stream(t).sum())
				.collect(Collectors.toList());
		int diag_sum_tl = sumDiag_1(board);
		int diag_sum_tr = sumDiag_2(board);

		boolean player_one_wins = rowsum.stream().anyMatch(e -> e == board.length);
		player_one_wins |= colsum.stream().anyMatch(e -> e == board.length);
		player_one_wins |= diag_sum_tl == board.length;
		player_one_wins |= diag_sum_tr == board.length;

		if (player_one_wins) {
			return X;
		}

		boolean player_two_wins = rowsum.stream().anyMatch(e -> e == -board.length);
		player_two_wins |= colsum.stream().anyMatch(e -> e == -board.length);
		player_two_wins |= diag_sum_tl == -board.length;
		player_two_wins |= diag_sum_tr == -board.length;

		if (player_two_wins) {
			return O;
		}
		if (Arrays.stream(board).allMatch(t -> Arrays.stream(t).allMatch(e -> e != 0))) {
			return 0;
		}
		return null;
	}

	private boolean is_move_legal(TicTacToeMove move) {

		if (move.getValue() != this.next_to_move) {
			return false;
		}

		boolean x_in_range = (0 <= move.getX_coordinate() && move.getX_coordinate() < this.board_size);
		if (!x_in_range) {
			return false;
		}

		boolean y_in_range = (0 <= move.getY_coordinate() && move.getY_coordinate() < this.board_size);

		if (!y_in_range) {
			return false;
		}

		return this.board[move.getX_coordinate()][move.getY_coordinate()] == 0;

	}
	
	public int[][] getBoard() {
		return board;
	}

	private UnaryOperator<int[][]> transpose() {
		
		return m -> {
			return IntStream.range(0, m[0].length).parallel()
					.mapToObj(r -> IntStream.range(0, m.length).parallel().map(c -> m[c][r]).toArray()).toArray(int[][]::new);
		};
	}

	private int[][] copy() {
		int[][] A2 = board.clone();
		for (int i = 0; i < A2.length; i++) {
			A2[i] = A2[i].clone();
		}
		return A2;
	}

	private int sumDiag_1(int[][] tab) {
		int sum = 0;
		for (int i = 0; i < tab.length; i++) {
			sum += tab[i][i];
		}
		return sum;
	}

	private int sumDiag_2(int[][] tab) {
		int sum = 0;
		for (int i = 0; i < tab.length; i++) {
			sum += tab[i][tab.length - i - 1];
		}
		return sum;
	}

	private List<Couple> indices() {
		List<Couple> res = new ArrayList<TicTacToeGameState.Couple>();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == 0) {
					res.add(new Couple(i, j));
				}
			}
		}
		return res;

	}

	private class Couple {
		public int x, y;

		public Couple(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
	}

	@Override
	public String toString() {
		String str = "";
		
		for (int i = 0; i < board.length; i++) {
			str += "|";
			for (int j = 0; j < board.length; j++) {
				str += " "+ this.board[i][j] + " ";
			}
			str += "|\n";
		}

		return str;
	}
}
