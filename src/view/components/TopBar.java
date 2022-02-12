package view.components;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import strategy.LanguageStrategy;
import view.components.MyLabel.LabelSize;

import java.util.ArrayList;

import decorator.*;
public class TopBar extends VBox{

	
	private ArrayList<MyMenuButton> buttons = new ArrayList<>();
	private MyLabel lblProfile;
	
	public MyLabel getLblProfile() {
		return lblProfile;
	}

	public void setLblProfile(MyLabel lblProfile) {
		this.lblProfile = lblProfile;
	}

	public TopBar(LanguageStrategy language) {
		
		super();
		new GreenBackgroundDecorator(this);
		this.setSpacing(20);
		HBox hbTitle = new HBox();
		hbTitle.setPadding(new Insets(10));
		MyLabel lblTitle = new MyLabel("WhatsGame", LabelSize.LARGE);
		this.lblProfile = new MyLabel("", LabelSize.EXTRA_LARGE);
		Image im = new Image("file:assets/profile.png");
		ImageView img = new ImageView(im);
		lblProfile.setGraphic(img);
		
		new WhiteColorDecorator(lblTitle);
		hbTitle.getChildren().addAll(lblProfile, lblTitle);
		HBox hbMenu = new HBox();
		
		MyMenuButton btn1 = new MyMenuButton(language.getMessage("top_bar_btn_1"));
		MyMenuButton btn2 = new MyMenuButton(language.getMessage("top_bar_btn_2"));
		MyMenuButton btn3 = new MyMenuButton(language.getMessage("top_bar_btn_3"));
		
		buttons.add(btn1);
		buttons.add(btn2);
		buttons.add(btn3);
		
		buttons.forEach(b -> {
			b.setOnAction(e -> {
				buttons.forEach(b1 -> {
					b1.setSelected(false);
				});
				b.setSelected(true);
			});
		});
		
		btn1.setSelected(true);
		
		hbMenu.getChildren().addAll(btn1, btn2, btn3);
		this.getChildren().addAll(hbTitle, hbMenu);
	}

	public ButtonBase getButtonChat() {
		return this.buttons.get(0);
	}
	
	public ButtonBase getButtonFriends() {
		return this.buttons.get(1);
	}
	
	public ButtonBase getButtonUpdates() {
		return this.buttons.get(2);
	}
	
}
