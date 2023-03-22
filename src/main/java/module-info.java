module com.example.quizserver {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.quizserver to javafx.fxml;
    exports com.example.quizserver;
}