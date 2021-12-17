module EmotionGUI {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	requires com.opencsv;
	
	opens application to javafx.graphics, javafx.fxml;
}
