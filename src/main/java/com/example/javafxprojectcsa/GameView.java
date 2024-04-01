package com.example.javafxprojectcsa;

import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.QuadCurve;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.BorderPane;


public class GameView implements Initializable {
    private int score;
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
    private BorderPane borderPane;

    private double[] pathX;
    private double[] pathY;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        // Load hoop image
        Image hoopImg = new Image(getClass().getResourceAsStream("/Image/hoopImage.png"));
        hoopImage.setImage(hoopImg);

        // Load basketball image
        Image ballImg = new Image(getClass().getResourceAsStream("/Image/basketball.png"));
        ballImage.setImage(ballImg);
    }

    @FXML
    private void onAngleBtnClick() {
        System.out.println("Enter");
        shootBtn.setVisible(false);
        errorLabel.setText("");

        String input = angleText.getText();

        System.out.println(input);
        int number = 0;

        try {
            number = Integer.parseInt(input);
            System.out.println("Converted integer: " + number);
            if(number < 0 || number > 90)
            {
                System.out.println("Invalid input. Enter a number from 0 to 90");
                errorLabel.setText("Invalid input. Enter a number from 0 to 90");
            }
            else
            {
                errorLabel.setText("Angle entered is: " + number);
                //want to show button only if angle is correct
                shootBtn.setVisible(true);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter an integer");
            errorLabel.setText("Invalid input. Please enter an integer");
        }

        angle = number*Math.PI/180;
        angleText.clear();
    }

    @FXML
    private void onShootBtnClick() {
        // Shooting logic
        double startX = ballImage.getX();
        double startY = ballImage.getY();
        double v1 = 20; // Initial velocity
        double g = 9.8; // Acceleration due to gravity

        // Calculate end coordinates of the shot
        double endX = calculateEndX(startX, angle);
        double endY = calculateEndY(startY, angle, endX - startX, v1, g);

        // Calculate shot path
        calculateShotPath(startX, startY, endX, endY);

        // Animate shot
        animateShot(ballImage);
    }

    private void calculateShotPath(double startX, double startY, double endX, double endY) {
        // Calculate the number of points along the shot path
        int numPoints = 100;
        pathX = new double[numPoints];
        pathY = new double[numPoints];

        // Calculate coordinates of each point along the shot path
        for (int i = 0; i < numPoints; i++) {
            double t = (double) i / (numPoints - 1);
            double x = startX + (endX - startX) * t;
            double y = calculateParabolicY(startX, startY, endX, endY, x);
            pathX[i] = x;
            pathY[i] = y;
        }
    }

    private double calculateParabolicY(double startX, double startY, double endX, double endY, double x) {
        // Calculate y-coordinate based on parabolic trajectory
        double a = (startY - endY) / Math.pow(startX - endX, 2);
        return a * Math.pow(x - startX, 2) + startY;
    }

    private double calculateEndX(double startX, double angle) {
        // Calculate the x-coordinate where the shot ends
        return startX + 1000 * Math.cos(angle); // Adjust distance for longer shots
    }

    private double calculateEndY(double startY, double angle, double horizontalDistance, double v1, double g) {
        // Calculate the y-coordinate where the shot ends
        return startY - horizontalDistance * Math.tan(angle) - (0.5 * g * Math.pow(horizontalDistance / (v1 * Math.cos(angle)), 2));
    }

    private void animateShot(ImageView ballImageView) {
        // Create a QuadCurve to represent the shot path
        QuadCurve path = new QuadCurve();
        path.setStartX(pathX[0]);
        path.setStartY(pathY[0]);
        path.setControlX(ballImageView.getX() + 50); // Adjust control point as needed
        path.setControlY(ballImageView.getY() - 200); // Adjust control point as needed
        path.setEndX(pathX[pathX.length - 1]);
        path.setEndY(pathY[pathY.length - 1]);

        // Create a PathTransition to animate the basketball along the shot path
        PathTransition pathTransition = new PathTransition();
        pathTransition.setNode(ballImageView);
        pathTransition.setPath(path);
        pathTransition.setDuration(Duration.seconds(2)); // Adjust duration as needed

        // Play the animation
        pathTransition.play();
    }
    /////////////////used chatgpt for this to get started - one way to identify if shot is made or not
    private boolean isShotMade(double endX, double endY) {
        // Define the center coordinates of the hoop
        double hoopCenterX = hoopImage.getX() + hoopImage.getBoundsInLocal().getWidth() / 2;
        double hoopCenterY = hoopImage.getY() + hoopImage.getBoundsInLocal().getHeight() / 2;

        // Calculate the distance between the end coordinates of the shot and the center of the hoop
        double distance = Math.sqrt(Math.pow(endX - hoopCenterX, 2) + Math.pow(endY - hoopCenterY, 2));

        // Define a threshold distance within which the shot is considered made
        double thresholdDistance = 60; // Adjust this value based on your game's requirements

        // If the calculated distance is less than or equal to the threshold distance, consider the shot made
        return distance <= thresholdDistance;
    }
}
