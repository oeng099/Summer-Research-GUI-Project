package application;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
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

	private int numSeries = 1;

	private double[] endR = new double[] { 251, 153, 9, 234 };
	private double[] endG = new double[] { 20, 34, 18, 115 };
	private double[] endB = new double[] { 20, 195, 121, 141 };

	private double[] startR = new double[] { 23, 253, 255, 137 };
	private double[] startG = new double[] { 255, 231, 146, 227 };
	private double[] startB = new double[] { 101, 45, 0, 181 };

	private double differenceR;
	private double differenceG;
	private double differenceB;
	
	private ArrayList<ArrayList<Double>> annotationTimes = new ArrayList<ArrayList<Double>>();


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

		File csvFile = selectAFile();
		//If the file has the correct extension, set CSVFilename textfield to its name
		if (isCorrectFileType(csvFile.toString(),"csv")) {
			CSVFilename.setText(csvFile.toString());
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

	public void calculateDifferences(int numNodes, int numSeries) {
		int currentSeries = numSeries - 1;
		this.differenceR = (endR[currentSeries] - startR[currentSeries]) / numNodes;
		this.differenceG = (endG[currentSeries] - startG[currentSeries]) / numNodes;
		this.differenceB = (endB[currentSeries] - startB[currentSeries]) / numNodes;
	}

	public void colourNodes(XYChart<Number, Number> ValenceArousalPlot, int numSeries) {
		Set<Node> nodes = ValenceArousalPlot.lookupAll(".series" + numSeries);

		int currentSeries = numSeries - 1;
		double currentR = startR[currentSeries];
		double currentG = startG[currentSeries];
		double currentB = startB[currentSeries];

		for (Node n : nodes) {
			n.setStyle("-fx-background-color: rgb(" + currentR + "," + currentG + "," + currentB + ")");
			currentR += differenceR;
			currentG += differenceG;
			currentB += differenceB;
		}
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

	public void saveAsPNG(ActionEvent event) {
		WritableImage image = ValenceArousalPlot.snapshot(new SnapshotParameters(), null);
		
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png","*.png");
		fileChooser.getExtensionFilters().add(extFilter);
		
		File file = fileChooser.showSaveDialog(stage);
		
		try {
			BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
			ImageIO.write(bufferedImage,"png",file);
		} catch (IOException e) {
			System.out.println("Scatter plot cannot be converted.");
		}
	}
	
	public void saveAsCSV(ActionEvent event) throws IOException {
		
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv","*.csv");
		fileChooser.getExtensionFilters().add(extFilter);
		
		File file = fileChooser.showSaveDialog(stage);
		String csv = file.toString();
		CSVWriter writer = new CSVWriter(new FileWriter(csv));
		
		String [] record = "Time,Valence,Arousal".split(",");
		writer.writeNext(record);
		XYChart.Series<Number, Number> emotionCoordinates = ValenceArousalPlot.getData().get(1);
		
		for(int i = 0; i < emotionCoordinates.getData().size(); i++) {
			Number time = annotationTimes.get(0).get(i);
			Number valence = emotionCoordinates.getData().get(i).getXValue();
			Number  arousal = emotionCoordinates.getData().get(i).getYValue();
			String[] coordinates = new String[3];
			coordinates[0] = time.toString();
			coordinates[1] = valence.toString();
			coordinates[2] = arousal.toString();
			writer.writeNext(coordinates);
		}
		writer.close();
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

		File WAVFile = selectAFile();
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