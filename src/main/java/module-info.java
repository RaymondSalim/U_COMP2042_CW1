module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.demo.model to javafx.fxml;
    opens com.example.demo.model.base to javafx.fxml;
    opens com.example.demo.observer to javafx.fxml;
    opens com.example.demo.observer.base to javafx.fxml;
    opens com.example.demo.controller to javafx.fxml;
    opens com.example.demo.view.objects to javafx.fxml;
    opens com.example.demo.view.screens to javafx.fxml;
    opens com.example.demo.view.levels to javafx.fxml;
    opens com.example.demo.view.overlays to javafx.fxml;
    opens com.example.demo.view.components to javafx.fxml;
    opens com.example.demo.utils to javafx.fxml;
    opens com.example.demo.view.entities to javafx.fxml;

    exports com.example.demo;
}