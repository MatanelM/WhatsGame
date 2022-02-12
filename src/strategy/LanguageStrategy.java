package strategy;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public interface LanguageStrategy {

	static Map<String, StringProperty> strings = new HashMap<String, StringProperty>();
		
	public default StringProperty getMessage(String key) {
		return this.strings.get(key);
	}
	
	abstract Map<String, StringProperty> createMap();
	
	default void setLanguage(Map<String, StringProperty> language) {
		language.entrySet().forEach(e->{
			if ( this.strings.get(e.getKey() )!= null )
				this.strings.get(e.getKey()).setValue(e.getValue().getValueSafe());
			else
				this.strings.put(e.getKey(), new SimpleStringProperty(e.getValue().getValueSafe()));
		});
	}
}
