package com.incrys;

import jdk.nashorn.internal.parser.JSONParser;
import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Created by User on 7/12/2017.
 */
public class JsonServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuffer stringBuilder = new StringBuffer();
        String line = null;
        BufferedReader bufferedReader = req.getReader();
        while ((line=bufferedReader.readLine())!=null) {
            stringBuilder.append(line);
        }

        PrintWriter printWriter = resp.getWriter();

        String username = null;
        String email = null;
        LocalDate birthDate= null;
        String password = null;
        String confirmedPassword = null;
        String secretCode = null;

        try{
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            username = jsonObject.getString("username");
            email = jsonObject.getString("email");
            birthDate = LocalDate.parse(jsonObject.getString("birthDate"));
            password = jsonObject.getString("password");
            confirmedPassword = password;
            secretCode = jsonObject.getString("secretCode");

            User user = new User(username,birthDate,email,password,confirmedPassword,secretCode);
            System.out.println(user.toString());

            if(user.validateUser()) {
                printWriter.append("SUCCESS");
                user.addToDatabase();
                user.sendEmail();
            }
            else{
                printWriter.append("\n\n"+user.message);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            printWriter.append(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }
}
