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
        updateInstructorCourses(course.getInstructorId(), course.getCourseId());
    }
   private static void updateInstructorCourses(String instructorId, String courseId) throws IOException {
    JSONArray users = loadJson(USERS_FILE);
    
    System.out.println("DEBUG: Trying to update instructor courses for ID: " + instructorId);
    
    // First, get the course title from courses.json
    String courseTitle = getCourseTitleById(courseId);
    
    if (courseTitle == null) {
        System.out.println("DEBUG: ERROR - Could not find course title for ID: " + courseId);
        return;
    }
    
    System.out.println("DEBUG: Found course title: " + courseTitle);
    
    // Find ANY instructor and add the course TITLE
    for (int i = 0; i < users.length(); i++) {
        JSONObject user = users.getJSONObject(i);
        //if (user.getString("role").equals("Instructor")) 
        if (user.getString("userId").equals(instructorId))
        {
            System.out.println("DEBUG: Found an instructor - ID: " + user.getString("userId"));
            
            JSONArray createdCourses;
            if (user.has("createdCourses")) {
                createdCourses = user.getJSONArray("createdCourses");
            } else {
                createdCourses = new JSONArray();
            }
            
            // Add the course TITLE instead of ID
            createdCourses.put(courseTitle);
            user.put("createdCourses", createdCourses);
            System.out.println("DEBUG: Added course TITLE to instructor: " + user.getString("email"));
            break; // Stop after first instructor found
        }
    }
    
    saveJson(USERS_FILE, users);
}

// ADD THIS NEW METHOD to get course title by ID
private static String getCourseTitleById(String courseId) throws IOException {
    JSONArray courses = loadJson(COURSES_FILE);
    
    for (int i = 0; i < courses.length(); i++) {
        JSONObject course = courses.getJSONObject(i);
        if (course.getString("courseId").equals(courseId)) {
            return course.getString("title");
        }
    }
    return null; // Course not found
} 

    public static ArrayList<Course> getAllCourses() throws IOException {
    JSONArray coursesArray = loadJson(COURSES_FILE);
    ArrayList<Course> courses = new ArrayList<>(); // Always return a list, never null
    
    if (coursesArray.length() == 0) {
        System.out.println("DEBUG: No courses found in JSON file");
        return courses; // Return empty list instead of null
    }
    
    // ... rest of your existing code
    for (int i = 0; i < coursesArray.length(); i++) {
        JSONObject obj = coursesArray.getJSONObject(i);
        // ... your existing parsing code
    }
    
    System.out.println("DEBUG: Loaded " + courses.size() + " courses from JSON");
    return courses;
}
    public static ArrayList<User> getStudentsForCourse(String courseId) throws IOException {

    ArrayList<User> result = new ArrayList<>();

    JSONArray courses = loadJson(COURSES_FILE);
    JSONArray users = loadJson(USERS_FILE);

    // find the course by ID
    JSONObject targetCourse = null;
    for (int i = 0; i < courses.length(); i++) {
        JSONObject c = courses.getJSONObject(i);
        if (c.getString("courseId").equals(courseId)) {
            targetCourse = c;
            break;
        }
    }

    if (targetCourse == null) {
        return result; // no such course
    }

    // extract student IDs
    JSONArray studentIds = targetCourse.getJSONArray("students");

    // match each student ID with a user in users.json
    for (int i = 0; i < users.length(); i++) {
        JSONObject u = users.getJSONObject(i);

        if (studentIds.toList().contains(u.getString("userId"))) {

            result.add(new Studentt(
                u.getString("username"),
                u.getString("email"),
                u.getString("passwordHash")
            ));
        }
    }

    return result;
}

}