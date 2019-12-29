package game;

import java.util.List;

public interface State {

	public List<Action> get_legal_actions();

	public int next_to_move();

	public State move(Action action) throws Exception;

	public boolean is_game_over();

	public Integer game_result();

}
