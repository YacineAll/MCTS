package game.connect4;

import game.Action;

public class Connect4Action implements Action{
	
	private int index ;
	private int player;

	public Connect4Action(int index,int player) {
		super();
		this.player = player;
		this.index = index;
	}
	
	@Override
	public String toString() {
		return index+"";
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getPlayer() {
		return player;
	}
	

}
