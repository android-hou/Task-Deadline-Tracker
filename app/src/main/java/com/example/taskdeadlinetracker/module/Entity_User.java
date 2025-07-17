package com.example.taskdeadlinetracker.module;

import java.io.Serializable;
import java.util.Date;

public class Entity_User implements Serializable {
    private int id;
    private String username;
    private String email;
    private String password;
    private Date createdAt;
    private Date updatedAt;

    public Entity_User() {}

    public Entity_User(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getter & Setter cho id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Getter & Setter cho createdAt
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // Getter & Setter cho updatedAt
    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
