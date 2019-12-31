package game.connect4;

import game.State;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mcts.MonteCarloTreeSearch;
import mcts.MonteCarloTreeSearchNode;
import mcts.TwoPlayersGameMonteCarloTreeSearchNode;

public class Connect4GUI extends Application {
	private final int nb_simulation = 10000;

	private Tile[][] board = new Tile[6][7];
	
	private State state = new Connect4State(-1);

	private Pane root = new Pane();

	private Parent createContent() {

		root.setPrefSize(700, 600);

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				Tile tile = new Tile(true, i, j);
				tile.setTranslateX(j * 100);
				tile.setTranslateY(i * 100);

				root.getChildren().add(tile);

				board[i][j] = tile;
			}
		}

		
		return root;
	}

	private void o_play() {
		try {

			if (!state.is_game_over() && (state.next_to_move() == -1)) {

				System.out.println(state);
				MonteCarloTreeSearchNode root = new TwoPlayersGameMonteCarloTreeSearchNode(state, null, null);
				MonteCarloTreeSearch ts = new MonteCarloTreeSearch(root);
				Connect4Action move = (Connect4Action) ts.best_action(nb_simulation).getAction();
				state = state.move(move);
				System.out.println(state);
				Connect4State st = (Connect4State) state;
				int x = st.get_x_availble(move.getIndex()) + 1;
				int y = move.getIndex();
				this.board[x][move.getIndex()].setPlayable(false);

				this.board[x][y].drawO();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class Tile extends StackPane {

		private Text text = new Text();

		private boolean playable;
		private int x, y;

		public Tile(boolean playable, int x, int y) {

			this.x = x;
			this.y = y;

			Rectangle border = new Rectangle(100, 100);
			border.setFill(null);
			border.setStroke(Color.BLACK);
			this.playable = playable;

			text.setFont(Font.font(72));

			setAlignment(Pos.CENTER);
			getChildren().addAll(border, text);

			setOnMouseClicked(event -> {
				if (!this.playable)
					return;

				if (state.next_to_move() == 1) {
					try {

						state = state.move(new Connect4Action(this.y, 1));

						Connect4State st = (Connect4State) state;
						
						board[st.get_x_availble(this.y) + 1][this.y].drawX();
						this.playable = false;
						o_play();
					} catch (IndexOutOfBoundsException e) {
						return;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			});
		}

		public int getX() {
			return x;
		}

		public void setPlayable(boolean playable) {
			this.playable = playable;
		}

		public int getY() {
			return y;
		}

		public boolean isPlayable() {
			return playable;
		}

		public double getCenterX() {
			return getTranslateX() + 100;
		}

		public double getCenterY() {
			return getTranslateY() + 100;
		}

		public String getValue() {
			return text.getText();
		}

		public void drawX() {
			text.setText("X");
		}

		public void drawO() {
			text.setText("O");
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new Scene(createContent()));
		primaryStage.show();
		o_play();
	}

}
