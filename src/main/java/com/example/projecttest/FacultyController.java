package com.example.projecttest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FacultyController {
    @FXML private VBox addFacultyForm;
    @FXML private TextField idField;
    @FXML private VBox dashboardView;
    @FXML private TextField nameField;
    @FXML private TextField degreeField;
    @FXML private TextField researchInterestField;
    @FXML private TextField emailField;
    @FXML private TextField officeLocationField;
    @FXML private TextField coursesField;
    @FXML private Label errorLabel;
    private static final String FILE_PATH = "UMS_Data.xlsx"; // Excel file path
    private String loggedInFacultyId; // To store the logged-in faculty ID

    // Default constructor required by FXML
    public FacultyController() {
        // Default constructor
    }

    // Setter to set the logged-in faculty ID
    public void setLoggedInFacultyId(String facultyId) {
        this.loggedInFacultyId = facultyId;
        System.out.println("Logged in Faculty ID: " + this.loggedInFacultyId);
    }

    // Retrieve all faculty members
    public List<Faculty> getFaculties() {
        List<Faculty> facultyList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(FILE_PATH));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Faculties");
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                Faculty faculty = createFacultyFromRow(row);
                facultyList.add(faculty);
            }
        } catch (IOException e) {
            showError("Error reading faculty data: " + e.getMessage());
        }
        return facultyList;
    }

    // Create Faculty object from Row data
    private Faculty createFacultyFromRow(Row row) {
        return new Faculty(
                row.getCell(0).getStringCellValue(), // Faculty ID
                row.getCell(1).getStringCellValue(), // Name
                row.getCell(2).getStringCellValue(), // Degree
                row.getCell(3).getStringCellValue(), // Research Interest
                row.getCell(4).getStringCellValue(), // Email
                row.getCell(5).getStringCellValue()  // Office Location
        );
    }

    // Add a new faculty member
    public boolean addFaculty(String id, String name, String degree, String researchInterest, String email, String officeLocation) {
        if (isFacultyIdExist(id)) {
            showError("Faculty ID already exists!");
            return false;
        }

        try (FileInputStream fis = new FileInputStream(new File(FILE_PATH));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Faculties");
            int lastRow = sheet.getLastRowNum();
            Row newRow = sheet.createRow(lastRow + 1);

            newRow.createCell(0).setCellValue(id);
            newRow.createCell(1).setCellValue(name);
            newRow.createCell(2).setCellValue(degree);
            newRow.createCell(3).setCellValue(researchInterest);
            newRow.createCell(4).setCellValue(email);
            newRow.createCell(5).setCellValue(officeLocation);
            newRow.createCell(6).setCellValue("default123"); // Default password

            try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
                workbook.write(fos);
            }

            return true;
        } catch (IOException e) {
            showError("Error adding faculty member: " + e.getMessage());
        }
        return false;
    }

    // Check if Faculty ID exists
    private boolean isFacultyIdExist(String id) {
        return getFaculties().stream().anyMatch(faculty -> faculty.getId().equals(id));
    }

    // Display error message using Alert
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleAddFaculty() {
        // Retrieve the values entered in the form fields
        String id = idField.getText();
        String name = nameField.getText();
        String degree = degreeField.getText();
        String researchInterest = researchInterestField.getText();
        String email = emailField.getText();
        String officeLocation = officeLocationField.getText();

        // Validate form fields (ensure no field is empty)
        if (id.isEmpty() || name.isEmpty() || degree.isEmpty() || researchInterest.isEmpty() || email.isEmpty() || officeLocation.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;  // Exit the method early if validation fails
        }

        // Validate email format using a regular expression
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errorLabel.setText("Please enter a valid email address.");
            return;  // Exit the method early if email is invalid
        }

        // Attempt to add the faculty member
        boolean success = addFaculty(id, name, degree, researchInterest, email, officeLocation);

        if (success) {
            errorLabel.setText("Faculty added successfully.");
            clearFields();  // Clear the form fields after successful addition
            // Optionally, you can switch views here, for example:
            // dashboardView.setVisible(true);  // Show the dashboard after adding faculty
            // addFacultyForm.setVisible(false);  // Hide the add faculty form
        } else {
            errorLabel.setText("Error adding faculty.");
        }
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        degreeField.clear();
        researchInterestField.clear();
        emailField.clear();
        officeLocationField.clear();
        coursesField.clear();
    }

    // Update faculty member's information
    public boolean updateFaculty(String facultyId, String name, String degree, String researchInterest, String officeLocation, String courses) {
        try (FileInputStream fis = new FileInputStream(new File(FILE_PATH));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Faculties");

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                if (row.getCell(0).getStringCellValue().equals(facultyId)) {
                    row.getCell(1).setCellValue(name);
                    row.getCell(2).setCellValue(degree);
                    row.getCell(3).setCellValue(researchInterest);
                    row.getCell(5).setCellValue(officeLocation);
                    row.getCell(6).setCellValue(courses);
                    break;
                }
            }

            try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
                workbook.write(fos);
            }
            return true;
        } catch (IOException e) {
            showError("Error updating faculty member: " + e.getMessage());
        }
        return false;
    }

    // Handle updating faculty member
    @FXML
    private void handleUpdateFaculty() {
        String id = idField.getText();
        String name = nameField.getText();
        String degree = degreeField.getText();
        String researchInterest = researchInterestField.getText();
        String officeLocation = officeLocationField.getText();
        String courses = coursesField.getText();

        boolean success = updateFaculty(id, name, degree, researchInterest, officeLocation, courses);
        if (success) {
            errorLabel.setText("Faculty updated successfully.");
        } else {
            errorLabel.setText("Error updating faculty.");
        }
    }

    // Delete a faculty member
    public boolean deleteFaculty(String facultyId) {
        try (FileInputStream fis = new FileInputStream(new File(FILE_PATH));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Faculties");
            int rowToDelete = -1;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                if (row.getCell(0).getStringCellValue().equals(facultyId)) {
                    rowToDelete = row.getRowNum();
                    break;
                }
            }

            if (rowToDelete != -1) {
                sheet.removeRow(sheet.getRow(rowToDelete));
            }

            try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
                workbook.write(fos);
            }
            return rowToDelete != -1;
        } catch (IOException e) {
            showError("Error deleting faculty member: " + e.getMessage());
        }
        return false;
    }

    // Handle delete faculty
    @FXML
    private void handleDeleteFaculty() {
        String facultyIdToDelete = idField.getText(); // Get input from the form

        boolean success = deleteFaculty(facultyIdToDelete);
        if (success) {
            errorLabel.setText("Faculty deleted successfully.");
        } else {
            errorLabel.setText("Error deleting faculty.");
        }
    }

    // For students to view faculty profiles
    public Faculty getFacultyProfileForStudent(String facultyId) {
        return getFacultyProfile(facultyId);
    }

    // Fetch faculty profile by ID
    private Faculty getFacultyProfile(String facultyId) {
        List<Faculty> faculties = getFaculties();

        for (Faculty faculty : faculties) {
            if (faculty.getId().equals(facultyId)) {
                return faculty;
            }
        }
        return null;
    }

    // Handle view faculty profile
    @FXML
    private void handleViewFacultyProfile() {
        System.out.println("View Faculty Profile clicked");
        Faculty faculty = getFacultyProfileForStudent("F123"); // Example: faculty ID
        if (faculty != null) {
            System.out.println("Faculty Name: " + faculty.getName());
            System.out.println("Email: " + faculty.getEmail());
            System.out.println("Office Location: " + faculty.getOfficeLocation());
            System.out.println("Research Interests: " + faculty.getResearchInterest());
        }
    }

    public void handleViewProfile(ActionEvent actionEvent) {}

    public void handleViewCourses(ActionEvent actionEvent) {}

    public void handleLogout(ActionEvent actionEvent) {}

    public void handleSaveFaculty(ActionEvent actionEvent) {
        handleAddFaculty();  // Delegate to add faculty handler
    }

    public void handleBackToDashboard(ActionEvent actionEvent) {
        addFacultyForm.setVisible(false);
        dashboardView.setVisible(true);
    }

    public void handleViewFaculties(ActionEvent actionEvent) {}

    public void handleManageCourses(ActionEvent actionEvent) {}

    public void handleViewReports(ActionEvent actionEvent) {}

    public void handleAssignCourses(ActionEvent actionEvent) {
    }

    public void handleViewFacultyProfiles(ActionEvent actionEvent) {
    }

    public void handleEditFaculty(ActionEvent actionEvent) {
    }
}
