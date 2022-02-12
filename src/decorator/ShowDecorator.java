package decorator;

import javafx.scene.Node;
import util.Util;

public class ShowDecorator extends StyleDecorator {

	public ShowDecorator(Node elem) {
		super(elem);
		this.updateStyle();
	}

	@Override
	protected void updateStyle() {
		this.removeProperty(Util.CSS_VISIBILITY);
		elem.setStyle(elem.getStyle() + Util.CSS_VISIBILITY +"visible;");
	}

}
