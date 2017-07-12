package com.incrys;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.UUID;

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
            String confirmationLink = "http://localhost:8080/activation?id=" +
                        id;
            connection.close();

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("teamoneims1@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("paltineanu13@gmail.com"));
            message.setSubject("Testing Subject");
            message.setText("Hello," + this.username +
                    "\n\n Click the following link to confirm your e-mail address : " +
                    confirmationLink);

            Transport.send(message);

            System.out.println("Confirmation e-mail sent.");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String hashPassword(String pwd) {
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

    public void addToDatabase(){
        Connection connection = JDBConnectionFactory.getConnection();
        Statement statement= null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO t1database.users VALUES (0,'"+this.username +
                    "','"+java.sql.Date.valueOf(this.dateOfBirth)+"','"+this.email+"','"+this.hashPassword(this.password)+"',0)");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}