package application;

import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class AnnotationScreenController extends ValenceArousalScreenController {

	@FXML
	Button selectMedia;
	@FXML
	Button playMedia;
	@FXML
	MediaView audioVisual;
	@FXML
	Label timeLabel;
	@FXML
	Button playPause;
	@FXML
	Button forward;
	@FXML
	Button backward;
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
	@FXML
	ImageView waveform;
	@FXML
	Button clear;

	private Media media;
	private MediaPlayer player;
	private AutoClicker autoclicker;

	boolean pPressed = false;

	private int numNodes;
	private Duration totalTime;

	XYChart.Series<Number, Number> emotionCoordinates = new XYChart.Series<Number, Number>();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//Initialises the sliders on the screen.
		initialiseTimeSlider();
		initialiseVolumeSlider();
		initialiseSpeedSlider();
		
		//Adds the initial features to the Valence-Arousal Plot
		initialiseModel();
	};

	// Method to set up the time slider
	public void initialiseTimeSlider() {
		timeSlider.valueProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable ov) {
				// Changes the time of the media player if the time slider value is changed.
				if (timeSlider.isValueChanging()) {
					player.seek(player.getTotalDuration().multiply(timeSlider.getValue() / 100.0));
				}

			}

		});
	}

	// Method to set up the volume slider
	public void initialiseVolumeSlider() {
		volumeSlider.valueProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable ov) {
				// Changes the volume of the player to the slider value when the slider is
				// changed.
				if (volumeSlider.isValueChanging()) {
					player.setVolume(volumeSlider.getValue() / 100.0);
				}

			}

		});
	}

	//Method to set up the speed slider
	public void initialiseSpeedSlider() {
		speedSlider.setMaxWidth(400.0);
		//Changes the player rate and speed text to match the value the speed slider is changed to.
		speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				//Changes the text displayed to the new speed
				currentSpeed.setText(String.format("Current Speed: %.2f ", newValue));
				//Changes the media player to the new speed
				player.setRate(newValue.doubleValue());
			}
		});
	}

	//Method to switch the media status between PLAYING and PAUSED when p key is pressed
	public void pKeyPressed() {
		//Sets pPressed to true if p was pressed to change the status of the player from PAUSED to PLAYING
		//This is to ensure there is no time delay when the AutoClicker is resumed .
		if (player.getStatus() == MediaPlayer.Status.PAUSED) {
			pPressed = true;
		}
		//Runs the changeMediaStatus method set to playPause button
		playPause.fire();
	}

	//Method to select and load a media file into the media player
	public void selectMediaFile(ActionEvent event) throws IOException, InterruptedException {

		//Sets up a file chooser to only see mp4 and wav files and then allows user to select a file
		FileChooser mediaFileChooser = setUpFileChooser("Media Files",new String[] {"*.mp4","*.wav"});
		File mediaFile = openFile(mediaFileChooser);
		
		if (isCorrectFileType(mediaFile.toString(),"mp4")) {
			//If file selected is an mp4 file it is loaded and the player is made visible
			loadMedia(mediaFile);
			audioVisual.setVisible(true);
			waveform.setVisible(false);
		} else if(isCorrectFileType(mediaFile.toString(),"wav")) {
			//If file selected is a wav file it is loaded, the player is made not visible and a waveform image
			//is loaded in its place
			loadMedia(mediaFile);
			audioVisual.setVisible(false);
			waveform.setVisible(true);
			//Loads a png of the waveform of the the wav file
			loadWaveform(mediaFile);
		}
	}

	//Method to load the media player with a file
	public void loadMedia(File file) {
		media = new Media(file.toURI().toString());
		player = new MediaPlayer(media);
		audioVisual.setMediaPlayer(player);
	}
	
	//Method to load an image of a waveform to the screen
	public void loadWaveform(File mediaFile) throws IOException, InterruptedException {
		//Creates the waveform png based on the file inputed
		createWaveform(mediaFile.toString());
		String fileNameWithoutExtension = mediaFile.getName().replace(".wav","");
		//Creates an image based on the name of the image created by createWaveform
		Image audioWaveform = new Image(new FileInputStream(
				"src/application/images/audio_waveforms/" + fileNameWithoutExtension + "_Audio_Waveform.png"));
		waveform.setImage(audioWaveform);
	}

	//Method to create an waveform image from a wav file
	public void createWaveform(String WAVFileName) throws IOException, InterruptedException {
		PythonScriptManager pythonScriptManager = new PythonScriptManager("src/application/plotWAV.py");
		//Inputs the name of the WAVFile into the Python script
		pythonScriptManager.changePythonScript("wavfile =","wavfile = r'" + WAVFileName + "'");
		pythonScriptManager.runScript("plotWAV.py");
	}

	public void playMediaFile(ActionEvent event) {

		this.numNodes = (int) Math.floor(player.getStopTime().toMillis() / 500);
		calculateDifferences(numNodes,numSeries);

		this.totalTime = player.getTotalDuration();
		player.setVolume(volumeSlider.getValue() / 100.0);
		player.currentTimeProperty().addListener(
				(observable, oldTime, newTime) -> timeLabel.setText(formatTime(newTime, player.getTotalDuration())));
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
		});
		;
	}

	public void changeMediaStatus(ActionEvent event) {
		if (player.getStatus() == MediaPlayer.Status.PLAYING) {
			player.pause();
			autoclicker.pauseClicking();
		} else {
			player.play();
			autoclicker.resumeClicking(pPressed);
			pPressed = false;
		}
	}

	public void playfromStart(ActionEvent event) {
		player.seek(player.getStartTime());
		clear.fire();
		playMedia.fire();
	}

	public void goBackFiveSeconds(ActionEvent event) {
		player.seek(player.getCurrentTime().subtract(Duration.seconds(5)));
		double pointsInFiveSec = 5.0 / (Double.valueOf(autoclicker.getDelayTime()) / 1000);
		int pointsToRemove = (int) Math.round(pointsInFiveSec) + 1;
		int currentPointsLength = emotionCoordinates.getData().size();
		for (int i = currentPointsLength - 1; i > currentPointsLength - pointsToRemove; i--) {
			emotionCoordinates.getData().remove(i);
		}
		if (ValenceArousalPlot.getData().contains(emotionCoordinates)) {
			ValenceArousalPlot.getData().remove(1);
		}
		ValenceArousalPlot.getData().add(emotionCoordinates);
		colourNodes(ValenceArousalPlot,numSeries);
	}

	public void goForwardFiveSeconds(ActionEvent event) {
		player.seek(player.getCurrentTime().add(Duration.seconds(5)));
	}

	public void startAutoClicker() {
		this.autoclicker = new AutoClicker(InputEvent.BUTTON1_DOWN_MASK);
		autoclicker.startClicking();
	}

	public void updateTime() {
		player.currentTimeProperty().addListener(new ChangeListener<Duration>() {

			@Override
			public void changed(ObservableValue<? extends Duration> ov, Duration oldTime, Duration currentTime) {
				timeSlider.setValue(currentTime.toMillis() / totalTime.toMillis() * 100.0);
			}

		});
	}

	public String formatTime(Duration currentTime, Duration totalTime) {
		int secondsElapsed = (int) Math.floor(currentTime.toSeconds());
		int hoursElapsed = secondsElapsed / 360;
		if (hoursElapsed > 0) {
			secondsElapsed -= hoursElapsed * 360;
		}
		int minutesElapsed = secondsElapsed / 60;
		if (minutesElapsed > 0) {
			secondsElapsed -= minutesElapsed * 60;
		}

		int totalSeconds = (int) Math.floor(totalTime.toSeconds());
		int totalHours = totalSeconds / 360;
		if (totalHours > 0) {
			totalSeconds -= totalSeconds * 360;
		}
		int totalMinutes = totalSeconds / 60;
		if (totalMinutes > 0) {
			totalSeconds -= totalMinutes * 60;
		}

		return String.format("%d:%02d:%02d/%d:%02d:%02d", hoursElapsed, minutesElapsed, secondsElapsed, totalHours,
				totalMinutes, totalSeconds);
	}



	public void createPointByClick() {
		
		ArrayList<Double> mediaAnnotationTimes = new ArrayList<Double>();
		ValenceArousalPlot.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				if ((47.0 <= event.getX() && event.getX() <= 666.0)
						&& (39.0 <= event.getY() && event.getY() <= 658.0)) {
					double valenceSlope = 2.0 / 619.0;
					double valenceConstant = -713.0 / 619.0;
					double arousalSlope = -2.0 / 619.0;
					double arousalConstant = 697.0 / 619.0;

					double valenceConverted = event.getX() * valenceSlope + valenceConstant;
					BigDecimal roundedValenceConverted = new BigDecimal(valenceConverted).setScale(2,
							RoundingMode.HALF_UP);
					double roundedValence = roundedValenceConverted.doubleValue();

					double arousalConverted = event.getY() * arousalSlope + arousalConstant;
					BigDecimal roundedArousalConverted = new BigDecimal(arousalConverted).setScale(2,
							RoundingMode.HALF_UP);
					double roundedArousal = roundedArousalConverted.doubleValue();

					double annotationTime = player.getCurrentTime().toSeconds();
					double roundAT = Math.round(annotationTime * 100.0) / 100.0;
					mediaAnnotationTimes.add(roundAT);

					XYChart.Data<Number, Number> data = new XYChart.Data<Number, Number>(roundedValence,
							roundedArousal);
					data.setNode(new HoverNode(Double.toString(roundedValence), Double.toString(roundedArousal),
							coordinateDetail));
					plotEmotionalCoordinates(data);
				}

			}
		});
		annotationTimes.add(0,mediaAnnotationTimes);
	}

	public void plotEmotionalCoordinates(XYChart.Data<Number, Number> data) {
		emotionCoordinates.getData().add(data);
		emotionCoordinates.setName("Emotion Coordinates");
		if (ValenceArousalPlot.getData().contains(emotionCoordinates)) {
			ValenceArousalPlot.getData().remove(1);
		}
		ValenceArousalPlot.getData().add(emotionCoordinates);
		colourNodes(ValenceArousalPlot,numSeries);
	}




	// Method to clear the model
	public void clearModel(ActionEvent event) {
		// Checking if Emotion Coordinates series is plotted on the model
		if (ValenceArousalPlot.getData().size() > 1) {
			// Clears the data from emotionCoordinates
			emotionCoordinates.getData().clear();
			// Clears the model of the Emotion Coordinates series
			ValenceArousalPlot.getData().remove(1);
		}
	}
}
