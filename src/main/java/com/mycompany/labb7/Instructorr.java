/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.labb7;
import java.util.ArrayList;

public class Instructorr extends User {
    private ArrayList<String> createdCourses;

    public Instructorr(String username, String email, String passwordHash) {
        super(username, email, passwordHash);
        createdCourses = new ArrayList<>();
    }

    @Override
    public String getRole() { return "Instructor"; }

    public ArrayList<String> getCreatedCourses() { return createdCourses; }

    public void addCourse(String courseId) {
        if (!createdCourses.contains(courseId)) createdCourses.add(courseId);
    }
}
