package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Controller {

	
	protected Stage stage;
	protected Scene scene;
	protected Parent root;
	
	protected FXMLLoader loader;
	
	@FXML
	protected BorderPane borderPane;
	
	public enum Screen
	{
		HOME,
		VISUALISATION,
		ANNOTATION,
		MODELCHOOSE
	}
	
	//Method to change the screen to whichever screen is inputed.
	public void changeScreen(ActionEvent buttonEvent,Screen screen) throws IOException{
		switch(screen) {
		case HOME:
			loadLoader("fxml/HomeScreen.fxml");
			showStage(buttonEvent,"css/HomeScreen.css");
			stage.setTitle("EmotionGUI");
			break;
		case VISUALISATION:
			loadLoader("fxml/VisualiseScreen.fxml");
			showStage(buttonEvent,"css/VisualiseScreen.css");
			stage.setTitle("Visualisation Screen");
			break;
		case ANNOTATION:
			loadLoader("fxml/AnnotationScreen.fxml");
			showStage(buttonEvent,"css/AnnotationScreen.css");
			//Sets up a key to play/pause while in the annotation screen
			setPlayPauseKey();
			stage.setTitle("Annotation Screen");
			break;
		case MODELCHOOSE:
			loadLoader("fxml/ModelChooseScreen.fxml");
			showStage(buttonEvent,"css/ModelChooseScreen.css");
			stage.setTitle("Change the Valence and Arousal Models");
			break;
		}
	}
	
	//Method to set and load the loader
	public void loadLoader(String fxml) throws IOException {
		loader = new FXMLLoader(getClass().getResource(fxml));
		root = loader.load();
	}
	
	//Method to show the stage
	public void showStage(ActionEvent event, String css) {
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root,borderPane.getWidth(),borderPane.getHeight());
		scene.getStylesheets().add(getClass().getResource(css).toExternalForm());
		stage.setScene(scene);
		stage.show();
	
		stage.setOnCloseRequest(newEvent ->{
			newEvent.consume();
			stage.close();
		});
	}
	
	//Method to set a key for play/pause in the annotation screen
	public void setPlayPauseKey() {
		AnnotationScreenController annotationController = loader.getController();
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				//Sets the key 'p' to function the same as a play/pause button in the annotation screen.
				if(event.getCode() == KeyCode.P) {
				annotationController.pKeyPressed();
				}
			}
		}); 
	}

	public void changingScriptLines() {
		// TODO Auto-generated method stub
		
	}
}
