package mcts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import game.Action;
import game.State;

public class TwoPlayersGameMonteCarloTreeSearchNode extends MonteCarloTreeSearchNode {

	private int number_of_visits;
	private Map<Integer, Integer> _results;
	private Stack<Action> _untried_actions;

	public TwoPlayersGameMonteCarloTreeSearchNode(State state, MonteCarloTreeSearchNode parent,Action act) {
		super(state, parent,act);
		this.number_of_visits = 0;
		this._results = new HashMap<Integer, Integer>();

	}

	public Map<Integer, Integer> get_results() {
		return _results;
	}

	@Override
	public List<Action> untried_actions() {
		if (this._untried_actions == null) {
			this._untried_actions = new Stack<Action>();
			this._untried_actions.addAll(this.getState().get_legal_actions());
		}
		return _untried_actions;
	}

	@Override
	public int q() {
		
		
		int key_1 = this.getParent().getState().next_to_move();
		int key_2 = -1*this.getParent().getState().next_to_move();
		

		int wins  = this._results.get(key_1) == null ? 0 : this._results.get(key_1) ;
		int loses = this._results.get(key_2) == null ? 0 : this._results.get(key_2) ;
		
		return wins - loses;
	}

	@Override
	public int n() {
		return number_of_visits;
	}

	@Override
	public MonteCarloTreeSearchNode expand() {
		Action action = this._untried_actions.pop();
		State next_state = null;
		try {
			next_state = this.getState().move(action);
		} catch (Exception e) {
			e.printStackTrace();
		}
		MonteCarloTreeSearchNode child = new TwoPlayersGameMonteCarloTreeSearchNode(next_state, this,action);
		this.add_child(child);

		return child;
	}

	@Override
	public boolean is_terminal_node() {
		// TODO Auto-generated method stub
		return this.getState().is_game_over();
	}

	@Override
	public int rollout() {
		State current_rollout_state = this.getState();
		while (!current_rollout_state.is_game_over()) {
			List<Action> possible_moves = current_rollout_state.get_legal_actions();
			Action action = this.rollout_policy(possible_moves);
			try {
				current_rollout_state = current_rollout_state.move(action);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return current_rollout_state.game_result();
	}

	@Override
	public void backpropagate(int result) {
		this.number_of_visits += 1;
		
		int old = this._results.get(result) == null ? 0 :  this._results.get(result);
		
		this._results.put(result, old + 1);
		if (this.getParent() != null) {
			this.getParent().backpropagate(result);
		}
	}

}
