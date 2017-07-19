package com.incrys;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.plaf.nimbus.State;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoField;
import java.util.*;

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
        if(!this.username.matches("^[a-zA-Z0-9_-]+( [a-zA-Z0-9_-]+)*$")) {
            isValid = false;
            message.add("Username contains invalid characters");
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
        if (dateOfBirth.isBefore(LocalDate.of(1900, 01, 01))) {
            isValid = false;
            message.add("You're too old to have an account.");
        }
        return isValid;
    }

    private boolean validatePassword() {
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

    private boolean validateEmail() {
        if(!this.email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[" +
                "\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0" +
                "-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|" +
                "[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-" +
                "]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\" +
                "\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
            message.add("Email invalid.");
            return false;
        }

        Connection connection = JDBConnectionFactory.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * from t1database.users WHERE email= '" + this.email +"'");
            if(resultSet.next()) {
                message.add("Email already registered.");
                connection.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean validateUser() throws IOException {
        boolean isValid=true;
        if(!this.validateUsername()) isValid=false;
        if(!this.validateBirthDate()) isValid=false;
        if(!this.validatePassword()) isValid=false;
        if(!this.validateEmail()) isValid=false;
        if(isValid) {
            if(!this.validateSecretCode()) {
                isValid = false;
            }
        }
        return isValid;
    }

    public boolean validateSecretCode() throws IOException {
        URL url = new URL("https://validate.mybluemix.net/token");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

        httpCon.setUseCaches(false);
        httpCon.setDefaultUseCaches(false);

        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");

        httpCon.setRequestProperty("Content-Type", "application/json");
        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
        String format = String.format("{\"token\":\"%s\"}", this.secretCode);

        out.write(format);
        out.close();

        httpCon.connect();
        System.out.println(format);

        if (httpCon.getResponseCode()==200) {
            httpCon.disconnect();
            return true;
        } else {
            httpCon.disconnect();
            message.add("Wrong code.");
            return false;
        }
    }

    public void sendEmail() throws SQLException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication("teamoneims1@gmail.com","1234567890aA");
                    }
                });

        try {
            Connection connection = JDBConnectionFactory.getConnection();
            Statement statement= null;
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * from t1database.users where email = '" + this.email + "'");

            resultSet.next();
            String id = String.valueOf(resultSet.getInt(1));
            String confirmationLink = "http://192.168.1.60:8080/IMS3.0/activation?id=" + id;
            connection.close();

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("teamoneims1@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(this.email));
            message.setSubject("Confirmation e-mail");
            message.setText("Hello," + this.username +
                    "\n\n\n Click the following link to confirm your e-mail address : " +
                    confirmationLink);

            Transport.send(message);

            System.out.println("Confirmation e-mail sent.");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hashPassword(String pwd) {
        String generatedPassword = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(pwd.getBytes());
            byte[] bytes = messageDigest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i=0;i< bytes.length;i++) {
                stringBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public void addToDatabase() {
        Connection connection = JDBConnectionFactory.getConnection();
        PreparedStatement statement= null;
        try {
            statement = connection.prepareStatement("INSERT INTO t1database.users VALUES (0,'"+this.username +
                    "','"+java.sql.Date.valueOf(this.dateOfBirth)+"','"+this.email+"','"+this.hashPassword(this.password)+"','"+ this.secretCode+"',0)");
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean authenticate(String email,String password) {
        String hashedPassword = hashPassword(password);
        Connection connection = JDBConnectionFactory.getConnection();
        boolean isValid=false;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM t1database.users WHERE email='"+email+
                    "' AND password_hash='"+hashedPassword+"' AND verified=1");
            if(resultSet.next()) isValid = true;
            else isValid = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    public void updateCredentials() throws SQLException {
        Connection connection = JDBConnectionFactory.getConnection();
        if(this.username!=null) {
            if (this.validateUsername()) {
                Statement statement = connection.createStatement();
                statement.executeUpdate("UPDATE t1database.users " +
                        "SET display_name='" + this.username + "'" +
                        "WHERE email='" + this.email + "'");
                message.add("Updated username.");
            }
        }
        if(this.password!=null) {
            if(this.validatePassword()) {
                Statement statement = connection.createStatement();
                statement.executeUpdate("UPDATE t1database.users " +
                        "SET password_hash='" + this.hashPassword(this.password) + "' " +
                        "WHERE email='" + this.email + "'");
                message.add("Updated password.");
            }
        }

        if(this.dateOfBirth!=null) {
            if(this.validateBirthDate()) {
                Statement statement = connection.createStatement();
                statement.executeUpdate("UPDATE t1database.users " +
                        "SET date_of_birth='" + java.sql.Date.valueOf(this.dateOfBirth) + "' " +
                        "WHERE email='" + this.email + "'");
                message.add("Updated birthday.");
            }
        }
    }
}