module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.example.demo.controller;
    opens com.example.demo.view to javafx.fxml;
    opens com.example.demo.model to javafx.fxml;
    opens com.example.demo.view.base to javafx.fxml;
    opens com.example.demo.model.base to javafx.fxml;
}