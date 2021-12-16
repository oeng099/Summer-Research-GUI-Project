package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
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
	Button saveCoordinates;
	@FXML
	Button clear;
	@FXML
	TextField fileName;
	@FXML
	TextField valenceCoordinate;
	@FXML
	TextField arousalCoordinate;
	@FXML
	Text manualPlotError;
	@FXML
	BorderPane borderPane;
	@FXML
	ScatterChart<Number, Number> ValenceArousalPlot;

	XYChart.Series<Number, Number> emotionCoordinates = new XYChart.Series<Number, Number>();

	private Stage stage;
	private Scene scene;
	private Parent root;

	public void selectAFile(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();

		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			fileName.setText(file.toString());
		}
	}

	public void returnToMainMenu(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("fxml/HomeScreen.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root, borderPane.getWidth(), borderPane.getHeight());
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void plotManual(ActionEvent event) throws IOException {
		if (valenceCoordinate.getText().isEmpty() || arousalCoordinate.getText().isEmpty()) {
			manualPlotError.setText("Please enter valid numbers for both valence and arousal from -1 to 1");
		} else if (!(Double.parseDouble(valenceCoordinate.getText()) < 1.0
				&& Double.parseDouble(valenceCoordinate.getText()) > -1.0)
				|| !(Double.parseDouble(arousalCoordinate.getText()) < 1.0
						&& Double.parseDouble(arousalCoordinate.getText()) > -1.0)) {
			manualPlotError.setText("Please enter valid numbers for both valence and arousal from -1 to 1");
		} else {
			manualPlotError.setText(" ");
			double valence = Double.parseDouble(valenceCoordinate.getText());
			double arousal = Double.parseDouble(arousalCoordinate.getText());
			emotionCoordinates.getData().add(new Data<Number, Number>(valence, arousal));
			if (ValenceArousalPlot.getData().isEmpty()) {
				ValenceArousalPlot.getData().addAll(emotionCoordinates);
			} else {
				ValenceArousalPlot.getData().remove((int) (Math.random() * (ValenceArousalPlot.getData().size() - 1)));
				ValenceArousalPlot.getData().addAll(emotionCoordinates);
			}
		}
	}
	
	public void clearModel(ActionEvent event) {
		emotionCoordinates.getData().clear();
		ValenceArousalPlot.getData().remove((int) (Math.random() * (ValenceArousalPlot.getData().size() - 1)));
	}
}
