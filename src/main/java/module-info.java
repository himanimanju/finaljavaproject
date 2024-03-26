module com.example.javafxprojectcsa {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javafxprojectcsa to javafx.fxml;
    exports com.example.javafxprojectcsa;
}