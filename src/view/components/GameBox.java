package view.components;

import beans.Game;
import decorator.BlackColorDecorator;
import decorator.BorderRadiusDescorator;
import decorator.GreenBackgroundDecorator;
import decorator.GreenBorderDecorator;
import decorator.LightGreenBackgroundDecorator;
import decorator.WhiteColorDecorator;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import view.components.MyLabel.LabelSize;

public class GameBox extends HBox {

	private Label lblName;
	private Game game;

	public GameBox(Game game) {
		this.setGame(game);
		this.lblName = new MyLabel(game.getName(), LabelSize.SMALL);

		this.getChildren().add(lblName);
		new BorderRadiusDescorator(this, 20);
		new GreenBorderDecorator(this);
		new LightGreenBackgroundDecorator(this);
		this.setPadding(new Insets(6, 10, 6, 10));
		new BlackColorDecorator(this.lblName);

		this.setOnMouseEntered(e -> {
			new GreenBackgroundDecorator(this);
			new WhiteColorDecorator(this.lblName);
		});
		this.setOnMouseExited(e -> {
			new LightGreenBackgroundDecorator(this);
			new BlackColorDecorator(this.lblName);
		});
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}


}
