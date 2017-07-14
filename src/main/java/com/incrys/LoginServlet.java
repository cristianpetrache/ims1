package com.incrys;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


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

        Properties properties = new Properties();

        InputStream inputStream = new FileInputStream("session.properties");
        properties.load(inputStream);
        int LOGIN_LOCK = Integer.parseInt(properties.getProperty("LOGIN_LOCK"));
        int SESSION_TIMEOUT = Integer.parseInt(properties.getProperty("SESSION_TIMEOUT"));

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
        if(httpSession.getAttribute("loginTimeout")==null) {
            httpSession.setAttribute("loginTimeout",new Date());
        }


        if (httpSession.getAttribute("email")==null) {
            Date timeoutCheck= (Date) httpSession.getAttribute("loginTimeout");
            if(timeoutCheck.before(new Date())) {
                    httpSession.setAttribute("loginAttempts",0);
            }
            if ((Integer)httpSession.getAttribute("loginAttempts") <= 4){
                if(User.authenticate(email, password)) {
                    printWriter.append("LOGIN SUCCESSFUL");
                    httpSession.setAttribute("email",email);
                    httpSession.setMaxInactiveInterval(SESSION_TIMEOUT);
                } else {
                        int attempts = (Integer) httpSession.getAttribute("loginAttempts");
                        httpSession.setAttribute("loginAttempts",attempts+1);

                        printWriter.append("Invalid credentials.");
                        printWriter.append("\nLogin attempts: ");
                        printWriter.append(httpSession.getAttribute("loginAttempts").toString());
                        Date current = new Date();
                        Date timeout = new Date(current.getTime()+LOGIN_LOCK*60000);
                        httpSession.setAttribute("loginTimeout",timeout);
                }
            } else {
                printWriter.append("Too many attempts. Locked out.");
                Date timeout = (Date) httpSession.getAttribute("loginTimeout");
                long diff = timeout.getTime() - (new Date().getTime());
                printWriter.append("\nTry again in " + TimeUnit.MILLISECONDS.toSeconds(diff) + " seconds.");
            }
        } else {
            printWriter.append("Already logged in as " + httpSession.getAttribute("email").toString());
        }

    }
}
