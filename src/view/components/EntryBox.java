package view.components;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import view.components.MyLabel.LabelSize;

import decorator.*;

public class EntryBox extends GridPane {

	// this might get more items in order to update messages in the chat, like task
	// or chat id
	
	public EntryBox(String content) {
		
		this.setPadding(new Insets(20, 0, 20, 16));
		MyLabel lblName = new MyLabel(content, LabelSize.MEDIUM);
		
		lblName.setMinWidth(310);
		this.add(lblName, 0, 1);
		
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

	}

}
