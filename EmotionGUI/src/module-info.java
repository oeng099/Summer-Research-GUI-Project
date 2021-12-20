module EmotionGUI {
	requires transitive java.desktop;
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	requires com.opencsv;
	requires javafx.swing;
	
	opens application to javafx.graphics, javafx.fxml;
}
