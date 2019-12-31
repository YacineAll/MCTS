package mcts;

public class MonteCarloTreeSearch {

	private MonteCarloTreeSearchNode root;

	public MonteCarloTreeSearch(MonteCarloTreeSearchNode root) {
		super();
		this.root = root;
	}

	public MonteCarloTreeSearchNode best_action(int simulations_number) {
		
		for (int i = 0; i < simulations_number; i++) {
			MonteCarloTreeSearchNode v = this._tree_policy();
			Integer reward = v.rollout();
			
			v.backpropagate(reward);
		}
		
		return this.root.best_child(0.0);
		
	}

	private MonteCarloTreeSearchNode _tree_policy() {
		MonteCarloTreeSearchNode current_node = this.root;
		while (!current_node.is_terminal_node()) {
			if (!current_node.is_fully_expanded()) {
				return current_node.expand();
			} else {
				current_node = current_node.best_child(1.4);
			}
		}
		return current_node;

	}
}
