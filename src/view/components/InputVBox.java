package view.components;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import view.components.InputBox.InputType;
import view.components.MyLabel.LabelSize;

public class InputVBox extends VBox {
	private static int SPACING = 10;
	private Label lblMain;
	private TextField field;

	public InputVBox(StringProperty property, InputType type) {

		this(property, "", type);

	}

	public InputVBox(StringProperty property, String placeholder, InputType type) {

		super();
		this.setSpacing(SPACING);

		this.lblMain = new MyLabel("", LabelSize.LARGE);
		
		this.bindTextProperty(property);
		
		this.lblMain.setPrefWidth(140);
		switch (type) {
		case TEXT:
			field = new TextField();
			break;
		case NUMBER:
			field = new TextField();
			field.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("\\d*")) {
						field.setText(newValue.replaceAll("[^\\d]", ""));
					}
				}
			});
			break;
		case PASSWORD:
			field = new PasswordField();
			field.setPromptText("Your password");
		}
//		this.getField().setPromptText(placeholder);
		this.getChildren().addAll(lblMain, field);
		this.setAlignment(Pos.CENTER);
	}

	public String getValue() {
		return ((TextField) this.field).getText();
	}

	public TextField getField() {
		return this.field;
	}

	public Label getLabel() {
		return this.lblMain;
	}

	public Label setLabel(Label lbl) {
		this.getChildren().set(0, lbl);
		return this.lblMain = lbl;
	}

	public void bindTextProperty(StringProperty property) {
		this.lblMain.textProperty().bind(property);
	}
}
