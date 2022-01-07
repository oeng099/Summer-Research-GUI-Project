package application;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class HoverNode extends StackPane {

	public HoverNode(String valenceCoordinate, String arousalCoordinate,Text coordinateDetail) {
		
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				coordinateDetail.setText("Point: " + valenceCoordinate + "," + arousalCoordinate);
				setCursor(Cursor.NONE);
				toFront();
			}
		});

		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				coordinateDetail.setText("Point:");
				setCursor(Cursor.CROSSHAIR);
			}
		});
	}
	
public HoverNode(String valenceCoordinate, String arousalCoordinate,Text coordinateDetail,String emotion) {
		
	Label label = createDataLabel(valenceCoordinate,arousalCoordinate,emotion);
	
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				getChildren().setAll(label);
				coordinateDetail.setText("Point: " + valenceCoordinate + "," + arousalCoordinate + " (" + emotion + ")");
				setCursor(Cursor.NONE);
				toFront();
			}
		});

		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				getChildren().clear();
				coordinateDetail.setText("Point:");
				setCursor(Cursor.CROSSHAIR);
			}
		});
	}

public Label createDataLabel(String valenceCoordinate, String arousalCoordinate, String emotion) {
	Label dataLabel = new Label(valenceCoordinate + "," + arousalCoordinate + " (" + emotion + ")");
	dataLabel.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
    dataLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-font-family: Montserrat SemiBold");
    
    dataLabel.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
    
	return dataLabel;
}

}