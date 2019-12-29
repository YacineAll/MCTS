package game.tictactoe;

import game.Action;

public class TicTacToeMove implements Action {

	private int x_coordinate, y_coordinate;
	private int value;
	
	
	public TicTacToeMove(int x_coordinate, int y_coordinate, int value) {
		super();
		this.x_coordinate = x_coordinate;
		this.y_coordinate = y_coordinate;
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("x:%d y:%d v:%d", this.x_coordinate, this.y_coordinate, this.value);
	}

	public int getValue() {
		return value;
	}

	public int getX_coordinate() {
		return x_coordinate;
	}

	public int getY_coordinate() {
		return y_coordinate;
	}

}
