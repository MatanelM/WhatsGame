package decorator;

import javafx.scene.Node;
import util.Util;

public class HideDecorator extends StyleDecorator{

	public HideDecorator(Node elem) {
		super(elem);
		this.updateStyle();
	}

	@Override
	protected void updateStyle() {
		this.removeProperty(Util.CSS_VISIBILITY);
		elem.setStyle(elem.getStyle() + Util.CSS_VISIBILITY+ "collapse;");
	}

	
	
}
