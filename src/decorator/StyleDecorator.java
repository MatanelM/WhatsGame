package decorator;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.scene.Node;

public abstract class StyleDecorator implements Styleable{
	protected Node elem;
	
	public StyleDecorator(Node elem) {
		this.elem = elem;
	}
	
	@Override
	public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
		return this.elem.getCssMetaData();
	}

	@Override
	public String getId() {
		return this.elem.getId();
	}

	@Override
	public ObservableSet<PseudoClass> getPseudoClassStates() {
		return this.elem.getPseudoClassStates();
	}

	@Override
	public String getStyle() {
		return this.elem.getStyle();
	}

	@Override
	public ObservableList<String> getStyleClass() {
		return this.elem.getStyleClass();
		
	}

	@Override
	public Styleable getStyleableParent() {
		return this.elem.getStyleableParent();
	}

	@Override
	public String getTypeSelector() {
		return elem.getTypeSelector();
	}
	
	protected abstract void updateStyle();
	
	public boolean removeProperty(String property) {
		String nodeCss = this.getStyle();
		if ( nodeCss.contains(property))
		{
			int startIndex = nodeCss.indexOf(property);
			int endIndex = nodeCss.indexOf(";", startIndex);
			String startCss = nodeCss.substring(0, startIndex);
			String endCss = nodeCss.substring(endIndex + 1, nodeCss.length());
			String newCss = startCss + endCss;
			elem.setStyle(newCss);
			return true;
		}
		
		return false;
	}


}
