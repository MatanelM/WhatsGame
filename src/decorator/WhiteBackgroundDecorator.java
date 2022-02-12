package decorator;

import javafx.scene.Node;
import util.Util;

public class WhiteBackgroundDecorator extends StyleDecorator {
	
	public WhiteBackgroundDecorator(Node elem) {
		super(elem);
		updateStyle();
	}

	public void updateStyle() {
		// on change, mouse over etc. can cast and then activate
		removeProperty(Util.CSS_BACKGROUND);
		elem.setStyle(elem.getStyle() + Util.CSS_BACKGROUND + "white;");
	}
}
