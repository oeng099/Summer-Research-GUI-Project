package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VisualiseScreenController implements Initializable{

	@FXML
	Button selectFile;
	@FXML
	Button plotFile;
	@FXML
	Button plotCoordinates;
	@FXML
	Button mainMenu;
	@FXML
	Button saveCoordinates;
	@FXML
	Button saveImage;
	@FXML
	Button clearChart;
	@FXML
	Button CSVInfo;
	@FXML
	TextField CSVFilename;
	@FXML
	TextField WAVFilename;
	@FXML
	TextField valenceCoordinate;
	@FXML
	TextField arousalCoordinate;
	@FXML
	Text manualPlotError;
	@FXML
	Text coordinateDetail;
	@FXML
	Text valenceText;
	@FXML
	Text arousalText;
	@FXML
	Text manualPlotText;
	@FXML
	Text CSVFileText;
	@FXML
	Text instructionText;
	@FXML
	Text WAVFileText;
	@FXML
	Text selectAFileText;
	@FXML
	BorderPane borderPane;
	@FXML
	ScatterChart<Number, Number> ValenceArousalPlot;


	XYChart.Series<Number, Number> emotionCoordinates = new XYChart.Series<Number, Number>();

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private FXMLLoader loader;	
	
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		URL imageURL = getClass().getResource("images/infoIcon.png");
		Image image = new Image(imageURL.toExternalForm());
		ImageView view = new ImageView(image);
		view.setFitHeight(30);
		view.setPreserveRatio(true);
		
		CSVInfo.setPrefSize(30,30);
		CSVInfo.setGraphic(view);
		
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
			//landmarkData.setNode(new HoverNode(landmarkValence[i],landmarkArousal[i],coordinateDetail,landmarkEmotions.get(i)));
			
			Label label = new Label(landmarkEmotions.get(i));
			
			landmarkData.setNode(label);
			
			label.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					int emotionIndexNumber = landmarkEmotions.indexOf(label.getText());
					coordinateDetail.setText("Point: " + landmarkValence[emotionIndexNumber] + "," + landmarkArousal[emotionIndexNumber] + " (" + landmarkEmotions.get(emotionIndexNumber) + ")");
				}
			});

			label.setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					coordinateDetail.setText("Point:");
				}
			});
			
			initialSeries.getData().add(landmarkData);
		}
		
		ValenceArousalPlot.getData().add(initialSeries);
		
		//Set<Node> initialPoints = ValenceArousalPlot.lookupAll(".series" + 0);
		//for(Node n : initialPoints) {
			//n.setStyle();
		//}
		

		
	}
	
	public void showMouseCoordinates() {
		ValenceArousalPlot.setOnMouseMoved(new EventHandler<MouseEvent>(){
			@Override 
			public void handle(MouseEvent event) {
				
				if((69.0 <= event.getX() && event.getX() <= 666.0) && (39.0 <= event.getY() && event.getY() <= 639.0)) {
				double valenceSlope = 2.0/597.0;
				double valenceConstant = -245.0/199.0;
				double arousalSlope = 1.0/300.0;
				double arousalConstant = -113.0/100.0;
				
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
	
	

	public void selectAFile(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();

		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			//REMEMBER TO ADD CSV ERROR HANDLING
			CSVFilename.setText(file.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void plotCSVFile(ActionEvent event) throws CsvValidationException, IOException {
	
		File file = new File(CSVFilename.getText());
		CSVReader reader = new CSVReaderBuilder(new FileReader(file)).withSkipLines(1).build();
		String[] lineInFile;
		while((lineInFile = reader.readNext()) != null) {
			double unroundedValence = Double.parseDouble(lineInFile[1]);
			double unroundedArousal = Double.parseDouble(lineInFile[2]);
			BigDecimal roundedValence = new BigDecimal(unroundedValence).setScale(2,RoundingMode.HALF_UP);
			BigDecimal roundedArousal = new BigDecimal(unroundedArousal).setScale(2,RoundingMode.HALF_UP);
			double valence = roundedValence.doubleValue();
			double arousal = roundedArousal.doubleValue();
			XYChart.Data<Number, Number> data = new XYChart.Data<Number, Number>(valence, arousal);
			data.setNode(new HoverNode(Double.toString(valence),Double.toString(arousal),coordinateDetail));
			emotionCoordinates.getData().add(data);
		}
		emotionCoordinates.setName("Emotion Points");
		manualPlotError.setText(" ");
		ValenceArousalPlot.getData().addAll(emotionCoordinates);
	}
	
	@SuppressWarnings("unchecked")
	public void plotCSVFile(String CSVFile) throws CsvValidationException, IOException {
		
		File file = new File(CSVFile);
		CSVReader reader = new CSVReaderBuilder(new FileReader(file)).withSkipLines(1).build();
		String[] lineInFile;
		while((lineInFile = reader.readNext()) != null) {
			double unroundedValence = Double.parseDouble(lineInFile[0]);
			double unroundedArousal = Double.parseDouble(lineInFile[1]);
			BigDecimal roundedValence = new BigDecimal(unroundedValence).setScale(2,RoundingMode.HALF_UP);
			BigDecimal roundedArousal = new BigDecimal(unroundedArousal).setScale(2,RoundingMode.HALF_UP);
			double valence = roundedValence.doubleValue();
			double arousal = roundedArousal.doubleValue();
			XYChart.Data<Number, Number> data = new XYChart.Data<Number, Number>(valence, arousal);
			data.setNode(new HoverNode(Double.toString(valence),Double.toString(arousal),coordinateDetail));
			emotionCoordinates.getData().add(data);
		}
		emotionCoordinates.setName("Emotion Points");
		manualPlotError.setText(" ");
		ValenceArousalPlot.getData().addAll(emotionCoordinates);
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

	@SuppressWarnings("unchecked")
	public void plotManual(ActionEvent event) throws IOException {
		if (valenceCoordinate.getText().isEmpty() || arousalCoordinate.getText().isEmpty()) {
			manualPlotError.setText("Please enter valid numbers for both valence and arousal from -1 to 1");
		} else if (!(Double.parseDouble(valenceCoordinate.getText()) < 1.0
				&& Double.parseDouble(valenceCoordinate.getText()) > -1.0)
				|| !(Double.parseDouble(arousalCoordinate.getText()) < 1.0
						&& Double.parseDouble(arousalCoordinate.getText()) > -1.0)) {
			manualPlotError.setText("Please enter valid numbers for both valence and arousal from -1 to 1");
		} else {
			emotionCoordinates.setName("Emotion Points");
			manualPlotError.setText(" ");
			double valence = Double.parseDouble(valenceCoordinate.getText());
			double arousal = Double.parseDouble(arousalCoordinate.getText());
			XYChart.Data<Number, Number> data = new XYChart.Data<Number, Number>(valence, arousal);
			data.setNode(new HoverNode(valenceCoordinate.getText(),arousalCoordinate.getText(),coordinateDetail));
			emotionCoordinates.getData().add(data);
			if(!ValenceArousalPlot.getData().contains(emotionCoordinates)) {
				ValenceArousalPlot.getData().addAll(emotionCoordinates);
			} else {
				ValenceArousalPlot.getData().remove(1);
				ValenceArousalPlot.getData().addAll(emotionCoordinates);
			}
		}
	}
	
	public void saveAsCSV(ActionEvent event) throws IOException {
		String csv = "emotion.csv";
		CSVWriter writer = new CSVWriter(new FileWriter(csv));
		
		String [] record = "Valence,Arousal".split(",");
		writer.writeNext(record);
		
		for(int i = 0; i < emotionCoordinates.getData().size(); i++) {
			Number valence = emotionCoordinates.getData().get(i).getXValue();
			Number  arousal = emotionCoordinates.getData().get(i).getYValue();
			String[] coordinates = new String[2];
			coordinates[0] = valence.toString();
			coordinates[1] = arousal.toString();
			writer.writeNext(coordinates);
		}
		
		writer.close();
	}
	
	public void saveAsPNG(ActionEvent event) {
		WritableImage image = ValenceArousalPlot.snapshot(new SnapshotParameters(), null);
		
		File file = new File("emotions.png");
		
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image,null), "png", file);
		} catch (IOException e) {
			System.out.println("Scatter plot cannot be converted.");
		}
	}

	public void clearModel(ActionEvent event) {
		if(emotionCoordinates.getData().equals(null)) {
		ValenceArousalPlot.getData().remove(1);
		}
		emotionCoordinates.getData().clear();
	}
	
	public void CSVHelp(ActionEvent event) throws IOException {
		loader = new FXMLLoader(getClass().getResource("fxml/CSVHelpScreen.fxml"));
		root = (Parent) loader.load();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("css/CSVHelpScreen.css").toExternalForm());
		stage = new Stage();
		stage.setTitle("CSV File Info");
		stage.setResizable(false);
		stage.setScene(scene);
		
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}
	
	public void WAV_TO_CSV(String WAVFile) throws IOException, InterruptedException {
		String[] command = new String[] {"python3","group108demo.py",WAVFile};
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.directory(new File("src/application/WAV_To_CSV"));
		Process process = builder.start();
		process.waitFor();
	}
	
	public void selectWAVFile(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();

		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			//REMEMBER TO ADD WAV ERROR HANDLING
			WAVFilename.setText(file.toString());
		}
	}
	
	public void plotWAVFile(ActionEvent event) throws IOException, CsvValidationException, InterruptedException {
		String WAVFile = WAVFilename.getText();
		if(checkCorrectFileType(WAVFile,"wav")){
		WAV_TO_CSV(WAVFile);
		String[] WAVFileArray = WAVFile.split("/");
		String CSVFile = WAVFileArray[WAVFileArray.length-1].replace("wav", "csv");
		plotCSVFile("src/application/WAV_To_CSV/CSV_Outputs/" + CSVFile);
		}
	}
	
	public boolean checkCorrectFileType(String filename, String extension) {
		String filenameExtension = filename.substring(filename.lastIndexOf(".")+1,filename.length());
		if(filenameExtension.equals(extension)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void changeModel(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("ModelChooseScreen.fxml"));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root,borderPane.getWidth(),borderPane.getHeight());
		//scene.getStylesheets().add(getClass().getResource().toExternalForm());
		stage.setTitle("Change the Valence and arousal models");
		stage.setScene(scene);
		stage.show();
	}

}
