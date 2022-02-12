package decorator;

import javafx.scene.Node;
import util.Util;

public class HalfWhiteBackgroundDecorator extends StyleDecorator {

	public HalfWhiteBackgroundDecorator(Node elem) {
		super(elem);
		this.updateStyle();
	}

	@Override
	protected void updateStyle() {
		this.removeProperty(Util.CSS_BACKGROUND);
		this.elem.setStyle(this.elem.getStyle() + Util.CSS_BACKGROUND + Util.WHITE_HALF + ";");
	}

}
