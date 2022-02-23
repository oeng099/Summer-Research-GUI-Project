package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class HomeScreenController extends Controller {

	@FXML
	Button visualise;
	@FXML
	Button annotation;
	@FXML
	Text emotionGUI;
	
	//Method to change to visualisation screen
	public void changeToVisualise(ActionEvent event) throws IOException {
		changeScreen(event,Screen.VISUALISATION);
	}
	
	//Methiod to change to annotation screen
	public void changeToAnnotation(ActionEvent event) throws IOException {
		changeScreen(event,Screen.ANNOTATION);
	}
	
}