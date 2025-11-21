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
import java.util.HashMap;
import java.util.Map;

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
    public static void saveJson(String file, JSONArray array) throws IOException {
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

   /* public static User authenticate(String email, String passwordHash) throws IOException {
        JSONArray users = loadJson(USERS_FILE);
        for (int i = 0; i < users.length(); i++) {
            JSONObject obj = users.getJSONObject(i);
            if (obj.getString("email").equals(email) && obj.getString("passwordHash").equals(passwordHash)) {
                if (obj.getString("role").equals("Student")) return new Studentt(obj.getString("username"), email, passwordHash);
                else return new Instructorr(obj.getString("username"), email, passwordHash);
            }
        }
        return null;
    }*/
    
   public static User authenticate(String email, String passwordHash) throws IOException {
    JSONArray users = loadJson(USERS_FILE);
    for (int i = 0; i < users.length(); i++) {
        JSONObject obj = users.getJSONObject(i);
        if (obj.getString("email").equals(email) && obj.getString("passwordHash").equals(passwordHash)) {
            String userId = obj.getString("userId");
            String username = obj.getString("username");
            String role = obj.getString("role");

            if (role.equals("Student")) {
                Studentt student = new Studentt(username, email, passwordHash);
                student.setUserId(userId); // restore stored ID
                return student;
            } else {
                Instructorr instructor = new Instructorr(username, email, passwordHash);
                instructor.setUserId(userId); // restore stored ID
                // also restore createdCourses if present
                if (obj.has("createdCourses")) {
                    JSONArray courses = obj.getJSONArray("createdCourses");
                    for (int j = 0; j < courses.length(); j++) {
                        instructor.addCourse(courses.getString(j));
                    }
                }
                return instructor;
            }
        }
    }
    return null; // authentication failed
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
            
            
            createdCourses.put(courseId);
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


public static ArrayList<Course> getAllCourses1() throws IOException {
    JSONArray coursesArray = loadJson(COURSES_FILE);
    if (coursesArray.length() == 0) 
        return null;

    ArrayList<Course> courses = new ArrayList<>();

    for (int i = 0; i < coursesArray.length(); i++) {
        JSONObject obj = coursesArray.getJSONObject(i);

        // Students
        JSONArray studentsArray = obj.has("students") ? obj.getJSONArray("students") : new JSONArray();
        ArrayList<String> students = new ArrayList<>();
        for (int j = 0; j < studentsArray.length(); j++) {
            students.add(studentsArray.getString(j));
        }

        // Lessons
        JSONArray lessonsArray = obj.has("lessons") ? obj.getJSONArray("lessons") : new JSONArray();
        ArrayList<Lesson> lessons = new ArrayList<>();
        for (int j = 0; j < lessonsArray.length(); j++) {
            JSONObject lessonObj = lessonsArray.getJSONObject(j);
            Lesson lesson = new Lesson(
                lessonObj.getString("lessonId"),
                lessonObj.getString("title"), 
                lessonObj.getString("content")
            );
            lessons.add(lesson);
        }

        // Course object
        Course course = new Course(
            obj.getString("courseId"),
            obj.getString("title"), 
            obj.getString("description"), 
            obj.getString("instructorId"),
            students,
            lessons
        );
        courses.add(course);
    }

    return courses;
}


    
 public static void enrollStudentInCourse(String studentId, String courseId) throws IOException {
    JSONArray courses = loadJson("courses.json");

    for (int i = 0; i < courses.length(); i++) {
        JSONObject course = courses.getJSONObject(i);

        if (course.getString("courseId").equals(courseId)) {
            JSONArray students = course.getJSONArray("students");

            // prevent duplicate enrollment
            if (!students.toList().contains(studentId)) {
                students.put(studentId);
            }

            saveJson("courses.json", courses);
            break;
        }
    }

    // Update student's enrolledCourses
    JSONArray users = loadJson("users.json");
    for (int i = 0; i < users.length(); i++) {
        JSONObject user = users.getJSONObject(i);

        if (user.getString("userId").equals(studentId)) {
            JSONArray enrolled = user.getJSONArray("enrolledCourses");

            if (!enrolled.toList().contains(courseId)) {
                enrolled.put(courseId);
            }

            saveJson("users.json", users);
            break;
        }
    }
}

public static ArrayList<Course> getEnrolledCourses(String studentId) throws IOException {
    ArrayList<Course> result = new ArrayList<>();

    ArrayList<Course> allCourses = getAllCourses1();
    JSONArray users = loadJson("users.json");

    // Get student's enrolled course IDs
    JSONArray enrolledIds = null;
    for (int i = 0; i < users.length(); i++) {
        JSONObject u = users.getJSONObject(i);
        if (u.getString("userId").equals(studentId)) {
            enrolledIds = u.getJSONArray("enrolledCourses");
            break;
        }
    }

    if (enrolledIds == null)
        return result;

    // Match course IDs to Course objects
    for (Course c : allCourses) {
        if (enrolledIds.toList().contains(c.getCourseId())) {
            result.add(c);
        }
    }

    return result;
}




    public static ArrayList<Studentt> getStudentsForCourse(String courseId) throws IOException {

    ArrayList<Studentt> result = new ArrayList<>();

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

 public static void updateStudent(Studentt updatedStudent) throws IOException {
    // Read all users from the JSON file
    JSONArray allUsers = loadJson("users.json");
    
    // Find and update the specific student
    for (int i = 0; i < allUsers.length(); i++) {
        JSONObject user = allUsers.getJSONObject(i);
        if (user.getString("userId").equals(updatedStudent.getUserId())) {
            // Create updated JSON object directly
            JSONObject updatedUser = new JSONObject();
            updatedUser.put("userId", updatedStudent.getUserId());
            updatedUser.put("username", updatedStudent.getUsername());
            updatedUser.put("email", updatedStudent.getEmail());
            updatedUser.put("passwordHash", updatedStudent.getPasswordHash());
            updatedUser.put("role", "student");
            
            // Add enrolled courses
            JSONArray enrolledCourses = new JSONArray();
            for (String courseId : updatedStudent.getEnrolledCourses()) {
                enrolledCourses.put(courseId);
            }
            updatedUser.put("enrolledCourses", enrolledCourses);
            
            // Add progress data
            JSONObject progressJson = new JSONObject();
            HashMap<String, ArrayList<String>> progress = updatedStudent.getProgress();
            for (Map.Entry<String, ArrayList<String>> entry : progress.entrySet()) {
                JSONArray lessonsArray = new JSONArray();
                for (String lessonId : entry.getValue()) {
                    lessonsArray.put(lessonId);
                }
                progressJson.put(entry.getKey(), lessonsArray);
            }
            updatedUser.put("progress", progressJson);
            
            // Replace the old data
            allUsers.put(i, updatedUser);
            break;
        }
    }
    
    // Save the updated array back to the file
    saveJson("users.json", allUsers);
}
}