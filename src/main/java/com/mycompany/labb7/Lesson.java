/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.labb7;

/**
 *
 * @author aseel
 */
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Lesson {
    private String lessonId;
    private String title;
    private String content;
    private ArrayList<String> resources;

    public Lesson(String lessonId,String title, String content,ArrayList<String> resources) {
        this.lessonId =lessonId;
        this.title = title;
        this.content = content;
        this.resources = resources;
    }
    
    public Lesson(String lessonId,String title, String content) {
        this.lessonId =lessonId;
        this.title = title;
        this.content = content;
        this.resources = new ArrayList<>();
    }
    
    public Lesson(String title, String content) {
       Random rand = new Random();
       this.lessonId = String.valueOf(rand.nextInt(10000));
        this.title = title;
        this.content = content;
        this.resources = new ArrayList<>();
    }

    public String getLessonId() { return lessonId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public ArrayList<String> getResources() { return resources; }

    public void addResource(String resource) { resources.add(resource); }
}


