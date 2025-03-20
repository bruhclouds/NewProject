package com.example.projecttest;

public class Faculty extends User {

    private String degree;
    private String researchInterest;
    private String officeLocation;
    private String profilePhoto; // Optional field, can be left as null

    public Faculty(String id, String name, String degree, String researchInterest, String email, String officeLocation) {
        super(id, name, email, "default123"); // Default password as per your code
        this.degree = degree;
        this.researchInterest = researchInterest;
        this.officeLocation = officeLocation;
        this.profilePhoto = "default_photo.jpg"; // You can set a default profile photo or leave it null
    }

    // Getters and setters for the new attributes
    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getResearchInterest() {
        return researchInterest;
    }

    public void setResearchInterest(String researchInterest) {
        this.researchInterest = researchInterest;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
