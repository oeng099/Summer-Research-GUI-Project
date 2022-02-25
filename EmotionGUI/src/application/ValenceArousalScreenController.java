package application;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;



public abstract class ValenceArousalScreenController extends Controller implements Initializable {
	
	@FXML
	protected ScatterChart<Number, Number> ValenceArousalPlot;
	@FXML
	protected Button mainMenu;
	@FXML
	protected Text coordinateDetail;

	// Sets up the landmark emotions with their respective valence and arousal
	// values
	ArrayList<String> landmarkEmotions = new ArrayList<String>(Arrays.asList("angry", "afraid", "sad", "bored",
			"excited", "interested", "happy", "pleased", "relaxed", "content"));
	double landmarkValence[] = { -0.7, -0.65, -0.8, -0.1, 0.37, 0.2, 0.5, 0.35, 0.6, 0.5 };
	double landmarkArousal[] = { 0.65, 0.5, -0.15, -0.45, 0.9, 0.7, 0.5, 0.35, -0.3, -0.45 };

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

	//Method to initialise the landmark emotions on the valence-arousal plot
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
	
	//Method to display the valence and arousal coordinate of the mouse position when it is over the valence-arousal plot
	public void showMouseCoordinates() {
		//Set up the model edge coordinates
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
				//Checks if the mouse is currently on the model
				if ((event.getX() >= modelLeftCoordinate && event.getX() <= modelRightCoordinate)
						&& (event.getY() >= modelTopCoordinate && event.getY() <= modelBottomCoordinate)) {

					//Converts the mouse coordinate to produce valence and arousal coordinates
					double valence = convertCoordinate(event.getX(),valenceSlope,valenceConstant);
					double arousal = convertCoordinate(event.getY(),arousalSlope,arousalConstant);

					//Set coordinateDetail to display the valence-arousal coordinates
					String coordinates = "Valence: " + valence + " Arousal: " + arousal;
					coordinateDetail.setText(coordinates);
				} else {
					coordinateDetail.setText("Point: ");
				}
			}
		});
	}
	
	//Method to convert the mouse coordinate to a valence or arousal coordinate
	public double convertCoordinate(double position, double slope, double constant) {
		//Calculate the coordinate on the model from the mouse coordinate
		double coordinateConverted = position * slope + constant;
		//Round the coordinate to 2 d.p.
		BigDecimal roundedCoordinateConverted = new BigDecimal(coordinateConverted).setScale(2,
				RoundingMode.HALF_UP);
		return roundedCoordinateConverted.doubleValue();
	}
	
	//Method to return to the home screen
	public void returnToMainMenu(ActionEvent event) throws IOException {
		changeScreen(event,Screen.HOME);
	}
}
