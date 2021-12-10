package application;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class VisualiseScreenController {
	
	@FXML
	Button selectFile;
	@FXML
	Button plotFile;
	@FXML
	Button plotCoordinates;
	@FXML
	Button mainMenu;
	@FXML
	TextField fileName;
	@FXML
	BorderPane borderPane;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void selectAFile(ActionEvent event) {
		
		FileChooser fileChooser = new FileChooser();
		
		File file = fileChooser.showOpenDialog(stage);
		if(file != null) {
			fileName.setText(file.toString());
		}
	}
	
	public void returnToMainMenu(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("fxml/HomeScreen.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root,borderPane.getWidth(),borderPane.getHeight());
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
