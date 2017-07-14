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
import java.util.Date;


/**
 * Created by User on 7/14/2017.
 */
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuffer stringBuilder = new StringBuffer();
        String line = null;
        BufferedReader bufferedReader = req.getReader();
        while ((line=bufferedReader.readLine())!=null) {
            stringBuilder.append(line);
        }

        PrintWriter printWriter = resp.getWriter();
        String email=null;
        String password=null;

        try {
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            email=jsonObject.getString("email");
            password = jsonObject.getString("password");
        } catch (JSONException e) {
            e.printStackTrace();
            printWriter.append(e.getMessage());
        }

        HttpSession httpSession = req.getSession();
        if(httpSession.getAttribute("loginAttempts")==null) {
            httpSession.setAttribute("loginAttempts", 0);
        }


        if (httpSession.getAttribute("email")==null) {
            if ((Integer)httpSession.getAttribute("loginAttempts") <= 5){
                if(User.authenticate(email, password)) {
                    printWriter.append("LOGIN SUCCESSFUL");
                    httpSession.setAttribute("email",email);
                    httpSession.setMaxInactiveInterval(900);
                } else {
                        int attempts = (Integer) httpSession.getAttribute("loginAttempts");
                        httpSession.setAttribute("loginAttempts",attempts+1);

                        printWriter.append("Invalid credentials.");
                        printWriter.append("\nLogin attempts: ");
                        printWriter.append(httpSession.getAttribute("loginAttempts").toString());
                }
            } else {
                printWriter.append("Too many attempts. Locked out.");
//                Date current = new Date();
//                Date timeout = new Date(current.getTime()+1*60000);
//                httpSession.setAttribute("loginTimeout",timeout);
//                Date diff =
//                printWriter.append("Try again in "+ diff);
            }
        } else {
            printWriter.append("Already logged in as " + httpSession.getAttribute("email").toString());
        }

    }
}
