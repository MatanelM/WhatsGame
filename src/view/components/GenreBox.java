package view.components;

import beans.Genre;
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

public class GenreBox extends HBox {

	private Label lblName;
	private Genre genre;

	public GenreBox(Genre genre) {
		this.setGenre(genre);
		this.lblName = new MyLabel(genre.getGenre(), LabelSize.SMALL);

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

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

}
