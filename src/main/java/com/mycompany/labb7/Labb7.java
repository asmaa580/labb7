/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.labb7;
//import model.*;
//import util.*;
import java.io.IOException;

 /**
 *
 * @author USER
 */
public class Labb7 {
/*

    public static void main(String[] args) {
        try {
            System.out.println("=== SkillForge Backend Test ===");

            // 1️⃣ Sign up Instructor
            String instrEmail = "instructor@example.com";
            String instrPass = "pass123";
            String instrHash = SecurityHashing.hashPassword(instrPass);
            

            Instructorr ins= new Instructorr("JohnInstructor", instrEmail, instrHash);
            JsonDataBaseManager.addUser(ins);
            System.out.println("Instructor signed up: " + ins.getUsername());

            // 2️⃣ Sign up Student
            String studentEmail = "student@example.com";
            String studentPass = "student123";
            String studentHash = SecurityHashing.hashPassword(studentPass);

            student st = new student("JaneStudent", studentEmail, studentHash);
            JsonDataBaseManager.addUser(st);
            System.out.println("Student signed up: " + st.getUsername());

            // 3️⃣ Login Test
            User loginInstr = JsonDataBaseManager.authenticate(instrEmail, instrHash);
            if (loginInstr != null)
                System.out.println("Instructor logged in successfully: " + loginInstr.getUsername());

            User loginStudent = JsonDataBaseManager.authenticate(studentEmail, studentHash);
            if (loginStudent != null)
                System.out.println("Student logged in successfully: " + loginStudent.getUsername());

            // 4️⃣ Instructor creates a course
            Course c = new Course("Java Programming", "Learn Java from scratch", ins.getUserId());
            JsonDataBaseManager.addCourse(c);
            ins.addCourse(c.getCourseId());
            System.out.println("Course created: " + c.getTitle());

            // 5️⃣ Instructor adds lessons
            Lesson lesson1 = new Lesson("Introduction", "Java basics");
            Lesson lesson2 = new Lesson("OOP Concepts", "Inheritance, Polymorphism");
            c.addLesson(lesson1);
            c.addLesson(lesson2);
            System.out.println("Lessons added to course: ");
            for (Lesson l : c.getLessons()) System.out.println(" - " + l.getTitle());

            // 6️⃣ Student enrolls in course
            st.enrollCourse(c.getCourseId());
            c.enrollStudent(st.getUserId());
            System.out.println(st.getUsername() + " enrolled in " + c.getTitle());

            // 7️⃣ Student completes lessons
            st.completeLesson(c.getCourseId(), lesson1.getLessonId());
            st.completeLesson(c.getCourseId(), lesson2.getLessonId());
            System.out.println(st.getUsername() + " completed lessons in " + c.getTitle());

            // 8️⃣ Display student progress
            System.out.println("Student Progress:");
            st.getProgress().forEach((courseId, completedLessons) -> {
                System.out.println("Course ID: " + courseId);
                for (String lid : completedLessons) System.out.println(" - Completed Lesson ID: " + lid);
            });

            System.out.println("=== Backend Test Complete ===");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }*/
}
