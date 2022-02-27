package application;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class VisualiseScreenController extends ValenceArousalScreenController{

	@FXML
	Button selectCSVFileButton ;
	@FXML
	Button selectWAVFileButton;
	@FXML
	Button plotCSVFileButton;
	@FXML
	Button plotWAVFileButton;
	@FXML
	Button plotCoordinates;
	@FXML
	Button saveCoordinates;
	@FXML
	Button saveImage;
	@FXML
	Button clearChart;
	@FXML
	Button CSVInfo;
	@FXML
	TextField CSVFilename;
	@FXML
	TextField WAVFilename;
	@FXML
	TextField valenceCoordinate;
	@FXML
	TextField arousalCoordinate;
	@FXML
	Text manualPlotError;
	@FXML
	Text valenceText;
	@FXML
	Text arousalText;
	@FXML
	Text manualPlotText;
	@FXML
	Text CSVFileText;
	@FXML
	Text instructionText;
	@FXML
	Text WAVFileText;
	@FXML
	Text selectAFileText;



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		//Sets the image of the CSVInfo button to an info icon
		initialiseInfoIcon();
		
		//Adds the initial features to the Valence-Arousal Plot
		initialiseModel();

	}
	
	//Method to load the infoIcon image to the CSVInfo button
	public void initialiseInfoIcon() {
		//Loads the info icon image
		URL imageURL = getClass().getResource("images/infoIcon.png");
		Image image = new Image(imageURL.toExternalForm());
		ImageView view = new ImageView(image);
		
		//Adjusts size of the image to fit the button
		view.setFitHeight(30);
		view.setPreserveRatio(true);
		
		CSVInfo.setGraphic(view);

	}
	
	//Method to select a CSV file and display it on screen
	public void selectCSVFile(ActionEvent event) {

		//Sets up a file chooser to only show CSV files then allows user to select a file
		FileChooser CSVFileChooser = setUpFileChooser("CSV files",new String[] {"*.csv"});
		File CSVFile = openFile(CSVFileChooser);
		//If the file has the correct extension, set CSVFilename textfield to its name
		if (isCorrectFileType(CSVFile.toString(),"csv")) {
			CSVFilename.setText(CSVFile.toString());
		}
	}
	
	//Method to open the CSV Help screen.
	public void openCSVHelp(ActionEvent event) throws IOException {
		changeScreen(event,Screen.CSVHELP);
	}

	
	@SuppressWarnings("unchecked")
	public void plotCSVFile(ActionEvent event) throws CsvValidationException, IOException {

		File file = new File(CSVFilename.getText());
		CSVReader reader = new CSVReaderBuilder(new FileReader(file)).withSkipLines(1).build();
		String[] lineInFile;
		int numNodes = 0;

		XYChart.Series<Number, Number> emotionCoordinates = new XYChart.Series<Number, Number>();
		ArrayList<Double> currentSeriesTime = new ArrayList<Double>();

		while ((lineInFile = reader.readNext()) != null) {
			currentSeriesTime.add(Double.parseDouble(lineInFile[0]));
			double unroundedValence = Double.parseDouble(lineInFile[1]);
			double unroundedArousal = Double.parseDouble(lineInFile[2]);
			BigDecimal roundedValence = new BigDecimal(unroundedValence).setScale(2, RoundingMode.HALF_UP);
			BigDecimal roundedArousal = new BigDecimal(unroundedArousal).setScale(2, RoundingMode.HALF_UP);
			double valence = roundedValence.doubleValue();
			double arousal = roundedArousal.doubleValue();
			XYChart.Data<Number, Number> data = new XYChart.Data<Number, Number>(valence, arousal);
			data.setNode(new HoverNode(Double.toString(valence), Double.toString(arousal), coordinateDetail));
			emotionCoordinates.getData().add(data);
			numNodes++;
		}
		calculateDifferences(numNodes, numSeries);
		emotionCoordinates.setName("Emotion Points" + numSeries);
		manualPlotError.setText(" ");
		ValenceArousalPlot.getData().addAll(emotionCoordinates);
		colourNodes(ValenceArousalPlot, numSeries);
		annotationTimes.add(currentSeriesTime);
		numSeries++;
	}

	@SuppressWarnings("unchecked")
	public void plotCSVFile(String CSVFile) throws CsvValidationException, IOException {

		File file = new File(CSVFile);
		CSVReader reader = new CSVReaderBuilder(new FileReader(file)).withSkipLines(1).build();
		String[] lineInFile;
		int numNodes = 0;

		XYChart.Series<Number, Number> emotionCoordinates = new XYChart.Series<Number, Number>();
		ArrayList<Double> currentSeriesTime = new ArrayList<Double>();

		while ((lineInFile = reader.readNext()) != null) {
			currentSeriesTime.add(Double.parseDouble(lineInFile[0]));
			double unroundedValence = Double.parseDouble(lineInFile[1]);
			double unroundedArousal = Double.parseDouble(lineInFile[2]);
			BigDecimal roundedValence = new BigDecimal(unroundedValence).setScale(2, RoundingMode.HALF_UP);
			BigDecimal roundedArousal = new BigDecimal(unroundedArousal).setScale(2, RoundingMode.HALF_UP);
			double valence = roundedValence.doubleValue();
			double arousal = roundedArousal.doubleValue();
			XYChart.Data<Number, Number> data = new XYChart.Data<Number, Number>(valence, arousal);
			data.setNode(new HoverNode(Double.toString(valence), Double.toString(arousal), coordinateDetail));
			emotionCoordinates.getData().add(data);
			numNodes++;
		}
		calculateDifferences(numNodes, numSeries);
		emotionCoordinates.setName("Emotion Points" + numSeries);
		manualPlotError.setText(" ");
		ValenceArousalPlot.getData().addAll(emotionCoordinates);
		colourNodes(ValenceArousalPlot, numSeries);
		annotationTimes.add(currentSeriesTime);
		numSeries++;
	}





	@SuppressWarnings("unchecked")
	public void plotManual(ActionEvent event) throws IOException {

		boolean hasManual = false;
		int hasManualIndex = -1;
		int currentIndex = 0;
		XYChart.Series<Number, Number> emotionCoordinates;
		for (XYChart.Series<Number, Number> ValenceArousalSeries : ValenceArousalPlot.getData()) {
			if (ValenceArousalSeries.getName().equals("Manual Points")) {
				hasManual = true;
				hasManualIndex = currentIndex;
				break;
			}
			currentIndex++;
		}

		if (hasManual) {
			emotionCoordinates = ValenceArousalPlot.getData().get(currentIndex);
		} else {
			emotionCoordinates = new XYChart.Series<Number, Number>();
		}

		if (valenceCoordinate.getText().isEmpty() || arousalCoordinate.getText().isEmpty()) {
			manualPlotError.setText("Please enter valid numbers for both valence and arousal from -1.0 to 1.0");
		} else if (!(Double.parseDouble(valenceCoordinate.getText()) < 1.0
				&& Double.parseDouble(valenceCoordinate.getText()) > -1.0)
				|| !(Double.parseDouble(arousalCoordinate.getText()) < 1.0
						&& Double.parseDouble(arousalCoordinate.getText()) > -1.0)) {
			manualPlotError.setText("Please enter valid numbers for both valence and arousal from -1.0 to 1.0");
		} else {
			emotionCoordinates.setName("Manual Points");
			manualPlotError.setText(" ");
			double valence = Double.parseDouble(valenceCoordinate.getText());
			double arousal = Double.parseDouble(arousalCoordinate.getText());
			XYChart.Data<Number, Number> data = new XYChart.Data<Number, Number>(valence, arousal);
			data.setNode(new HoverNode(valenceCoordinate.getText(), arousalCoordinate.getText(), coordinateDetail));
			emotionCoordinates.getData().add(data);

			if (hasManual) {
				ValenceArousalPlot.getData().remove(hasManualIndex);
				ValenceArousalPlot.getData().addAll(emotionCoordinates);
			} else {
				ValenceArousalPlot.getData().addAll(emotionCoordinates);
				numSeries++;
			}
		}
	}

	public void clearModel(ActionEvent event) {
		if (ValenceArousalPlot.getData().size() > 1) {
			ValenceArousalPlot.getData().remove(1, ValenceArousalPlot.getData().size());
			annotationTimes.removeAll(annotationTimes);
			numSeries = 1;
		}
	}

	//Method to get the predicted emotional output of a WAV file from a machine-learning models to a CSV file
	public void WAV_TO_CSV(String WAVFile) throws IOException, InterruptedException {
		PythonScriptManager pythonScriptManager = new PythonScriptManager("src/application/group108demo.py");
		pythonScriptManager.changePythonScript("scriptInput =","scriptInput = r'" + WAVFile + "'");
		pythonScriptManager.runScript("group108demo.py");
	}

	//Method to select a WAV file and display it on the screen
	public void selectWAVFile(ActionEvent event) {

		//Sets up a file chooser to only show WAV files then allows user to select a file
		FileChooser WAVFileChooser = setUpFileChooser("WAVfiles",new String[] {"*.wav"});
		File WAVFile = openFile(WAVFileChooser);
		
		if (WAVFile != null) {
			// If the file has the correct extension, set WAVFilename textfield to its name
			if (isCorrectFileType(WAVFile.toString(), "wav")) {
				WAVFilename.setText(WAVFile.toString());
			}
		}
	}
	
	//Method to plot the emotional data of a selected WAV file onto the model
	public void plotWAVFile(ActionEvent event) throws IOException, CsvValidationException, InterruptedException {
		
		//If the WAVFilename textfield is empty, run selectWAVFile method
		if(WAVFilename.getText().isEmpty()) {
			selectWAVFileButton.fire();
		}
		File WAVFile = new File(WAVFilename.getText());
		//Creates a CSV file that contains the predicted emtional output of the WAV file from the machine-learning models
		WAV_TO_CSV(WAVFile.toString());
		//Gets the name of the CSV file created
		String CSVFile = WAVFile.getName().replace(".wav", ".csv");
		//Plots the CSV file generated onto the model
		plotCSVFile("src/application/WAV_To_CSV/CSV_Outputs/" + CSVFile);
	}


	//Method to change to the screen to change machine-learning models it is trained on
	public void changeMachineLearningModels(ActionEvent event) throws IOException {
		changeScreen(event,Screen.MODELCHOOSE);
	}

}