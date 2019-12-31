package mcts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import game.Action;
import game.State;

import java.lang.Math;

public abstract class MonteCarloTreeSearchNode {

	private State state;
	private MonteCarloTreeSearchNode parent;
	private ArrayList<MonteCarloTreeSearchNode> children;
	private Action action ;

	public MonteCarloTreeSearchNode(State state, MonteCarloTreeSearchNode parent, Action act) {
		super();
		this.state = state;
		this.parent = parent;
		this.action = act;
		this.children = new ArrayList<MonteCarloTreeSearchNode>();
	}
	
	public Action getAction() {
		return action;
	}
	
	

	public MonteCarloTreeSearchNode getParent() {
		return parent;
	}

	public boolean is_fully_expanded() {
		return this.untried_actions().isEmpty();
	}

	public ArrayList<MonteCarloTreeSearchNode> getChildren() {
		return children;
	}

	public State getState() {
		return state;
	}

	public void add_child(MonteCarloTreeSearchNode child) {
		this.children.add(child);
	}

	public abstract List<Action> untried_actions();

	public abstract int q();

	public abstract int n();

	public abstract MonteCarloTreeSearchNode expand();

	public abstract boolean is_terminal_node();

	public abstract Integer rollout();

	public abstract void backpropagate(Integer result);

	public MonteCarloTreeSearchNode best_child(double c_param) {
		ArrayList<Double> array = new ArrayList<Double>();
		for (MonteCarloTreeSearchNode c : children) {

			double n = c.q() / (float) c.n() + c_param * Math.sqrt((2 * this.n() / (float) (c.n())));

			array.add(n);
		}
		return this.children.get(array.indexOf(Collections.max(array)));

	}

	public Action rollout_policy(List<Action> possible_moves) {
		return possible_moves.get(new Random().nextInt(possible_moves.size()));
	}

}
