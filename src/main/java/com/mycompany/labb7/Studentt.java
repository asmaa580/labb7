/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.labb7;
import java.util.ArrayList;
import java.util.HashMap;
/**
 *
 * @author aseel
 */

public class Studentt extends User {
    private ArrayList<String> enrolledCourses;
    private HashMap<String, ArrayList<String>> progress; // courseId -> list of completed lessonIds

    public Studentt(String username, String email, String passwordHash) {
        super(username, email, passwordHash);
        enrolledCourses = new ArrayList<>();
        progress = new HashMap<>();
    }

    @Override
    public String getRole() { return "Student"; }

    public ArrayList<String> getEnrolledCourses() { return enrolledCourses; }
    public HashMap<String, ArrayList<String>> getProgress() { return progress; }

    public void enrollCourse(String courseId) {
        if (!enrolledCourses.contains(courseId)) {
            enrolledCourses.add(courseId);
            progress.put(courseId, new ArrayList<>());
        }
    }

    public void completeLesson(String courseId, String lessonId) {
        if (progress.containsKey(courseId)) {
            ArrayList<String> completed = progress.get(courseId);
            if (!completed.contains(lessonId)) completed.add(lessonId);
        }
    }
}

