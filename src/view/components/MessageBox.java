package view.components;

import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import util.Util;


import beans.Message;
import decorator.*;

public class MessageBox extends GridPane {

	public enum MessageOrientation{
		LEFT, RIGHT
	}
	
	public MessageBox(Message message, MessageOrientation orientation) {
		super();
		new BorderRadiusDescorator(this, 20);
		this.setPadding(new Insets(4,6,4,6));
		
		
		
		Text label = new Text(message.getContent());
		label.setWrappingWidth(365);
		label.setFont(Font.font(Util.MainFont, 20));
		FlowPane fp= new FlowPane();
		fp.getChildren().add(label);
		switch (orientation) {
		case LEFT:
			this.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
			new GreenBackgroundDecorator(this);
			new WhiteColorDecorator(label);
			break;
			
		case RIGHT:
			this.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
			new LightGreenBackgroundDecorator(this);
			new BlackColorDecorator(label);
			break;
		default:
			break;
		}
		this.add(fp, 0, 0);
		this.setMaxWidth(375);
		Label lbl = new Label(message.getDate().toString());
		lbl.setFont(Font.font(Util.MainFont, 8));
		lbl.setTextFill(Color.WHITE);
		
		this.add(lbl, 0, 1);
		
		
	}
	
}
