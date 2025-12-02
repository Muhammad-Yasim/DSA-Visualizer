module com.example.dsavisualizer {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.dsavisualizer to javafx.fxml;
    exports com.example.dsavisualizer;
}