package application;

import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

	//Method to play a media file from start
	public void playMediaFile(ActionEvent event) {

		//Set up the total number of nodes to be plotted, the colour gradation increments and total time
		this.numNodes = (int) Math.floor(player.getStopTime().toMillis() / 500);
		calculateDifferences(numNodes,numSeries);
		this.totalTime = player.getTotalDuration();
		
		player.setVolume(volumeSlider.getValue() / 100.0);
		
		//Sets up a listener to have the time label match the player time
		updateTimeLabel();
		
		//Start the autoclicker to begin annotation
		startAutoClicker();
		try {
			//Thread is paused for five seconds, similar to that of the autoclicker to allow user to move
			//into correct position for annotation
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//Starts playing the media
		player.play();
		//Sets up a listener to have the time slider match the player time
		updateTimeSlider();
		
		//When the media ends, stop annotation
		player.setOnEndOfMedia(() -> {
			autoclicker.stopClicking();
		});
		;
	}
	
	//Method to set a listener to the player so that the timeSlider will move in accordance to the player time
	public void updateTimeSlider() {
		player.currentTimeProperty().addListener(new ChangeListener<Duration>() {

			@Override
			public void changed(ObservableValue<? extends Duration> ov, Duration oldTime, Duration currentTime) {
				timeSlider.setValue(currentTime.toMillis() / totalTime.toMillis() * 100.0);
			}

		});
	}
	
	//Method to set up a listener to the player so that the time label will be updated to the player time
	public void updateTimeLabel() {
		player.currentTimeProperty().addListener(new ChangeListener<Duration>() {

			@Override
			public void changed(ObservableValue<? extends Duration> ov, Duration oldTime, Duration currentTime) {
					timeLabel.setText(formatTime(currentTime, player.getTotalDuration()));
		}
	});
	}
	
	//Method to start the autoclicker that serves as the way for users to annotate
	public void startAutoClicker() {
		this.autoclicker = new AutoClicker(InputEvent.BUTTON1_DOWN_MASK);
		autoclicker.startClicking();
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

	//Method to play a media from the start again
	public void playfromStart(ActionEvent event) {
		//Set the player back to its start time
		player.seek(player.getStartTime());
		//Clears the model by pressing the clear button
		clear.fire();
		//Plays the media again by pressing the play button
		playMedia.fire();
	}

	//Method to skip five seconds behind in the media
	public void goBackFiveSeconds(ActionEvent event) {
		//Sets the player time to five seconds behind
		player.seek(player.getCurrentTime().subtract(Duration.seconds(5)));
		//Calculates the number of points to remove
		int pointsToRemove = calculatePointsToRemove(autoclicker.getDelayTime());
		//Remove the points plotted in the last five seconds from the emotionCoordinates series
		int currentPointsLength = emotionCoordinates.getData().size();
		for (int i = currentPointsLength - 1; i > currentPointsLength - pointsToRemove; i--) {
			emotionCoordinates.getData().remove(i);
		}
		plotAnnotationCoordinates(emotionCoordinates);
	}
	
	//Method to calculate the number of points to remove
	public int calculatePointsToRemove(int rate) {
		//Calculates the number of points that were plotted in the last five seconds
		double pointsInFiveSec = 5.0 / (Double.valueOf(rate) / 1000);
		//Rounds the number of points up 
		int pointsToRemove = (int) Math.round(pointsInFiveSec) + 1;
		return pointsToRemove;
	}
	
	//Method to skip five seconds ahead in the media
	public void goForwardFiveSeconds(ActionEvent event) {
		//Sets the player time to five seconds ahead
		player.seek(player.getCurrentTime().add(Duration.seconds(5)));
	}
	
	//Method to format the current time and total time of the MediaPlayer
	public String formatTime(Duration currentTime, Duration totalTime) {
		
		//Gets the time the player is currently on and the total time of the player
		Time presentTime = new Time((int) Math.floor(currentTime.toSeconds()));
		Time finishTime = new Time((int) Math.floor(totalTime.toSeconds()));

		return String.format("%d:%02d:%02d/%d:%02d:%02d", presentTime.getHours(), presentTime.getMinutes(),presentTime.getSeconds(), finishTime.getHours(),
				finishTime.getMinutes(), finishTime.getSeconds());
	}

	//Method to create a point on the model at the location of a mouse click
	public void createPointByClick() {
		
		ArrayList<Double> mediaAnnotationTimes = new ArrayList<Double>();
		ValenceArousalPlot.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//Checks if the mouse cursor is within the model
				if (isInModel(event)) {

					// Converts the mouse coordinate to produce valence and arousal coordinates
					double valence = convertCoordinate(event.getX(), valenceSlope, valenceConstant);
					double arousal = convertCoordinate(event.getY(), arousalSlope, arousalConstant);
					
					//Adds the current time of the annotation to mediaAnnotationTimes
					mediaAnnotationTimes.add(getTimeFromPlayer());

					//Add the new point to the emotionCoordinates series and then plot the new series
					emotionCoordinates = addNewNode(emotionCoordinates,valence,arousal);
					plotAnnotationCoordinates(emotionCoordinates);
				}

			}
		});
		annotationTimes.add(0,mediaAnnotationTimes);
	}

	//Method to plot the coordinates of the data from annotation to the model
	public void plotAnnotationCoordinates(XYChart.Series<Number, Number> series) {
		emotionCoordinates.setName("Emotion Coordinates");
		//If there is an already existing emotionCoordiantes series, remove it
		if (ValenceArousalPlot.getData().contains(emotionCoordinates)) {
			ValenceArousalPlot.getData().remove(1);
		}
		ValenceArousalPlot.getData().add(emotionCoordinates);
		colourNodes(ValenceArousalPlot,numSeries);
	}
	
	//Method to get the time from the Media Player
	public double getTimeFromPlayer() {
		
		double annotationTime = player.getCurrentTime().toSeconds();
		//Round the time extracted to two 2 dp
		double roundAT = Math.round(annotationTime * 100.0) / 100.0;
		return roundAT;
	}


	// Method to clear the Valence-Arousal model
	@Override
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
