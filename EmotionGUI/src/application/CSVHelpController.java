package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CSVHelpController {
	
	@FXML
	private Button ok;
	@FXML
	private GridPane scenePane;
	
	private Stage stage;

	public void close(ActionEvent event) {
		stage = (Stage) scenePane.getScene().getWindow();
		stage.close(); 
	}
}
