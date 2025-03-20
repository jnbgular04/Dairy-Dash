module ProjectDec17Updated2 {
		
		requires javafx.base;
		requires javafx.graphics;
		requires javafx.controls;
		
		requires javafx.media;
		requires java.desktop;
		
		
		opens application to javafx.graphics, javafx.fxml;

	
}