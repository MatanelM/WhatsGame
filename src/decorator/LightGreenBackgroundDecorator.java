package decorator;

import javafx.scene.Node;
import util.Util;

public class LightGreenBackgroundDecorator extends StyleDecorator {

	public LightGreenBackgroundDecorator(Node elem) {
		super(elem);
		updateStyle();
	}

	@Override
	protected void updateStyle() {
		super.removeProperty(Util.CSS_BACKGROUND);
		elem.setStyle(elem.getStyle() + Util.CSS_BACKGROUND + Util.LIGHT_GREEN + ";");

	}
	
}
