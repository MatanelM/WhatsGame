package strategy;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class HebrewContext implements LanguageStrategy{

	public HebrewContext(VBox root) {
		super();
		setLanguage(createMap());
		setRTL(root);
	}
	public void setRTL(Pane parent) {
		
		if ( parent.getChildren().isEmpty() ) {
			return;
		}
		parent.getChildren().forEach(x -> {
			((Node)x).setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
		});
		
	}
	public Map<String, StringProperty> createMap() {
		Map<String, StringProperty> strings = new HashMap<String, StringProperty>();
		
		strings.put("login_button", new SimpleStringProperty("התחבר"));
		strings.put("login_username", new SimpleStringProperty("שם משתמש"));
		strings.put("login_password", new SimpleStringProperty("סיסמה"));
		
		strings.put("top_bar_btn_1", new SimpleStringProperty("צאט"));
		strings.put("top_bar_btn_2", new SimpleStringProperty("חברים"));
		strings.put("top_bar_btn_3", new SimpleStringProperty("עדכונים"));
		
		strings.put("chat_button_send", new SimpleStringProperty("שלח"));
		
		
		return strings;
	}

	
	
}
