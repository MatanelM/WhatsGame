package view.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import decorator.*;

public class IconBox extends HBox {

	public IconBox(String path) {
		Image imgIL = new Image("file:"+path);
		ImageView imgViewIL = new ImageView();
		imgViewIL.setImage(imgIL);
		Label lblIL = new Label("");
		lblIL.setGraphic(imgViewIL);
		this.getChildren().add(lblIL);
		
		this.setPadding(new Insets(10));
		new BorderRadiusDescorator(this, 80);
		this.setMaxWidth(50);
		
		
		this.setOnMouseEntered(e -> {
			new HandMouseDecorator(this);
		});
		this.setOnMouseExited(e -> {
			new MouseDecorator(this);
		});
		
	}
	
}
