/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.labb7;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
/**
 *
 * @author u s e r
 */
public class Course {
    private String courseId;
    private String title;
    private String description;
    private String instructorId;
    private ArrayList<Lesson> lessons;
    private ArrayList<String> students;

    public Course(String courseId,String title, String description, String instructorId,ArrayList<String> students,ArrayList<Lesson> lessons) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.lessons = lessons;
        this.students = students;
    }
    
    public Course(String title, String description, String instructorId) {
        
        Random rand = new Random();
       this.courseId = String.valueOf(rand.nextInt(10000));
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.lessons = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public String getCourseId() { return courseId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getInstructorId() { return instructorId; }
    public ArrayList<Lesson> getLessons() { return lessons; }
    public ArrayList<String> getStudents() { return students; }

    public void addLesson(Lesson lesson) { lessons.add(lesson); }
    public void removeLesson(String lessonId) {
        lessons.removeIf(l -> l.getLessonId().equals(lessonId));
    }

    public void enrollStudent(String studentId) {
        if (!students.contains(studentId)) students.add(studentId);
    }
}
