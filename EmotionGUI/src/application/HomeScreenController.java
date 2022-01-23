package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

public class HomeScreenController {

	@FXML
	Button visualise;
	@FXML
	Button annotation;
	@FXML
	BorderPane borderPane;
	@FXML
	Text emotionGUI;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void changeToVisualise(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("VisualiseScreen.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root,borderPane.getWidth(),borderPane.getHeight());
			scene.getStylesheets().add(getClass().getResource("css/VisualiseScreen.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void changeToAnnotation(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("AnnotationScreen.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root,borderPane.getWidth(),borderPane.getHeight());
			scene.getStylesheets().add(getClass().getResource("css/AnnotationScreen.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}