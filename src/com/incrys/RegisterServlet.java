package com.incrys;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("Register.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        LocalDate birthDate;
        try{
            birthDate = LocalDate.parse(req.getParameter("birthDate"));

        }catch (DateTimeParseException e) {
            birthDate=LocalDate.now();
        }
        String password = req.getParameter("pwd");
        String confirmedPassword = req.getParameter("pwd2");
        String secretCode = req.getParameter("secretCode");

        User user = new User(username,birthDate,email,password,confirmedPassword,secretCode);

        PrintWriter printWriter = resp.getWriter();
        printWriter.append(user.toString());
        if(user.validateUser()) {
            printWriter.append("SUCCESS");
        }
        else{
            printWriter.append("\n\n"+user.message);
        }
    }
}
