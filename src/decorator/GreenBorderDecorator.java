package decorator;

import javafx.scene.Node;
import util.Util;

public class GreenBorderDecorator extends StyleDecorator{

	public GreenBorderDecorator(Node elem) {
		super(elem);
		this.updateStyle();
	}

	@Override
	public void updateStyle() {
		removeProperty(Util.CSS_BORDER_COLOR);
		elem.setStyle(elem.getStyle() + Util.CSS_BORDER_COLOR + Util.DARK_GREEN + ";");
	}

	
	
}
