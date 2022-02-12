package decorator;

import javafx.scene.Node;
import util.Util;

public class HandMouseDecorator extends StyleDecorator {

	public HandMouseDecorator(Node elem) {
		super(elem);
		updateStyle();
	}

	@Override
	protected void updateStyle() {
		removeProperty(Util.CSS_MOUSE);
		elem.setStyle(elem.getStyle() + Util.CSS_MOUSE + "hand;");
	}

}
