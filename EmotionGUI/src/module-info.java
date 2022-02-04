module EmotionGUI {
	requires transitive java.desktop;
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	requires com.opencsv;
	requires javafx.swing;
	requires javafx.media;
	requires java.base;
	requires org.apache.commons.lang3;
	
	opens application to javafx.graphics, javafx.fxml;
}
