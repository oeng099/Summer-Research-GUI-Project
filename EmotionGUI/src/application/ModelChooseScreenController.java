package application;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class ModelChooseScreenController extends ScriptChangeController{
	
	@FXML
	Text modelChangeInstructions;
	@FXML
	Text valence;
	@FXML
	Text arousal;
	@FXML
	TextField valenceModel;
	@FXML
	TextField arousalModel;
	@FXML
	Button valenceButton;
	@FXML
	Button arousalButton;
	@FXML
	Button apply;
	@FXML
	Button ok;
	@FXML
	GridPane scenePane;
	@FXML
	BorderPane borderPane;
	
	//Method to change the valence machine-learning model
	public void changeValenceModel(ActionEvent event) {

		File modelFile = chooseModelFile();
		
		if (modelFile != null) {
			//Displays the file chosen as the new valence machine-learning model
			valenceModel.setText(modelFile.toString());
		}
	}
	
	//Method to change the arousal machine-learning model
	public void changeArousalModel(ActionEvent event) {

		File modelFile = chooseModelFile();
		
		if (modelFile != null) {
			//Displays the file chosen as the new valence machine-learning model
			arousalModel.setText(modelFile.toString());
		}
	}
	
	//Method to choose a file through the file chooser
	public File chooseModelFile() {
		FileChooser fileChooser = new FileChooser();

		File modelFile = fileChooser.showOpenDialog(stage);
		return modelFile;
	}
	
	//Method to change the current models in the Python script with the files selected
	public void applyChanges(ActionEvent event) throws IOException {
		if(!arousalModel.getText().isEmpty()) {
			//Changes arousal model if there is a file selected for it
			changePythonScript("src/application/group108demo.py","arousalModelPath =", "arousalModelPath ='" + arousalModel.getText() + "'");
		}
		if(!valenceModel.getText().isEmpty()) {
			//Changes valence model if there is a file selected for it
			changePythonScript("src/application/group108demo.py","valenceModelPath =", "valenceModelPath ='" + valenceModel.getText() + "'");
		}
	}
	
	//Method to return to visualisation screen.
	public void ok(ActionEvent event) throws IOException {
		changeScreen(event,Screen.VISUALISATION);
	}

}
