package decorator;

import javafx.scene.Node;
import util.Util;

public class GreenBackgroundDecorator extends StyleDecorator {

	
	
	public GreenBackgroundDecorator(Node elem) {
		super(elem);
		this.updateStyle();
	}

	@Override
	protected void updateStyle() {
		this.removeProperty(Util.CSS_BACKGROUND);
		this.elem.setStyle(elem.getStyle() + Util.CSS_BACKGROUND + Util.GREEN + ";");
	}

}
