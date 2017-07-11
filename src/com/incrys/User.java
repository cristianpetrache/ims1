package com.incrys;

import java.time.LocalDate;
import java.util.Date;
import java.util.StringJoiner;

public class User {
    private String username;
    private LocalDate dateOfBirth;
    private String email;
    private String password;
    private String confirmedPassword;
    private String secretCode;

    public StringJoiner message;

    public User(String username, LocalDate dateOfBirth, String email, String password, String confirmedPassword, String secretCode) {
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = password;
        this.confirmedPassword=confirmedPassword;
        this.secretCode = secretCode;

        this.message=new StringJoiner("\n");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
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

    private boolean validateUsername(){
        boolean isValid = true;
        if(this.username.length()>100) {
            isValid = false;
            message.add("Username too long.");
            return isValid;
        }
        if(this.username.length()==0) {
            isValid = false;
            message.add("Username is null.");
            return isValid;
        }
        return isValid;
    }

    private boolean validateBirthDate() {
        boolean isValid = true;
        if (dateOfBirth.isAfter(LocalDate.now().minusYears(18))) {
            isValid = false;
            message.add("Must be older than 18.");
        }
        return isValid;
    }

    private boolean validatePassword(){
        boolean isValid=true;
        if(!this.password.equals(this.confirmedPassword)) {
            isValid=false;
            message.add("Passwords do not match.");
            return isValid;
        }
        if(this.password.length()<8 || this.password.length()>100) {
            isValid = false;
            message.add("Password length invalid.");
            return isValid;
        }
        if(!this.password.matches(".*\\d+.*")){
            isValid = false;
            message.add("Password must contain a number.");
            return isValid;
        }
        if(!this.password.matches(".*[A-Z].*")){
            isValid = false;
            message.add("Password must contain at least an uppercase letter.");
            return isValid;
        }
        return isValid;
    }

    private boolean validateEmail(){
        if(!this.email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
            message.add("Email invalid.");
            return false;
        }
        return true;
    }

    public boolean validateUser(){
        boolean isValid=true;
        if(!this.validateUsername()) isValid=false;
        if(!this.validateBirthDate()) isValid=false;
        if(!this.validatePassword()) isValid=false;
        if(!this.validateEmail()) isValid=false;
        return isValid;
    }

}
