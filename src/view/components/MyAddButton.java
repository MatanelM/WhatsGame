package view.components;

import decorator.BlackColorDecorator;
import decorator.BorderRadiusDescorator;
import decorator.GreenBackgroundDecorator;
import decorator.GreenBorderDecorator;
import decorator.LightGreenBackgroundDecorator;
import decorator.WhiteColorDecorator;
import javafx.geometry.Insets;
import view.components.MyLabel.LabelSize;

public class MyAddButton extends MyButton {
	
	public MyAddButton(String title, ButtonSizeType type) {
		super(title, type);
		this.lblName = new MyLabel(title, LabelSize.LARGE);

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
}
