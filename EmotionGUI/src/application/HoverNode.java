package application;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
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
		
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				coordinateDetail.setText("Point: " + valenceCoordinate + "," + arousalCoordinate + " (" + emotion + ")");
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
}
