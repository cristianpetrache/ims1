package com.incrys;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("Register.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date birthDate = null;
        try {
            birthDate = format.parse(req.getParameter("birthDate"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String password = req.getParameter("pwd");
        String confirmedPassword = req.getParameter("pwd2");
        String secretCode = req.getParameter("secretCode");

        User user = new User(username,birthDate,email,password,confirmedPassword,secretCode);

        PrintWriter printWriter = resp.getWriter();
        printWriter.append(user.toString());
    }
}
