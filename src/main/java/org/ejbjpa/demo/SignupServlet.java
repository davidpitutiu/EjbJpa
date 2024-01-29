package org.ejbjpa.demo;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("cpassword");

        if (!password.equals(confirmPassword)) {
            response.sendRedirect("signup.jsp?error=Passwords+do+not+match");
            return;
        }

        String encryptedPassword = encryptPassword(password);

        try {
            Context ctx = new InitialContext();
            DataSource dataSource = (DataSource) ctx.lookup("java:/MySqlDS");

            try (Connection connection = dataSource.getConnection()) {

                String checkEmailQuery = "SELECT email FROM user WHERE email = ?";
                try (PreparedStatement checkEmailStatement = connection.prepareStatement(checkEmailQuery)) {
                    checkEmailStatement.setString(1, email);
                    ResultSet resultSet = checkEmailStatement.executeQuery();
                    if (resultSet.next()) {

                        response.sendRedirect("error.jsp?error=Email+already+exists");
                        return;
                    }
                }


                String insertUserQuery = "INSERT INTO user (email, password) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery)) {
                    preparedStatement.setString(1, email);
                    preparedStatement.setString(2, encryptedPassword);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        response.sendRedirect("index.jsp");
                    } else {
                        response.sendRedirect("error.jsp?error=Registration+failed");
                    }
                }
            }
        } catch (SQLException | javax.naming.NamingException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?error=Database+Error");
        }
    }

    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String md5Hash = bigInt.toString(16);
            while (md5Hash.length() < 32) {
                md5Hash = "0" + md5Hash;
            }
            return md5Hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password;
        }
    }
}


