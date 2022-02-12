package strategy;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class EnglishContext implements LanguageStrategy {
	
	public EnglishContext(VBox root) {
		super();
		this.setLanguage(createMap());
		setLTR(root);
	}
	public void setLTR(Pane parent) {
		
		if ( parent.getChildren().isEmpty() ) {
			return;
		}
		parent.getChildren().forEach(x -> {
			((Node)x).setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
		});
		
	}
	public Map<String, StringProperty> createMap() {
		Map<String, StringProperty> strings = new HashMap<String, StringProperty>();
		
		strings.put("login_button", new SimpleStringProperty("Login"));
		strings.put("login_username", new SimpleStringProperty("Nickname"));
		strings.put("login_password", new SimpleStringProperty("Password"));
		
		strings.put("top_bar_btn_1", new SimpleStringProperty("Chats"));
		strings.put("top_bar_btn_2", new SimpleStringProperty("Friends"));
		strings.put("top_bar_btn_3", new SimpleStringProperty("Updates"));
		
		strings.put("chat_button_send", new SimpleStringProperty("Send"));
		
		
		
		
		return strings;
	}
}
