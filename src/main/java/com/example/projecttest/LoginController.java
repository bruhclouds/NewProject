package com.example.projecttest;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private List<Student> students;
    private List<Faculty> faculties;

    // Initialize method to load data from Excel
    public void initialize() {
        loadDataFromExcel();
    }

    // Method to load the data from the Excel file
    private void loadDataFromExcel() {
        ExcelReader reader = new ExcelReader("UMS_Data.xlsx");
        students = reader.loadStudents();
        faculties = reader.loadFaculties();
        System.out.println("Loaded " + students.size() + " students.");
        System.out.println("Loaded " + faculties.size() + " faculties.");
    }

    @FXML
    private void handleLogin() {
        // Clear previous error messages
        errorLabel.setText("");

        // Get the login credentials
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Admin login check
        if (isAdmin(username, password)) {
            loadDashboard("admin_dashboard.fxml");
        } else if (isValidStudent(username, password)) {
            loadDashboard("student_dashboard.fxml");
        } else if (isValidFaculty(username, password)) {
            loadDashboard("faculty_dashboard.fxml");
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }

    // Check if the user is an admin
    private boolean isAdmin(String username, String password) {
        return "admin".equals(username) && "admin123".equals(password);
    }

    // Check if the user is a valid student
    private boolean isValidStudent(String username, String password) {
        return students.stream().anyMatch(student -> student.getId().equals(username) && student.getPassword().equals(password));
    }

    // Check if the user is a valid faculty
    private boolean isValidFaculty(String username, String password) {
        return faculties.stream().anyMatch(faculty -> faculty.getId().equals(username) && faculty.getPassword().equals(password));
    }

    // Method to load the appropriate dashboard after a successful login
    private void loadDashboard(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Error loading dashboard.");
        }
    }
}
