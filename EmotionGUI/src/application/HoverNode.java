package application;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class HoverNode extends StackPane {

	// Constructor of the HoverNode class, takes inputs of valence and arousal
	// coordinates
	public HoverNode(String valenceCoordinate, String arousalCoordinate) {

		// Creates the label that will appear when hovered over
		Label label = createDataLabel(valenceCoordinate, arousalCoordinate);

		// When the mouse enters a point it will display the label
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				getChildren().setAll(label);
				setCursor(Cursor.NONE);
				toFront();
			}
		});

		// When the mouse exits a point, its label will disappear
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				getChildren().clear();
				setCursor(Cursor.CROSSHAIR);
			}
		});
	}

	//Method to create a label 
	public Label createDataLabel(String valenceCoordinate, String arousalCoordinate) {
		//Creates the label with valence and arousal coordinates
		Label dataLabel = new Label(valenceCoordinate + "," + arousalCoordinate);
		//Sets the style for the label
		dataLabel.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
		dataLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-font-family: Montserrat SemiBold");
		dataLabel.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);

		return dataLabel;
	}

}
