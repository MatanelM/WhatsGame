package decorator;

import javafx.scene.Node;
import util.Util;

public class BorderRadiusDescorator extends StyleDecorator {

	public BorderRadiusDescorator(Node elem, int num) {
		super(elem);
		updateStyle(num);
	}

	private void updateStyle(int num) {
		updateStyle();
		elem.setStyle(elem.getStyle() + Util.CSS_BACKGROUND_RADIUS + num + "px;");
		elem.setStyle(elem.getStyle() + Util.CSS_BORDER_RADIUS + num + "px;");
		
	}

	@Override
	protected void updateStyle() {
		removeProperty(Util.CSS_BACKGROUND_RADIUS);
		removeProperty(Util.CSS_BORDER_RADIUS);
		
	}

}
