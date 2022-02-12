package run;

import controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import view.View;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {

		Model model = new Model();
		View view = new View(stage);
//		AdminView view = new AdminView(stage);

		Controller controller = new Controller(model, view);
	}
}
