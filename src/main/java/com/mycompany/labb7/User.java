/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.labb7;
import java.util.Random;
import java.util.UUID;

public abstract class User {
    protected String userId;
    protected String username;
    protected String email;
    protected String passwordHash;

    public User(String username, String email, String passwordHash) {
        Random rand = new Random();
       this.userId = String.valueOf(rand.nextInt(10000));
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }

    public abstract String getRole(); 
}
