package game.tictactoe;

import java.util.ArrayList;
import java.util.List;

import game.State;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import mcts.MonteCarloTreeSearch;
import mcts.MonteCarloTreeSearchNode;
import mcts.TwoPlayersGameMonteCarloTreeSearchNode;

public class TicTacToe_GUI extends Application {
	private final int nb_simulation = 1000;

	private Tile[][] board = new Tile[3][3];
	private List<Combo> combos = new ArrayList<>();
	private State state = new TicTacToeGameState(1);

	private Pane root = new Pane();

	private Parent createContent() {
		root.setPrefSize(600, 600);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Tile tile = new Tile(true, i, j);
				tile.setTranslateX(j * 200);
				tile.setTranslateY(i * 200);

				root.getChildren().add(tile);

				board[i][j] = tile;
			}
		}

		// horizontal
		for (int y = 0; y < 3; y++) {
			combos.add(new Combo(board[0][y], board[1][y], board[2][y]));
		}

		// vertical
		for (int x = 0; x < 3; x++) {
			combos.add(new Combo(board[x][0], board[x][1], board[x][2]));
		}

		// diagonals
		combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
		combos.add(new Combo(board[2][0], board[1][1], board[0][2]));

		return root;
	}

	private void o_play() {
		try {
			System.out.println(state);
			MonteCarloTreeSearchNode root = new TwoPlayersGameMonteCarloTreeSearchNode(state, null, null);
			MonteCarloTreeSearch ts = new MonteCarloTreeSearch(root);
			TicTacToeMove move = (TicTacToeMove) ts.best_action(nb_simulation).getAction();
			state = state.move(move);
			System.out.println(state);
			this.board[move.getX_coordinate()][move.getY_coordinate()].setPlayable(false);
			if (move.getValue() == 1 ) {
				this.board[move.getX_coordinate()][move.getY_coordinate()].drawX() ;
			}else {
				this.board[move.getX_coordinate()][move.getY_coordinate()].drawO();
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

			Rectangle border = new Rectangle(200, 200);
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
						state = state.move(new TicTacToeMove(this.x, this.y, 1) );
						drawX();
						this.playable = false;
						checkState();
						o_play();
						checkState();
					} catch (Exception e) {
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
	}

//	public static void main(String[] args) {
//		
//	}

	private void checkState() {
		for (Combo combo : combos) {
			if (combo.isComplete()) {
				playWinAnimation(combo);
				break;
			}
		}
	}

	private void playWinAnimation(Combo combo) {
		Line line = new Line();
		line.setStartX(combo.tiles[0].getCenterX());
		line.setStartY(combo.tiles[0].getCenterY());
		line.setEndX(combo.tiles[0].getCenterX());
		line.setEndY(combo.tiles[0].getCenterY());

		root.getChildren().add(line);

		Timeline timeline = new Timeline();
		timeline.getKeyFrames()
				.add(new KeyFrame(Duration.seconds(1), new KeyValue(line.endXProperty(), combo.tiles[2].getCenterX()),
						new KeyValue(line.endYProperty(), combo.tiles[2].getCenterY())));
		timeline.play();
	}

	private class Combo {
		private Tile[] tiles;

		public Combo(Tile... tiles) {
			this.tiles = tiles;
		}

		public boolean isComplete() {
			if (tiles[0].getValue().isEmpty())
				return false;

			return tiles[0].getValue().equals(tiles[1].getValue()) && tiles[0].getValue().equals(tiles[2].getValue());
		}
	}
}
