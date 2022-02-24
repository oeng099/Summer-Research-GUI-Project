package application;

import java.util.ArrayList;
import java.util.Arrays;

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

	
	public XYChart.Series<Number, Number> initialiseLandmarkEmotions(XYChart.Series<Number, Number> series) {

		// Iterates through the landmark emotions and add all to the series
		for (int i = 0; i < landmarkEmotions.size(); i++) {
			// Gets the valence and arousal values of a corresponding emotion
			Data<Number, Number> landmarkData = new Data<Number, Number>(landmarkValence[i], landmarkArousal[i]);
			// Sets a label with the name of the emotion that is represented in landmarkData
			Label emotion = new Label(landmarkEmotions.get(i));
			landmarkData.setNode(emotion);
			
			emotion.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					int emotionIndexNumber = landmarkEmotions.indexOf(emotion.getText());
					coordinateDetail.setText(
							"Point: " + landmarkValence[emotionIndexNumber] + "," + landmarkArousal[emotionIndexNumber]
									+ " (" + landmarkEmotions.get(emotionIndexNumber) + ")");
				}
			});

			emotion.setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					coordinateDetail.setText("Point:");
				}
			});
			
			//emotion = showEmotionDetailsWhenHover(emotion);
			series.getData().add(landmarkData);
		}

		return series;
	}
	
	public Label showEmotionDetailsWhenHover(Label emotion) {
	
	return emotion;
	}
	

	
}
