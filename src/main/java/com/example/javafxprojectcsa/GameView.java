package com.example.javafxprojectcsa;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Random;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurve;

import javafx.animation.PathTransition;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class GameView implements Initializable{
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
    ImageView personImage;
    @FXML
    QuadCurve testCurve;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label shotResultLabel;

    ///trying
    private double[] pathX;
    private double[] pathY;
    //private int pathIndex = 0;

    @FXML
    public void initialize(URL location, ResourceBundle resources)
    {
        File file = new File("src/main/resources/Image/hoopImage.png");
        Image image = new Image(file.toURI().toString());
        hoopImage.setImage(image);

        File file1 = new File("src/main/resources/Image/basketball.png");
        Image image1 = new Image(file1.toURI().toString());
        ballImage.setImage(image1);

        File file2 = new File("src/main/resources/Image/personImage.png");
        Image image2 = new Image(file2.toURI().toString());
        personImage.setImage(image2);
    }

    //does the action for the angle button on the screen
    @FXML
    private void onAngleBtnClick() {
        System.out.println("Enter");
        shootBtn.setVisible(false);
        shootBtn.setVisible(false);
        errorLabel.setText("");
        String input = angleText.getText();
        System.out.println(input);
        int number = 0;
        //check if it is a valid angle number from 0 to 90
        //convert text to number
        //if it is a valid angle, show shoot button and allow the person to shoot
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
        errorLabel.setVisible(true);

    }

    @FXML
    private void onShootBtnClick(){
        System.out.println("shoot");
        //need to calculate shot using angle and then move the image
        double endY;
        double endX;
        double startX = 50;
        double startY = 150;
        int v1 = 20;
        endX = 10*(2*Math.pow(v1, 2)*Math.cos(angle)*Math.sin(angle))/9.8;
        endY = 150;
        System.out.println(endX + ", " + endY);

        //trying
        // Calculate shot path
        calculateShotPath(startX, startY, endX, endY, v1);
        //traceShotPath(ballImage);

        boolean isShotMade = isShotMade(endX, endY);

        /////////////////////himani added to see if we could identify if shot is made and record it
        if (isShotMade(endX, endY)) {
            shotResultLabel.setText("Shot made!");
            System.out.println("Shot made!");
            successfulShots++;
            // updateShotsMadeLabel();
        } else {
            shotResultLabel.setText("Shot missed!");
            System.out.println("Shot missed!");
        }
        //note only for testing if the label works
        //successfulShots++;
        shotsMadeLabel.setText("Shots Made: " + successfulShots);
        shootBtn.setVisible(false);
        errorLabel.setVisible(false);
    }

    //trying
    private void calculateShotPath(double startX, double startY, double endX, double endY, int v1) {
        // Calculate the number of points along the shot path
        //should actually base it on the time it takes the ball to fall
        //we can calculate that using the formula to find how much time it would actually take for the
        //ball to fall
        //then use maybe 100 points divided from that time in order to calculate for x and y
        //cuz then it will be more accurate
        //not sure if we wnat to go through the trouble or not though
        //WE NEED TO SET A SCALE-LIKE HOW MANY PIXELS IS A METER IN ORDER TO ACTUALLY HAVE THE BALL TRAVEL FAR ENOUGH
        int numPoints = 100;
        pathX = new double[numPoints];
        pathY = new double[numPoints];
        double y = startY;

        // Calculate coordinates of each point along the shot path
        for (int i = 0; i < numPoints; i++) {
            //double t = (double) i / (numPoints - 1);
            //x should increase a constant amount every time
            //double x = (1 - t) * startX + t * endX;
            double x = i*(startX + endX)/numPoints;
            //y should increase, then decrease because it is a parabola
            //so maybe do an if statement--if i <= numpoints/2, then y increase
            //but if i > numpoints/2, then y decrease
            //double y = (1 - t) * startY + t * endY;
            if(i <= numPoints/2)
            {
                y = y - 5;
            } else {
                y = y + 5;
            }
            pathX[i] = x;
            pathY[i] = y;
        }
        //for calculating quad curve
        //i'll probably have to rearrange the whole thing later
        //I"ll probably delete late
        //THIS IS JUST TO TEST WHETHER OR NOT THE BALL IS ACTUALLY GOING THE RIGHT WAY

        double height= startX/(v1*Math.cos(angle));
        double midX = (startX + endX)/2;
        double midY = startY - 10*(startX*Math.tan(angle) + 0.5*9.8*Math.pow(height, 2));
        var cpX = 2*midX -pathX[0]/2 -pathX[99]/2;
        var cpY = 2*midY -pathY[0]/2 -pathY[99]/2;
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
        pathTransitionTest.play();
    }

    /*
    private void traceShotPath(ImageView ballImageView) {
        // Create a PathTransition to animate the basketball along the shot path
        PathTransition pathTransition = new PathTransition();
        pathTransition.setNode(ballImageView);

        // Create a Line object representing the shot path
        Line path = new Line();
        path.setStartX(pathX[0]);
        path.setStartY(pathY[0]);
        path.setEndX(pathX[50]);
        path.setEndY(pathY[50]);

        // Set the duration of the path transition
        pathTransition.setDuration(Duration.seconds(2)); // Adjust duration as needed

        // Set the path of the path transition
        pathTransition.setPath(path);

        // Play the path transition animation
        pathTransition.play();
        System.out.println("first");


        //I need to put a delay in here but don't know how
        Line path1 = new Line();
        path1.setStartX(pathX[51]);
        path1.setStartY(pathY[51]);
        path1.setEndX(pathX[99]);
        path1.setEndY(pathY[99]);

        pathTransition.setDuration(Duration.seconds(2));
        pathTransition.setPath(path1);
        pathTransition.play();
        System.out.println("second");

    }

     */

    /////////////////used chatgpt for this to get started - one way to identify if shot is made or not
    private boolean isShotMade(double endX, double endY) {
        // Logic to determine if the shot is made based on end coordinates
        // Example: Check if the end coordinates are within the hoop area
        //problem with above is the end coordinates are not going to be within the hoop
        // You need to implement this method according to your game's logic
        // For demonstration purposes, let's assume if endX is within a certain range, the shot is made
        //return endX >= 200 && endX <= 300;

        // Hypothetical game logic: Consider the shot made if it falls within a certain distance from the center of the hoop

        // Coordinates of the center of the hoop (hypothetical values for demonstration)
        //BRO IDK HOW TO FIND THE CENTER OF THE HOOP AHSHIAHISHIAHIHSIJAINBIAHISHI
        //rn it should be 643 and 90
        /*
        if scale up, then it should be:
        int hoopCenterX = borderPane.getWidth() - hoopImage.getWidth/2;
        int hoopCenterY = top hbox height + hoopImage.getHeight/2;
         */
        /*
        double hoopCenterX = 634;
        double hoopCenterY = 90;

        // Calculate the distance between the end coordinates of the shot and the center of the hoop
        double distance = Math.sqrt(Math.pow(endX - hoopCenterX, 2) + Math.pow(endY - hoopCenterY, 2));

        // Define a threshold distance within which the shot is considered made (hypothetical value for demonstration)
        double thresholdDistance = 60; // Adjust this value based on your game's requirements

        // If the calculated distance is less than or equal to the threshold distance, consider the shot made
        return distance <= thresholdDistance;
*/

        //jwijisjijdiednieifnfinr ignore this
        double ballMinX = ballImage.getBoundsInParent().getMinX();
        double ballMaxX = ballImage.getBoundsInParent().getMaxX();
        double ballMinY = ballImage.getBoundsInParent().getMinY();
        double ballMaxY = ballImage.getBoundsInParent().getMaxY();

        double hoopMinX = hoopImage.getBoundsInParent().getMinX();
        double hoopMaxX = hoopImage.getBoundsInParent().getMaxX();
        double hoopMinY = hoopImage.getBoundsInParent().getMinY();
        double hoopMaxY = hoopImage.getBoundsInParent().getMaxY();

        // Check if the ball intersects with the hoop
        boolean intersectsX = ballMaxX >= hoopMinX && ballMinX <= hoopMaxX;
        boolean intersectsY = ballMaxY >= hoopMinY && ballMinY <= hoopMaxY;

        return intersectsX && intersectsY;

        /*
        double hoopCenterX = hoopImage.getBoundsInParent().getMinX() + hoopImage.getBoundsInParent().getWidth() / 2;
        double hoopCenterY = hoopImage.getBoundsInParent().getMinY() + hoopImage.getBoundsInParent().getHeight() / 2;

        // Calculate the distance between the end coordinates of the shot and the center of the hoop
        double distance = Math.sqrt(Math.pow(endX - hoopCenterX, 2) + Math.pow(endY - hoopCenterY, 2));

        // Define a threshold distance within which the shot is considered made
        double thresholdDistance = 5; // Adjust this value based on your game's requirements

        // If the calculated distance is less than or equal to the threshold distance, consider the shot made
        return distance <= thresholdDistance;
         */
    }
}


