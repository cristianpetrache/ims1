package com.incrys;

import java.util.Date;

/**
 * Created by User on 7/10/2017.
 */
public class User {
    private String username;
    private Date dateOfBirth;
    private String email;
    private String password;

    public User(String username, Date dateOfBirth, String email, String passwordHash) {
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
