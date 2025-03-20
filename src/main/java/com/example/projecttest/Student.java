package com.example.projecttest;

public class Student {
    private String id;
    private String name;
    private String email;
    private String enrolledCourses; // Comma-separated list of courses
    private String grades;
    private String password; // Added password field

    // Constructor including password
    public Student(String id, String name, String email, String enrolledCourses, String grades, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.enrolledCourses = enrolledCourses;
        this.grades = grades;
        this.password = password;
    }

    // Getters for all fields
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getEnrolledCourses() {
        return enrolledCourses;
    }

    public String getGrades() {
        return grades;
    }

    public String getPassword() {
        return password;
    }

    // Setters for updating password and other fields (optional)
    public void setPassword(String password) {
        this.password = password;
    }
}
