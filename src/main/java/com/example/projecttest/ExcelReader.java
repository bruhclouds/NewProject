package com.example.projecttest;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {
    private String filePath;

    public ExcelReader(String filePath) {
        this.filePath = filePath;
    }

    // Method to load students from the Excel file
    public List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(2); // Assuming the students data is in the 3rd sheet (index 2)

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                // Safely retrieve data from cells
                String id = getCellValue(row, 0);
                String name = getCellValue(row, 1);
                String email = getCellValue(row, 4);
                String enrolledCourses = getCellValue(row, 5); // Assuming this column holds the courses the student is enrolled in
                String grades = getCellValue(row, 6); // Assuming this column holds the grades
                String password = getCellValue(row, 7); // Assuming this column holds the password

                // Check if mandatory fields are not null before adding to list
                if (id != null && name != null && email != null && enrolledCourses != null && password != null) {
                    students.add(new Student(id, name, email, enrolledCourses, grades, password)); // Pass password
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

    // Method to load faculties from the Excel file
    public List<Faculty> loadFaculties() {
        List<Faculty> faculties = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(3); // Assuming the faculty data is in the 4th sheet (index 3)

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                // Safely retrieve data from cells
                String id = getCellValue(row, 0);
                String name = getCellValue(row, 1);
                String degree = getCellValue(row, 2);  // Degree
                String researchInterest = getCellValue(row, 3);  // Research Interest
                String email = getCellValue(row, 4);
                String officeLocation = getCellValue(row, 5);  // Office Location
                String password = getCellValue(row, 6);  // Password

                // Check if mandatory fields are not null before adding to list
                if (id != null && name != null && email != null && password != null) {
                    faculties.add(new Faculty(id, name, degree, researchInterest, email, officeLocation));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return faculties;
    }

    // Helper method to safely get the value from a cell
    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            return null; // Return null if the cell does not exist
        }
        return cell.getStringCellValue();
    }
}
