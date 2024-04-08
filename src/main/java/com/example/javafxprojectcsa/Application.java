package com.example.javafxprojectcsa;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
        Scene sceneHome = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Start Screen");
        stage.setScene(sceneHome);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}