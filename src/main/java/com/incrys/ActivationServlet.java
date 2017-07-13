package com.incrys;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by User on 7/12/2017.
 */
@WebServlet(name = "ActivationServlet")
public class ActivationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        Connection connection = JDBConnectionFactory.getConnection();
        Statement statement= null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("update t1database.users set verified = 1 where id =" + id + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PrintWriter printWriter = response.getWriter();
        printWriter.append("Bravo bosulica, ti-ai confirmat e-mail-ul.");
    }
}
