package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import javax.imageio.ImageIO;

import com.opencsv.CSVWriter;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public abstract class ValenceArousalScreenController extends Controller implements Initializable {

	@FXML
	protected ScatterChart<Number, Number> ValenceArousalPlot;
	@FXML
	protected Button mainMenu;
	@FXML
	protected Text coordinateDetail;

	// Sets up the landmark emotions with their respective valence and arousal
	// values
	protected ArrayList<String> landmarkEmotions = new ArrayList<String>(Arrays.asList("angry", "afraid", "sad",
			"bored", "excited", "interested", "happy", "pleased", "relaxed", "content"));
	protected double landmarkValence[] = { -0.7, -0.65, -0.8, -0.1, 0.37, 0.2, 0.5, 0.35, 0.6, 0.5 };
	protected double landmarkArousal[] = { 0.65, 0.5, -0.15, -0.45, 0.9, 0.7, 0.5, 0.35, -0.3, -0.45 };
	
	protected ArrayList<ArrayList<Double>> annotationTimes = new ArrayList<ArrayList<Double>>();
	
	protected int numSeries = 0;

	protected double[] endR = new double[] { 251, 153, 9, 234 };
	protected double[] endG = new double[] { 20, 34, 18, 115 };
	protected double[] endB = new double[] { 20, 195, 121, 141 };

	protected double[] startR = new double[] { 23, 253, 255, 137 };
	protected double[] startG = new double[] { 255, 231, 146, 227 };
	protected double[] startB = new double[] { 101, 45, 0, 181 };

	protected double differenceR;
	protected double differenceG;
	protected double differenceB;

	// Method to set up the initial features on the Valence-Arousal Plot model
	public void initialiseModel() {
		// initalSeries is used as the series to plot the points of the initial features
		XYChart.Series<Number, Number> initialSeries = new XYChart.Series<Number, Number>();
		// Plots the circle in the model
		Data<Number, Number> circleCentre = new Data<Number, Number>(0, 0);
		Data<Number, Number> circle = plotCircle(circleCentre);
		initialSeries.getData().add(circle);
		// Sets up the landmark emotion points in the model
		initialSeries = initialiseLandmarkEmotions(initialSeries);
		initialSeries.setName("");

		ValenceArousalPlot.getData().add(initialSeries);
		this.numSeries++;
	}

	// Method to plot the centre circle on the model
	public Data<Number, Number> plotCircle(Data<Number, Number> circleCentre) {
		// Creates the circle with its size slightly smaller than the length of the
		// x-axis of the model
		Circle circle = new Circle(0, 0, (ValenceArousalPlot.getXAxis().getPrefWidth() - 2) / 2);
		// Sets the colour inside the circle to be transparent
		circle.setFill(new Color(0, 0, 0, 0));
		// Sets the colour of the circle line to be black
		circle.setStroke(Color.BLACK);
		circleCentre.setNode(circle);
		return circleCentre;
	}

	// Method to initialise the landmark emotions on the valence-arousal plot
	public XYChart.Series<Number, Number> initialiseLandmarkEmotions(XYChart.Series<Number, Number> series) {

		// Iterates through the landmark emotions and add all to the series
		for (int i = 0; i < landmarkEmotions.size(); i++) {
			// Gets the valence and arousal values of a corresponding emotion
			Data<Number, Number> landmarkData = new Data<Number, Number>(landmarkValence[i], landmarkArousal[i]);
			// Sets a label with the name of the emotion that is represented in landmarkData
			Label emotion = new Label(landmarkEmotions.get(i));
			landmarkData.setNode(emotion);
			series.getData().add(landmarkData);
		}

		return series;
	}

	// Method to display the valence and arousal coordinate of the mouse position
	// when it is over the valence-arousal plot
	public void showMouseCoordinates() {
		// Set up the model edge coordinates
		double modelLeftCoordinate = 47.0;
		double modelRightCoordinate = 660.0;
		double modelTopCoordinate = 39.0;
		double modelBottomCoordinate = 658.0;

		double valenceSlope = 2.0 / 619.0;
		double valenceConstant = -713.0 / 619.0;

		double arousalSlope = -2.0 / 619.0;
		double arousalConstant = 697.0 / 619.0;

		ValenceArousalPlot.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Checks if the mouse is currently on the model
				if ((event.getX() >= modelLeftCoordinate && event.getX() <= modelRightCoordinate)
						&& (event.getY() >= modelTopCoordinate && event.getY() <= modelBottomCoordinate)) {

					// Converts the mouse coordinate to produce valence and arousal coordinates
					double valence = convertCoordinate(event.getX(), valenceSlope, valenceConstant);
					double arousal = convertCoordinate(event.getY(), arousalSlope, arousalConstant);

					// Set coordinateDetail to display the valence-arousal coordinates
					String coordinates = "Valence: " + valence + " Arousal: " + arousal;
					coordinateDetail.setText(coordinates);
				} else {
					coordinateDetail.setText("Point: ");
				}
			}
		});
	}

	// Method to convert the mouse coordinate to a valence or arousal coordinate
	public double convertCoordinate(double position, double slope, double constant) {
		// Calculate the coordinate on the model from the mouse coordinate
		double coordinateConverted = position * slope + constant;
		// Round the coordinate to 2 d.p.
		BigDecimal roundedCoordinateConverted = new BigDecimal(coordinateConverted).setScale(2, RoundingMode.HALF_UP);
		return roundedCoordinateConverted.doubleValue();
	}

	// Method to set up a fileChooser to select a file with the correct extension
	public FileChooser setUpFileChooser(String description, String[] extension) {
		FileChooser fileChooser = new FileChooser();
		// Adds file filters to the fileChooser
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extension);
		fileChooser.getExtensionFilters().add(extFilter);
		return fileChooser;
	}

	// Method to allow users to select a file
	public File openFile(FileChooser fileChooser) {
		File file = fileChooser.showOpenDialog(stage);
		return file;
	}

	// Method to allow users to save a new file
	public File saveFile(FileChooser fileChooser) {
		File file = fileChooser.showSaveDialog(stage);
		return file;
	}

	// Method to check if a file has the correct extension
	public boolean isCorrectFileType(String filename, String extension) {
		String filenameExtension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
		if (filenameExtension.equals(extension)) {
			return true;
		} else {
			return false;
		}
	}

	//Method to save the current state of the Valence-Arousal plot as a png image
	public void saveAsPNG(ActionEvent event) {
		//Sets image variable to a snapshot of the Valence-Arousal plot
		WritableImage image = ValenceArousalPlot.snapshot(new SnapshotParameters(), null);

		//Sets up a file chooser to only show PNG files and then allows the user to write a filename to save the image as
		FileChooser pngFileChooser = setUpFileChooser("PNG files", new String[] { "*.png" });
		File pngFile = saveFile(pngFileChooser);

		try {
			BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
			//Write the image to the file
			ImageIO.write(bufferedImage, "png", pngFile);
		} catch (IOException e) {
			System.out.println("Valence-Arousal plot cannot be saved as an image.");
		}
	}
	
	// Method to save the data on the Valence-Arousal model into a CSV file
	public void saveAsCSV(ActionEvent event) throws IOException {

		// Sets up a file chooser to only show CSV files and then allows the user to
		// name the CSV file the model data will be saved to
		FileChooser CSVFileChooser = setUpFileChooser("CSV files", new String[] { "*.csv" });
		File CSVFile = saveFile(CSVFileChooser);

		// Set up the writer to the CSV file and the header for each series
		CSVWriter writer = new CSVWriter(new FileWriter(CSVFile));
		String[] headers = "Time,Valence,Arousal".split(",");

		// Get the total number of series in the model, will be saving the
		// ValenceArousal Plot data from its index 1
		// to its last index (as index 0 is initialSeries)
		int numEmotionSeries = ValenceArousalPlot.getData().size();

		//Iterates through all emotion series on the Valence-Arousal plot 
		for (int i = 1; i < numEmotionSeries; i++) {
			writer.writeNext(headers);
			//Gets the current series valence and arousal data and corresponding annotation time
			XYChart.Series<Number, Number> emotionCoordinates = ValenceArousalPlot.getData().get(i);
			ArrayList<Double> currentSeriesTime = annotationTimes.get(i - 1);
			//Writes the current series to the CSV file
			writeSeriesToCSV(writer,emotionCoordinates,currentSeriesTime);
		}
		writer.close();
	}
	
	//Method to write the emotional data of a series and it corresponding time to a CSV file
	public void writeSeriesToCSV(CSVWriter writer,XYChart.Series<Number, Number> series,ArrayList<Double> times) {
		//Iterates through all of the series
		for (int j = 0; j < series.getData().size(); j++) {
			//Get the time,valence and arousal values of a point
			Number time = times.get(j);
			Number valence = series.getData().get(j).getXValue();
			Number arousal = series.getData().get(j).getYValue();
			//Write the values to the CSV file
			String[] coordinates = new String[] { time.toString(), valence.toString(), arousal.toString() };
			writer.writeNext(coordinates);
		}
	}
	
	//Method to calculate the increment changes for each of the rgb colour values. 
	public void calculateDifferences(int numNodes, int numSeries) {
		//Gets the index of the next colour gradation that has not yet been used
		int currentSeries = numSeries - 1;
		//Calculates the differences for each value by finding the overall difference between the end and start
		//colour values and dividing by the total number of nodes
		this.differenceR = (endR[currentSeries] - startR[currentSeries]) / numNodes;
		this.differenceG = (endG[currentSeries] - startG[currentSeries]) / numNodes;
		this.differenceB = (endB[currentSeries] - startB[currentSeries]) / numNodes;
	}

	//Method to set colour gradation to a series plotted on the Valence-Arousal model
	public void colourNodes(XYChart<Number, Number> ValenceArousalPlot, int numSeries) {
		//Gets the nodes from the latest series plotted
		Set<Node> nodes = ValenceArousalPlot.lookupAll(".series" + numSeries);

		//Sets up the colours for the first node
		int currentSeries = numSeries - 1;
		double currentR = startR[currentSeries];
		double currentG = startG[currentSeries];
		double currentB = startB[currentSeries];

		//Iterates through all nodes and adds and a small difference value to each colour value each iteration
		//to have the next node be a slightly different colour
		for (Node n : nodes) {
			n.setStyle("-fx-background-color: rgb(" + currentR + "," + currentG + "," + currentB + ")");
			currentR += differenceR;
			currentG += differenceG;
			currentB += differenceB;
		}
	}
	
	public abstract void clearModel(ActionEvent event);


	// Method to return to the home screen
	public void returnToMainMenu(ActionEvent event) throws IOException {
		changeScreen(event, Screen.HOME);
	}

}
