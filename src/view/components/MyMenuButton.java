package view.components;
import decorator.*;
import javafx.beans.property.StringProperty;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import util.Util;

public class MyMenuButton extends MyButton {
	
	private static final Font MEDIUM_FONT = Font.font(Util.MainFont, FontWeight.BOLD, FontPosture.REGULAR, 16);
	private boolean isSelected;
	
	public MyMenuButton(StringProperty title) {
		super("",ButtonSizeType.MEDIUM);
		new GreenBackgroundDecorator(this);
		this.setFont(MEDIUM_FONT);
		new WhiteColorDecorator(this);
		this.setPrefWidth(400/3);
		this.textProperty().bind(title);
		new BorderRadiusDescorator(this, 0);
		
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		
		this.isSelected = isSelected;
		if (this.isSelected ) {
			new LightGreenBackgroundDecorator(this);
		}else {
			new GreenBackgroundDecorator(this);
		}
	}
	
	
}
