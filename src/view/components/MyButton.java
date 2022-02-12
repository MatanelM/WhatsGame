package view.components;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

public class MyButton extends Button {

	public enum ButtonSizeType{
		SMALL, MEDIUM, LARGE, EXTRA_LARGE
	}
	
	private static final int SMALL_HEIGHT = 20;
	private static final int SMALL_WIDTH = 130;
	private static final int MEDIUM_HEIGHT = 30;
	private static final int MEDIUM_WIDTH = 140;
	private static final int LARGE_HEIGHT = 40;
	private static final int LARGE_WIDTH = 160;
	private static final int EXTRA_LARGE_HEIGHT = 50;
	private static final int EXTRA_LARGE_WIDTH = 180;
	

	protected MyLabel lblName;
	public MyButton() {
		super();
		
	}
	public MyButton(String title, ButtonSizeType size) {
		super(title);
		
		
		
		switch (size) {
		case SMALL:
			this.setPrefHeight(SMALL_HEIGHT);
			this.setPrefWidth(SMALL_WIDTH);
			break;
		case MEDIUM:
			this.setPrefHeight(MEDIUM_HEIGHT);
			this.setPrefWidth(MEDIUM_WIDTH);
			break;
		case LARGE:
			this.setPrefHeight(LARGE_HEIGHT);
			this.setPrefWidth(LARGE_WIDTH);
			break;
		case EXTRA_LARGE:
			this.setPrefHeight(EXTRA_LARGE_HEIGHT);
			this.setPrefWidth(EXTRA_LARGE_WIDTH);
			break;
		default:
			break;
		}
		
	}
	public MyButton(StringProperty title, ButtonSizeType size) {
		super();
		this.textProperty().bind(title);
		switch (size) {
		case SMALL:
			this.setPrefHeight(SMALL_HEIGHT);
			this.setPrefWidth(SMALL_WIDTH);
			break;
		case MEDIUM:
			this.setPrefHeight(MEDIUM_HEIGHT);
			this.setPrefWidth(MEDIUM_WIDTH);
			break;
		case LARGE:
			this.setPrefHeight(LARGE_HEIGHT);
			this.setPrefWidth(LARGE_WIDTH);
			break;
		case EXTRA_LARGE:
			this.setPrefHeight(EXTRA_LARGE_HEIGHT);
			this.setPrefWidth(EXTRA_LARGE_WIDTH);
			break;
		default:
			break;
		}
		
	}
}
