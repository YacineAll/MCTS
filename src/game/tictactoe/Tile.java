package game.tictactoe;

import game.State;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Tile extends StackPane {

	private Text text = new Text();
	private State state;
	private boolean playable;
	private boolean turnO;
	
	public Tile(State state,boolean playable) {
		Rectangle border = new Rectangle(200, 200);
		border.setFill(null);
		border.setStroke(Color.BLACK);
		this.playable = playable;
		this.state = state;
		this.turnO = (this.state.next_to_move() == 1);
		

		text.setFont(Font.font(72));

		setAlignment(Pos.CENTER);
		getChildren().addAll(border, text);

		setOnMouseClicked(event -> {
			if (!playable)
				return;

			if (event.getButton() == MouseButton.PRIMARY) {
				if (!turnO)
					return;

				drawX();
				this.playable = false;
			}
		});
	}
	
	public void setState(State state) {
		this.state = state;
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
