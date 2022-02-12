package view.components;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import view.components.MyButton.ButtonSizeType;
import view.components.MyLabel.LabelSize;

import decorator.*;

public class EntryButtonBox extends HBox {

	
	private MyAddButton btn = new MyAddButton("Add", ButtonSizeType.MEDIUM);
	
	
	public EntryButtonBox(String content) {
		
		this.setPadding(new Insets(20, 0, 20, 16));
		MyLabel lblName = new MyLabel(content, LabelSize.MEDIUM);
		lblName.setMinWidth(310);
		this.getChildren().add(lblName);
		new BlackColorDecorator(lblName);
		new HalfWhiteBackgroundDecorator(this);

		this.setOnMouseEntered(e -> {
			new GreenBackgroundDecorator(this);
			new WhiteColorDecorator(lblName);
		});
		this.setOnMouseExited(e -> {
			new HalfWhiteBackgroundDecorator(this);
			new BlackColorDecorator(lblName);
		});

		this.getChildren().add(btn);
	}
	
	public MyAddButton getButton() {
		return this.btn;
	}

}
