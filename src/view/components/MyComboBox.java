package view.components;

import beans.Country;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class MyComboBox extends HBox{

	private Label lblMain;
	private ComboBox<String> cmbBox;

	public MyComboBox(String label) {
		super();
		this.lblMain = new Label(label);
		lblMain.setPrefWidth(140);
		this.cmbBox = new ComboBox<String>();
		for(int i=0; i<Country.values().length; i++) {
			cmbBox.getItems().addAll(Country.values()[i].toString());
		}
		
		this.setSpacing(10);
		this.setAlignment(Pos.CENTER_LEFT);
		this.getChildren().addAll(lblMain, cmbBox);
	}

	public String getValue() {
		return this.cmbBox.getValue();
	}

	public void setValue(String newValue) {
		this.cmbBox.setValue(newValue);
	}

	public void addValue(String value) {
		this.cmbBox.getItems().add(value);
	}

	public void reset() {
		this.cmbBox.getItems().clear();
	}

	
	public void setItems(ComboBox<String> newCmbBox) {
		this.cmbBox = newCmbBox;
	}

	public ComboBox<String> getComboBox() {
		return this.cmbBox;
	}

}
