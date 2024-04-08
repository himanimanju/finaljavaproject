package com.example.javafxprojectcsa;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class GameView implements Initializable {
    public Button angleBtn;
    private double angle;
    private int successfulShots = 0;

    @FXML
    TextField angleText;
    @FXML
    Label errorLabel;
    @FXML
    Button shootBtn;
    @FXML
    ImageView hoopImage;
    @FXML
    ImageView ballImage;
    @FXML
    Label shotsMadeLabel;
    @FXML
    ImageView personImage;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label shotResultLabel;
    @FXML
    private Circle ballCenterDot;
    @FXML
    private Circle hoopCenterDot;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("src/main/resources/Image/hoopImage.png");
        Image image = new Image(file.toURI().toString());
        hoopImage.setImage(image);

        File file1 = new File("src/main/resources/Image/basketball.png");
        Image image1 = new Image(file1.toURI().toString());
        ballImage.setImage(image1);

        File file2 = new File("src/main/resources/Image/personImage.png");
        Image image2 = new Image(file2.toURI().toString());
        personImage.setImage(image2);

        // FOR DEBUGGING PURPOSES, REMOVE ONCE DONE
        ballCenterDot = new Circle(0, 0, 5, Color.RED); // A small red dot
        hoopCenterDot = new Circle(0, 0, 5, Color.BLUE); // A small blue dot

        borderPane.getChildren().add(ballCenterDot);
        borderPane.getChildren().add(hoopCenterDot);

        positionBallCenterDot();
        positionHoopCenterDot();
    }

    @FXML
    private void onAngleBtnClick() {
        shotResultLabel.setVisible(false);
        errorLabel.setText("");
        String input = angleText.getText();

        try {
            double number = Double.parseDouble(input);

            if (number < 0 || number > 90) {
                errorLabel.setText("Invalid input. Enter a number from 0 to 90");
            } else {
                errorLabel.setText("Angle entered is: " + number);
                shootBtn.setVisible(true);

                angle = number * Math.PI / 180;
                angleText.clear();
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid input. Please enter a number.");
        }
        errorLabel.setVisible(true);
    }

    @FXML
    private void onShootBtnClick(){
        double endY;
        double endX;
        double startX = 50;
        double startY = 150;
        int v1 = 20;
        endX = 10 * (2 * Math.pow(v1, 2) * Math.cos(angle) * Math.sin(angle)) / 9.8;
        endY = 150;
        System.out.println(endX + ", " + endY);

        calculateShotPath(startX, startY, endX, endY, v1);

        shotsMadeLabel.setText("Shots Made: " + successfulShots);
        shotResultLabel.setVisible(true);
        shootBtn.setVisible(false);
        errorLabel.setVisible(false);
    }

    private void calculateShotPath(double startX, double startY, double endX, double endY, int v1) {
        int numPoints = 100;
        double[] pathX = new double[numPoints];
        double[] pathY = new double[numPoints];
        double y = startY;

        // Calculate coordinates of each point along the shot path
        for (int i = 0; i < numPoints; i++) {
            double x = i*(startX + endX)/numPoints;

            if(i <= numPoints/2)
            {
                y = y - 2;
            } else {
                y = y + 2;
            }
            pathX[i] = x;
            pathY[i] = y;
        }

        double height= startX/(v1*Math.cos(angle));
        double midX = (startX + endX)/2;
        double midY = startY - 2*(startX*Math.tan(angle) + 0.5*9.8*Math.pow(height, 2));
        var cpX = 2*midX - pathX[0]/2 - pathX[99]/2;
        var cpY = 2*midY - pathY[0]/2 - pathY[99]/2;

        QuadCurve test = new QuadCurve();
        test.setStartX(pathX[0]);
        test.setStartY(pathY[0]);
        test.setControlX(cpX);
        test.setControlY(cpY);
        test.setEndY(pathY[99]);
        test.setEndX(pathX[99]);

        PathTransition pathTransitionTest = new PathTransition();
        pathTransitionTest.setNode(ballImage);
        pathTransitionTest.setDuration(Duration.seconds(5));
        pathTransitionTest.setPath(test);

        AnimationTimer collisionChecker = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Bounds ballBounds = ballImage.getBoundsInParent();
                double ballCenterX = ballBounds.getMinX() + ballBounds.getWidth() / 2;
                double ballCenterY = ballBounds.getMinY() + ballBounds.getHeight() / 2 + 40;
                positionBallCenterDot();
                positionHoopCenterDot();

                Bounds hoopBounds = hoopImage.getBoundsInParent();
                double hoopCenterX = hoopBounds.getMinX() + hoopBounds.getWidth() / 2;
                double hoopCenterY = hoopBounds.getMinY() + hoopBounds.getHeight() / 2;

                double distance = Math.sqrt(Math.pow(ballCenterX - hoopCenterX, 2) + Math.pow(ballCenterY - hoopCenterY, 2));

                if (distance < 15) {
                    successfulShots++;
                    shotsMadeLabel.setText("Shots Made: " + successfulShots);

                    this.stop();
                    shotResultLabel.setText("Nice Shot!");
                    shotResultLabel.setVisible(true);
                }
            }
        };

        pathTransitionTest.setOnFinished(event -> collisionChecker.stop());

        pathTransitionTest.play();
        collisionChecker.start();
    }

    private void positionHoopCenterDot() {
        Bounds hoopBounds = hoopImage.getBoundsInParent();
        hoopCenterDot.setCenterX(hoopBounds.getMinX() + hoopBounds.getWidth() / 2);
        hoopCenterDot.setCenterY(hoopBounds.getMinY() + hoopBounds.getHeight() / 2);
    }

    private void positionBallCenterDot() {
        Bounds ballBounds = ballImage.getBoundsInParent();
        ballCenterDot.setCenterX(ballBounds.getMinX() + ballBounds.getWidth() / 2);
        ballCenterDot.setCenterY(ballBounds.getMinY() + ballBounds.getHeight() / 2 + 40);
    }
}