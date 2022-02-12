package view.components;


import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class InputBox extends HBox {

	private static int SPACING = 10;

	public enum InputType {
		TEXT, NUMBER, PASSWORD
	}

	private Label lblMain;
	private TextField field;

	public InputBox(String label, InputType type) {
		super();
		this.setSpacing(SPACING);
		this.lblMain = new Label(label);
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
		this.getChildren().addAll(lblMain, field);
		this.setAlignment(Pos.CENTER_LEFT);

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
		return this.lblMain = lbl;
	}
	
	public void bindTextProperty(StringProperty property) {
		this.lblMain.textProperty().bind(property);
	}

}
