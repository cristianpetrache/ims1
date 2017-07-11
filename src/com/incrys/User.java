package com.incrys;

import java.util.Date;

public class User {
    private String username;
    private Date dateOfBirth;
    private String email;
    private String password;
    private String confirmedPassword;
    private String secretCode;

    public User(String username, Date dateOfBirth, String email, String password, String confirmedPassword, String secretCode) {
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = password;
        this.confirmedPassword=confirmedPassword;
        this.secretCode = secretCode;
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

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", confirmedPassword='" + confirmedPassword + '\'' +
                ", secretCode='" + secretCode + '\'' +
                '}';
    }

    public boolean validateUsername(){
        boolean isValid = true;
        if(this.username.length()>100) isValid=false;
        if(this.username.length()==0) isValid=false;
        return isValid;
    }

}
