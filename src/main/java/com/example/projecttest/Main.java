package com.example.projecttest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the login.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projecttest/login.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("University Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}