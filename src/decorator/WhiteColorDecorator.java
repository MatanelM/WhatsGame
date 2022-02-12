package decorator;

import javafx.scene.Node;
import util.Util;

public class WhiteColorDecorator extends StyleDecorator {
	public WhiteColorDecorator(Node elem) {
		super(elem);
		updateStyle();
	}

	@Override
	protected void updateStyle() {
		this.removeProperty(Util.CSS_COLOR);
		elem.setStyle(this.elem.getStyle() + Util.CSS_COLOR + Util.WHITE + ";");
	}
}
