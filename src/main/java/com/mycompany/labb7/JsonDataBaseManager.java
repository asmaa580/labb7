/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.labb7;

/**
 *
 * @author aseel
 */
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class JsonDataBaseManager {
    private static final String USERS_FILE = "users.json";
    private static final String COURSES_FILE = "courses.json";

    public static JSONArray loadJson(String file) throws IOException {
        Path path = Paths.get(file);

        
        if (!Files.exists(path)) {
            Files.writeString(path, "[]");
            return new JSONArray(); 
        }

        String content = Files.readString(path);
        if (content.isEmpty()) content = "[]"; 
        return new JSONArray(content);
    }

    // Save JSONArray to file
    private static void saveJson(String file, JSONArray array) throws IOException {
        Files.writeString(Paths.get(file), array.toString(4));
    }

    // ---------------- Users ----------------

    public static void addUser(User user) throws IOException {
        JSONArray users = loadJson(USERS_FILE);
        for (int i = 0; i < users.length(); i++) {
            if (users.getJSONObject(i).getString("email").equals(user.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
        }

        JSONObject obj = new JSONObject();
        obj.put("userId", user.getUserId());
        obj.put("username", user.getUsername());
        obj.put("email", user.getEmail());
        obj.put("passwordHash", user.getPasswordHash());
        obj.put("role", user.getRole());
        if (user instanceof Studentt) {
            obj.put("enrolledCourses", new JSONArray());
            obj.put("progress", new JSONObject());
        } else if (user instanceof Instructorr) {
            obj.put("createdCourses", new JSONArray());
        }
        users.put(obj);
        saveJson(USERS_FILE, users);
    }

    public static User authenticate(String email, String passwordHash) throws IOException {
        JSONArray users = loadJson(USERS_FILE);
        for (int i = 0; i < users.length(); i++) {
            JSONObject obj = users.getJSONObject(i);
            if (obj.getString("email").equals(email) && obj.getString("passwordHash").equals(passwordHash)) {
                if (obj.getString("role").equals("Student")) return new Studentt(obj.getString("username"), email, passwordHash);
                else return new Instructorr(obj.getString("username"), email, passwordHash);
            }
        }
        return null;
    }

    // ---------------- Courses ----------------

    public static void addCourse(Course course) throws IOException {
        JSONArray courses = loadJson(COURSES_FILE);
        JSONObject obj = new JSONObject();
        obj.put("courseId", course.getCourseId());
        obj.put("title", course.getTitle());
        obj.put("description", course.getDescription());
        obj.put("instructorId", course.getInstructorId());
        obj.put("lessons", new JSONArray());
        obj.put("students", new JSONArray());
        courses.put(obj);
        saveJson(COURSES_FILE, courses);
    }

    public static ArrayList<Course> getAllCourses() throws IOException {
        JSONArray coursesArray = loadJson(COURSES_FILE);
        ArrayList<Course> courses = new ArrayList<>();
        for (int i = 0; i < coursesArray.length(); i++) {
            JSONObject obj = coursesArray.getJSONObject(i);
            Course course = new Course(obj.getString("title"), obj.getString("description"), obj.getString("instructorId"));
            courses.add(course);
        }
        return courses;
    }
}