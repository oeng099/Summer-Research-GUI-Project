package application;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AnnotationScreenController implements Initializable{
	
	@FXML
	ScatterChart<Number, Number> ValenceArousalPlot;
	@FXML
	Button selectMedia;
	@FXML
	Button playMedia;
	@FXML
	MediaView audioVisual;
	@FXML
	Label timeLabel;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private Media media;
	private MediaPlayer player;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		XYChart.Series<Number, Number> initialSeries  = new XYChart.Series<Number, Number>();
		Circle circle = new Circle(0,0,ValenceArousalPlot.getXAxis().getPrefWidth()/2);
		circle.setFill(new Color(0,0,0,0));
		circle.setStroke(Color.BLACK);
		Data<Number, Number> data = new Data<Number, Number>(0,0);
		data.setNode(circle);
		initialSeries.getData().add(data);
		initialSeries.setName("Landmark Emotions");
		
		
		ArrayList<String> landmarkEmotions = new ArrayList<String>(Arrays.asList("angry","afraid","sad","bored","excited","interested","happy","pleased","relaxed","content"));
		String[] landmarkValence = {"-0.7", "-0.65", "-0.8","-0.1","0.37","0.2","0.5","0.35","0.6","0.5"};
		String[] landmarkArousal = {"0.65", "0.5", "-0.15","-0.45","0.9","0.7","0.5","0.35","-0.3","-0.45"};
		
		for(int i = 0;i<landmarkEmotions.size();i++) {
			Data<Number, Number> landmarkData = new Data<Number, Number>(Double.parseDouble(landmarkValence[i]),Double.parseDouble(landmarkArousal[i]));
			
			Label label = new Label(landmarkEmotions.get(i));
			
			landmarkData.setNode(label);
			
			initialSeries.getData().add(landmarkData);
		}
		
		ValenceArousalPlot.getData().add(initialSeries);
		
	}
	
	public void selectMediaFile(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();

		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			media = new Media(file.toURI().toString());
			player = new MediaPlayer(media);
			audioVisual.setMediaPlayer(player);
			
		}
	}
	
	public void playMediaFile(ActionEvent event) {
		player.play();
		player.currentTimeProperty().addListener((observable,oldTime,newTime) -> timeLabel.setText(formatTime(newTime,player.getTotalDuration())));
	}
	
	public String formatTime(Duration currentTime, Duration totalTime) {
		int secondsElapsed = (int) Math.floor(currentTime.toSeconds());
		int hoursElapsed = secondsElapsed/360;
		if(hoursElapsed > 0) {
			secondsElapsed -= hoursElapsed*360;
		}
		int minutesElapsed = secondsElapsed/60;
		if(minutesElapsed > 0) {
			secondsElapsed -= minutesElapsed*60;
		}
		
		int totalSeconds = (int) Math.floor(totalTime.toSeconds());
		int totalHours = totalSeconds/360;
		if(totalHours > 0) {
			totalSeconds -= totalHours*360;
		}
		int totalMinutes = totalSeconds/60;
		if(totalMinutes > 0) {
			totalSeconds -= totalMinutes*60;
		}
		
		return String.format("%d:%02d:%02d/%d:%02d:%02d",hoursElapsed,minutesElapsed,secondsElapsed,
				totalHours,totalMinutes,totalSeconds);	
	}

}