package view.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import util.Util;

public class MyLabel extends Label{
	public enum LabelSize{
		SMALL, MEDIUM, LARGE, EXTRA_LARGE
	}
	
	private static final Font SMALL_FONT = Font.font(Util.MainFont, FontWeight.BOLD, FontPosture.REGULAR, 12);
	private static final Font MEDIUM_FONT = Font.font(Util.MainFont, FontWeight.BOLD, FontPosture.REGULAR, 16);
	private static final Font LARGE_FONT = Font.font(Util.MainFont, FontWeight.BOLD, FontPosture.REGULAR, 20);
	private static final Font EXTRA_LARGE_FONT = Font.font(Util.MainFont, FontWeight.BOLD, FontPosture.REGULAR, 24);
	
	private static final int SMALL_HEIGHT = 18;
	private static final int MEDIUM_HEIGHT = 24;
	private static final int LARGE_HEIGHT = 30;
	private static final int EXTRA_LARGE_HEIGHT = 36;
	
	
	public MyLabel() {
	}

	public MyLabel(String text, LabelSize size) {
		super(text);
		switch (size) {
		case SMALL:
			this.setFont(SMALL_FONT);
			this.setPrefHeight(SMALL_HEIGHT);
			break;
		case MEDIUM:
			this.setFont(MEDIUM_FONT);
			this.setPrefHeight(MEDIUM_HEIGHT);
			break;
		case LARGE:
			this.setFont(LARGE_FONT);
			this.setPrefHeight(LARGE_HEIGHT);
			break;
		case EXTRA_LARGE:
			this.setFont(EXTRA_LARGE_FONT);
			this.setPrefHeight(EXTRA_LARGE_HEIGHT);
			break;
		default:
			break;
		}

	}

	public MyLabel(String text, Node graphic) {
		super(text, graphic);
	}

}
