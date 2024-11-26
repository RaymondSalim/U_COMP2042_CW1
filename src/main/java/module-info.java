module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demo.view to javafx.fxml;
    opens com.example.demo.view.base to javafx.fxml;
    opens com.example.demo.model to javafx.fxml;
    opens com.example.demo.model.base to javafx.fxml;
    opens com.example.demo.observer to javafx.fxml;
    opens com.example.demo.observer.base to javafx.fxml;
    opens com.example.demo.events to javafx.fxml;
    opens com.example.demo.controller to javafx.fxml;
    exports com.example.demo;
    opens com.example.demo.view.objects to javafx.fxml;
    opens com.example.demo.view.screens to javafx.fxml;
}