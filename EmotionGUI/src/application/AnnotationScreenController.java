package application;

import java.awt.event.InputEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Set;

import javax.imageio.ImageIO;

import com.opencsv.CSVWriter;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
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
	@FXML
	Text coordinateDetail;
	@FXML
	Button forward;
	@FXML
	Button backward;
	@FXML
	Button mainMenu;
	@FXML
	BorderPane borderPane;
	@FXML 
	Slider speedSlider;
	@FXML 
	Text currentSpeed;
	@FXML
	HBox mediaControlBar;
	@FXML
	Text time;
	@FXML
	Slider timeSlider;
	@FXML
	Text volume;
	@FXML
	Slider volumeSlider;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private Media media;
	private MediaPlayer player;
	private AutoClicker autoclicker;
	private Thread runner;
	
	private ArrayList<Double> annotationTimes = new ArrayList<Double>();

	private double endR = 251;
	private double endG = 20;
	private double endB = 20;
	
	private double startR = 23;
	private double startG = 255;
	private double startB = 101;
	
	double differenceR;
	double differenceG;
	double differenceB;
	
	private int numNodes;
	private Duration totalTime;
	
	XYChart.Series<Number, Number> emotionCoordinates = new XYChart.Series<Number, Number>();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		timeSlider.valueProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable ov) {
				if(timeSlider.isValueChanging()) {
					player.seek(player.getTotalDuration().multiply(timeSlider.getValue()/100.0));
				}
				
			}
			
		});
		
		volumeSlider.valueProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable ov) {
				if(volumeSlider.isValueChanging()) {
					player.setVolume(volumeSlider.getValue()/100.0);
				}
				
			}
			
		});
		
		XYChart.Series<Number, Number> initialSeries  = new XYChart.Series<Number, Number>();
		Circle circle = new Circle(0,0,ValenceArousalPlot.getXAxis().getPrefWidth()/2);
		circle.setFill(new Color(0,0,0,0));
		circle.setStroke(Color.BLACK);
		Data<Number, Number> data = new Data<Number, Number>(0,0);
		data.setNode(circle);
		initialSeries.getData().add(data);
		
		
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
		
		speedSlider.setMaxWidth(400.0);
		speedSlider.valueProperty().addListener(new ChangeListener<Number>(){
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				currentSpeed.setText(String.format("Current Speed: %.2f ", newValue));
				player.setRate(newValue.doubleValue());
			}
			
		});
		
	}
	
	public void spacePressed() {
				if(player.getStatus() == MediaPlayer.Status.PLAYING) {
					player.pause();
					autoclicker.stopClicking();
				} else {
					player.play();
					autoclicker.startClicking();
				}
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
		
		this.numNodes = (int) Math.floor(player.getStopTime().toMillis()/500);
		this.differenceR = (endR - startR)/numNodes;
		this.differenceG = (endG - startG)/numNodes;
		this.differenceB = (endB - startB)/numNodes;
		
		this.totalTime = player.getTotalDuration();
		player.setVolume(volumeSlider.getValue()/100.0);
		player.currentTimeProperty().addListener((observable,oldTime,newTime) -> timeLabel.setText(formatTime(newTime,player.getTotalDuration())));
		startAutoClicker();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		player.play();
		updateTime();
		player.setOnEndOfMedia(() -> {
			autoclicker.stopClicking();
		});;
	}
	
	public void playPause(ActionEvent event) {
		if(player.getStatus() == MediaPlayer.Status.PLAYING) {
			player.pause();
			autoclicker.stopClicking();
			try {
				runner.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			player.play();
			autoclicker.startClicking();
		}
	}
	
	
	public void playfromStart(ActionEvent event) {
		player.seek(player.getStartTime());
	}
	
	public void goBackFiveSeconds(ActionEvent event) {
		player.seek(player.getCurrentTime().subtract(Duration.seconds(5)));
	}
	
	public void goForwardFiveSeconds(ActionEvent event) {
		player.seek(player.getCurrentTime().add(Duration.seconds(5)));
	}
	
	public void startAutoClicker() {
		this.autoclicker = new AutoClicker(InputEvent.BUTTON1_DOWN_MASK,player);
		autoclicker.startClicking();
	}
	
	public void updateTime() {
		player.currentTimeProperty().addListener(new ChangeListener<Duration>(){

			@Override
			public void changed(ObservableValue<? extends Duration> ov, Duration oldTime, Duration currentTime) {
				timeSlider.setValue(currentTime.toMillis()/totalTime.toMillis()*100.0);
			}
		
		});
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

	public void showMouseCoordinates() {
		ValenceArousalPlot.setOnMouseMoved(new EventHandler<MouseEvent>(){
			@Override 
			public void handle(MouseEvent event) {
				
				if((47.0 <= event.getX() && event.getX() <= 666.0) && (39.0 <= event.getY() && event.getY() <= 658.0)) {
				double valenceSlope = 2.0/619.0;
				double valenceConstant = -713.0/619.0;
				double arousalSlope = -2.0/619.0;
				double arousalConstant = 697.0/619.0;
				
				double valenceConverted = event.getX()*valenceSlope + valenceConstant;
				BigDecimal roundedValenceConverted = new BigDecimal(valenceConverted).setScale(2,RoundingMode.HALF_UP);
				double roundedValence = roundedValenceConverted.doubleValue();
				
				double arousalConverted = event.getY()*arousalSlope + arousalConstant;
				BigDecimal roundedArousalConverted = new BigDecimal(arousalConverted).setScale(2,RoundingMode.HALF_UP);
				double roundedArousal = roundedArousalConverted.doubleValue();
				
				String coordinates = "Valence: " + roundedValence + " Arousal: " + roundedArousal;
				coordinateDetail.setText(coordinates);
				}
				else {
					coordinateDetail.setText("Point: ");
				}
			}
		});
	}
	
	public void createPointByClick() {
		ValenceArousalPlot.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@SuppressWarnings("unchecked")
			@Override
			public void handle(MouseEvent event) {
				
				if((47.0 <= event.getX() && event.getX() <= 666.0) && (39.0 <= event.getY() && event.getY() <= 658.0)) {
					double valenceSlope = 2.0/619.0;
					double valenceConstant = -713.0/619.0;
					double arousalSlope = -2.0/619.0;
					double arousalConstant = 697.0/619.0;
					
					double valenceConverted = event.getX()*valenceSlope + valenceConstant;
					BigDecimal roundedValenceConverted = new BigDecimal(valenceConverted).setScale(2,RoundingMode.HALF_UP);
					double roundedValence = roundedValenceConverted.doubleValue();
					
					double arousalConverted = event.getY()*arousalSlope + arousalConstant;
					BigDecimal roundedArousalConverted = new BigDecimal(arousalConverted).setScale(2,RoundingMode.HALF_UP);
					double roundedArousal = roundedArousalConverted.doubleValue();
					
					double annotationTime = player.getCurrentTime().toSeconds();
					double roundAT = Math.round(annotationTime*100.0)/100.0;
					annotationTimes.add(roundAT);
					
					XYChart.Data<Number, Number> data = new XYChart.Data<Number,Number>(roundedValence,roundedArousal);
					data.setNode(new HoverNode(Double.toString(roundedValence),Double.toString(roundedArousal),coordinateDetail));
					
					emotionCoordinates.getData().add(data);
					emotionCoordinates.setName("Emotion Coordinates");
					if(ValenceArousalPlot.getData().contains(emotionCoordinates)) {
						ValenceArousalPlot.getData().remove(1);
					}
					ValenceArousalPlot.getData().add(emotionCoordinates);
					colourNodes(ValenceArousalPlot);
				}
				
			}
		});
	}
	
	public void colourNodes(XYChart<Number,Number> ValenceArousalPlot) {
		Set<Node> nodes = ValenceArousalPlot.lookupAll(".series" + 1);
		
		double currentR = startR;
		double currentG = startG;
		double currentB = startB;
		
		for(Node n : nodes) {
			n.setStyle("-fx-background-color: rgb(" + currentR + "," + currentG + "," + currentB + ")");
			currentR += differenceR;
			currentG += differenceG;
			currentB += differenceB;
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
		
		for(int i = 0; i < emotionCoordinates.getData().size(); i++) {
			Number valence = emotionCoordinates.getData().get(i).getXValue();
			Number  arousal = emotionCoordinates.getData().get(i).getYValue();
			String[] coordinates = new String[3];
			coordinates[0] = annotationTimes.get(i).toString();
			coordinates[1] = valence.toString();
			coordinates[2] = arousal.toString();
			writer.writeNext(coordinates);
		}
		writer.close();
	}
	
	public void returnToMainMenu(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("fxml/HomeScreen.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root, borderPane.getWidth(), borderPane.getHeight());
			scene.getStylesheets().add(getClass().getResource("css/HomeScreen.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
