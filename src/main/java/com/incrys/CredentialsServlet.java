package com.incrys;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Created by User on 7/18/2017.
 */
public class CredentialsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter printWriter = resp.getWriter();
        HttpSession httpSession = req.getSession();

        if (httpSession.getAttribute("email")!=null) {
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;
            BufferedReader bufferedReader = req.getReader();
            while((line=bufferedReader.readLine())!=null) {
                stringBuffer.append(line);
            }

            JSONObject jsonObject = new JSONObject(stringBuffer.toString());

            String username = null;
            String password = null;
            LocalDate birthDate = null;
            try {
                username = (String) jsonObject.get("username");
            } catch (JSONException e) {
            }
            try {
                password = jsonObject.getString("password");
            } catch (JSONException e) {
            }
            try {
                String date = jsonObject.getString("birthDate");
                birthDate = LocalDate.parse(date);
            } catch (JSONException e) {
            } catch (DateTimeParseException e) {
                printWriter.append("Invalid date " + e.getParsedString() + ". Format is yyyy-mm-dd.\n");
            }

            User user = new User(username,birthDate,(String) httpSession.getAttribute("email"),password,password,null);

            try {
                user.updateCredentials();
                printWriter.append(user.message.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            printWriter.append("Not logged in.");
        }


    }
}
