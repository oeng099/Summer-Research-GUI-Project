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

	//Method to plot the emotional data of a CSV file on the model
	public void plotCSVFile(ActionEvent event) throws CsvValidationException, IOException {
		
		//Set up the CSV reader with the CSV file to plot. Skip first line as it contains headers
		File file = new File(CSVFilename.getText());
		CSVReader reader = new CSVReaderBuilder(new FileReader(file)).withSkipLines(1).build();
		
		String[] lineInFile;
		int numNodes = 0;

		//Initialise a series to contain valence and arousal values and a corresponding arraylist for time values
		XYChart.Series<Number, Number> emotionCoordinates = new XYChart.Series<Number, Number>();
		ArrayList<Double> currentSeriesTime = new ArrayList<Double>();

		//Iterates through the entire CSV file 
		while ((lineInFile = reader.readNext()) != null) {
			//Extract the time, valence and arousal values from the file
			currentSeriesTime.add(Double.parseDouble(lineInFile[0]));
			double unroundedValence = Double.parseDouble(lineInFile[1]);
			double unroundedArousal = Double.parseDouble(lineInFile[2]);
			
			//Round the valence and arousal values extracted
			double valence = roundDoubleToTwoDP(unroundedValence);
			double arousal = roundDoubleToTwoDP(unroundedArousal);
			
			//Add the a node created using the valence and arousal values to the emotionCoordinates series
			emotionCoordinates = addNewNode(emotionCoordinates,valence,arousal);
			numNodes++;
		}
		//Once all of the CSV file has been read, plot the series and store the annotation times
		plotSeries(numNodes,emotionCoordinates,currentSeriesTime);
	}
	

	
	//Method to round a double to 2dp
	public double roundDoubleToTwoDP(double numToRound) {
		BigDecimal roundedNum = new BigDecimal(numToRound).setScale(2, RoundingMode.HALF_UP);
		return roundedNum.doubleValue();
	}
	
	//Method to plot a series on the model
	public void plotSeries(int numNodes,XYChart.Series<Number,Number> series,ArrayList<Double> timeValues) {
		//Sets the increments that each colour changes by as each node is plotted
		calculateDifferences(numNodes, numSeries);
		//Adds the nodes onto the model and colours them
		series.setName("Emotion Points" + numSeries);
		ValenceArousalPlot.getData().add(series);
		colourNodes(ValenceArousalPlot, numSeries);
		//Adds the corresponding time values of the series to the annotationTimes variable
		annotationTimes.add(timeValues);
		numSeries++;
	}

	// Method to plot points onto the model manually
	public void plotManual(ActionEvent event) throws IOException {

		// Checks if there is already a manual series in the Valence-Arousal model
		boolean hasManual;
		// Returns a positive int if a manual plot series already exists and -1 if it
		// does not
		int manualIndex = findManualIndex();
		if (manualIndex == -1) {
			hasManual = false;
		} else {
			hasManual = true;
		}

		// Sets up the emotionCoordinates variable depending on hasManual
		XYChart.Series<Number, Number> emotionCoordinates = setUpManualEmotionCoordinates(hasManual, manualIndex);

		// If the valence and arousal inputs are acceptable, the new node is added and then plotted onto the model
		if (hasAcceptableInputs()) {
			emotionCoordinates.setName("Manual Points");
			manualPlotError.setText("");
			double valence = Double.parseDouble(valenceCoordinate.getText());
			double arousal = Double.parseDouble(arousalCoordinate.getText());
			//Add the new node to the emotionCoordinates series
			emotionCoordinates = addNewNode(emotionCoordinates, valence, arousal);
			//Plot the manual series on the model
			plotManualSeries(hasManual, manualIndex, emotionCoordinates);
		} else {
			manualPlotError.setText("Please enter valid numbers for both valence and arousal from -1.0 to 1.0");
		}
	}
	
	//Method to plot the manual series depending on whether a manual series already exists in the model
	public void plotManualSeries(boolean hasManual, int manualIndex, XYChart.Series<Number, Number> emotionCoordinates) {
		if (hasManual) {
			//If manual plot series already exists, remove the existing series and plot the new one
			ValenceArousalPlot.getData().remove(manualIndex);
			ValenceArousalPlot.getData().add(emotionCoordinates);
		} else {
			//If it does not already exist plot the new series and add to the total number of series
			ValenceArousalPlot.getData().add(emotionCoordinates);
			numSeries++;
		}
	}
	
	//Method to set up the emotionCoordinates variable depending on whether a manual plot series already exists
	public XYChart.Series<Number, Number> setUpManualEmotionCoordinates (boolean hasManual, int manualIndex) {
		if (hasManual) {
			//returns the manual plot series if it exists
			return ValenceArousalPlot.getData().get(manualIndex);
		} else {
			return new XYChart.Series<Number, Number>();
		}
	}
	
	//Method to check if the inputs in manual coordinates textfield are acceptable to be plotted
	public boolean hasAcceptableInputs() {
		//Checks if both valence and arousal inputs are both between -1.0 to 1.0
		if (Double.parseDouble(valenceCoordinate.getText()) < 1.0
				&& Double.parseDouble(valenceCoordinate.getText()) > -1.0
				&& Double.parseDouble(arousalCoordinate.getText()) < 1.0
						&& Double.parseDouble(arousalCoordinate.getText()) > -1.0) {
			return true;
		} else {
			return false;
		}
	}
	
	//Method to return the index of the manual points series if it already exists in the Valence-Arousal model
	public int findManualIndex() {

		int manualIndex = 0;
		//Iterates through all series in the Valence-Arousal Model
		for (XYChart.Series<Number, Number> ValenceArousalSeries : ValenceArousalPlot.getData()) {
			//If manual plot series is found, its index in the ValenceArousalPlot variable is returned
			if (ValenceArousalSeries.getName().equals("Manual Points")) {
				return manualIndex;
			}
			manualIndex++;
		}
		//If not found, -1 is returned instead.
		return -1;
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
		//Creates a CSV file that contains the predicted emotional output of the WAV file from the machine-learning models
		WAV_TO_CSV(WAVFile.toString());
		//Gets the name of the CSV file created and sets it to the CSVFile name textfield
		String CSVFile = WAVFile.getName().replace(".wav", ".csv");
		CSVFilename.setText("src/application/WAV_To_CSV/CSV_Outputs/" + CSVFile);
		//Plots the CSV file generated onto the model
		plotCSVFileButton.fire();
		//Clears the textfield of the generated CSV file
		CSVFilename.clear();
	}
	
	//Method to clear the Valence-Arousal model
	@Override
	public void clearModel(ActionEvent event) {
		//Checks if there are emotional coordinates series
		if (ValenceArousalPlot.getData().size() > 1) {
			//Remove all series from index 1 to its size (does not remove 0 as that is initalSeries)
			ValenceArousalPlot.getData().remove(1, ValenceArousalPlot.getData().size());
			//Resets all annotationTimes
			annotationTimes.removeAll(annotationTimes);
			//Resets numSeries back to 1
			numSeries = 1;
		}
	}

	//Method to change to the screen to change machine-learning models it is trained on
	public void changeMachineLearningModels(ActionEvent event) throws IOException {
		changeScreen(event,Screen.MODELCHOOSE);
	}

}