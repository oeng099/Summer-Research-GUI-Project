package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ModelChooseScreenController{

	Stage stage;
	Scene scene;
	Parent root;
	
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
	
	public void changeValenceModel(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();

		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			valenceModel.setText(file.toString());
		}
	}
	
	public void changeArousalModel(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();

		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			arousalModel.setText(file.toString());
		}
	}
	
	public void applyChanges(ActionEvent event) throws IOException {
		BufferedReader pythonScriptReader = new BufferedReader(new FileReader("src/application/group108demo.py"));
		StringBuffer inputBuffer = new StringBuffer();
		String line;
		
		while((line = pythonScriptReader.readLine()) != null) {
			if(line.contains("arousalModelPath =") && !arousalModel.getText().isEmpty()) {
				line = "arousalModelPath = '" + arousalModel.getText() + "'";
			} else if(line.contains("valenceModelPath =") && !valenceModel.getText().isEmpty()) {
				line = "valenceModelPath = '" + valenceModel.getText() + "'";
			}
			
			inputBuffer.append(line);
			inputBuffer.append('\n');
		}
		pythonScriptReader.close();
		
		FileOutputStream writeToModelFile = new FileOutputStream("src/application/WAV_To_CSV/group108demo.py");
		writeToModelFile.write(inputBuffer.toString().getBytes());
		writeToModelFile.close();
	}
	
	public void ok(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("fxml/VisualiseScreen.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root,borderPane.getWidth(),borderPane.getHeight());
		scene.getStylesheets().add(getClass().getResource("css/VisualiseScreen.css").toExternalForm());
		stage.setTitle("Visualise Screen");
		stage.setScene(scene);
		stage.show();
	}
}
