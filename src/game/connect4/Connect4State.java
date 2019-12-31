package game.connect4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import game.Action;
import game.State;

public class Connect4State implements State {

	private final int X = 1;
	private final int O = -1;

	private final int w = 7;
	private final int l = 6;

	private List<List<Box>> board;
	private int next_to_move;

	public Connect4State(int next_to_move) {
		super();
		board = new ArrayList<List<Box>>();
		for (int i = 0; i < l; i++) {
			List<Box> column = new ArrayList<Box>();
			for (int j = 0; j < w; j++) {
				column.add(new Box(i, j, 0));
			}
			board.add(column);
		}
		this.next_to_move = next_to_move;
	}

	public int get_x_availble(int y) {
			try {
				return IntStream.range(0, 7)
						.mapToObj(i -> board.stream().map(line -> line.get(i)).collect(Collectors.toList()))
						.collect(Collectors.toList()).get(y).stream().filter(e -> e.getValue() == 0).map(e -> e.getX()).max(Integer::compareTo).get();							
			} catch (Exception e2) {
				return -1;
			}
		
		 
	}

	private Connect4State(List<List<Box>> board, int next_to_move) {
		this.board = board;
		this.next_to_move = next_to_move;
	}

	@Override
	public List<Action> get_legal_actions() {

		List<Integer> actions = new ArrayList<Integer>();
		for (List<Box> list : board) {
			List<Integer> a = list.stream().filter(e -> e.getValue() == 0).map(e -> e.getY())
					.collect(Collectors.toList());
			a.stream().forEach(e -> actions.add(e));
		}

		List<Action> a = actions.stream().distinct().map(e -> new Connect4Action(e,this.next_to_move)).collect(Collectors.toList());
		return a;
	}

	@Override
	public int next_to_move() {
		return this.next_to_move;
	}

	@Override
	public State move(Action action) throws Exception {
		Connect4Action action_ = (Connect4Action) action;

		List<List<Box>> board_copy = board.stream()
				.map(l -> l.stream().map(b -> (Box) b.clone()).collect(Collectors.toList()))
				.collect(Collectors.toList());

		int index = action_.getIndex();

		IntStream.range(0, board_copy.size()).parallel().map(i -> board_copy.size() - i + 0 - 1).parallel()
				.mapToObj(e -> board_copy.get(e)).parallel().filter(line -> line.get(index).getValue() == 0).limit(1)
				.collect(Collectors.toList()).get(0).get(index).setValue(next_to_move);

		return new Connect4State(board_copy, this.next_to_move == X ? O : X);
	}

	private boolean checkComboInLines(int player) {

		List<List<List<Box>>> x_player_combo = board.stream()
				.map(line -> IntStream.range(0, 4).mapToObj(i -> line.subList(i, i + 4))
						.filter(e -> e.stream().allMatch(x -> x.getValue() == player)).collect(Collectors.toList()))
				.collect(Collectors.toList());

		return x_player_combo.stream().mapToInt(List::size).sum() != 0;
	}

	private boolean checkComboInColumns(int player) {

		List<List<List<Box>>> o_player_combo = IntStream.range(0, 7)
				.mapToObj(i -> board.stream().map(line -> line.get(i)).collect(Collectors.toList()))
				.map(line -> IntStream.range(0, 3).mapToObj(i -> line.subList(i, i + 4))
						.filter(e -> e.stream().allMatch(x -> x.getValue() == player)).collect(Collectors.toList()))
				.collect(Collectors.toList());

		return o_player_combo.stream().mapToInt(List::size).sum() != 0;
	}

	private boolean checkInDiagonals(int player) {
		int[][] bo = new int[l][w];
		for (int i = 0; i < l; i++) {
			for (int j = 0; j < w; j++) {
				bo[i][j] = board.get(i).get(j).getValue();
			}
		}

		for (int i = 3; i < l; i++) {
			for (int j = 0; j < w - 3; j++) {
				if (bo[i][j] == player && bo[i - 1][j + 1] == player && bo[i - 2][j + 2] == player
						&& bo[i - 3][j + 3] == player)
					return true;
			}
		}

		for (int i = 3; i < l; i++) {
			for (int j = 3; j < w; j++) {
				if (bo[i][j] == player && bo[i - 1][j - 1] == player && bo[i - 2][j - 2] == player
						&& bo[i - 3][j - 3] == player)
					return true;
			}
		}
		return false;

	}

	@Override
	public boolean is_game_over() {
		boolean result = false;

		for (int player : Arrays.asList(X, O)) {
			result |= checkInDiagonals(player) | checkComboInColumns(player) | checkComboInLines(player);
		}
		return result | !board.stream().anyMatch(line -> line.stream().anyMatch(e -> e.getValue() == 0));
	}

	@Override
	public Integer game_result() {

		boolean player_one_wins = checkInDiagonals(X);
		player_one_wins |= checkComboInColumns(X);
		player_one_wins |= checkComboInLines(X);

		if (player_one_wins) {
			return X;
		}

		boolean player_second_wins = checkInDiagonals(O);
		player_one_wins |= checkComboInColumns(O);
		player_one_wins |= checkComboInLines(O);

		if (player_second_wins) {
			return O;
		}
		return null;
	}

	@Override
	public String toString() {
		String str = "";
		for (List<Box> list : board) {
			str += "| ";
			for (Box box : list) {
				str += box.getValue() + " ";
			}
			str += "|\n";
		}
		return str;
	}

}
