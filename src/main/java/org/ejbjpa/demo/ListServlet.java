package org.ejbjpa.demo;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

@WebServlet("/list")
public class ListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("user_id");

        List<String> userItems = getUserItems(userId);

        // Debugging output
        System.out.println("User ID: " + userId);
        System.out.println("Number of items retrieved: " + userItems.size());

        request.setAttribute("userItems", userItems);

        request.getRequestDispatcher("/list.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("user_id");
        String deleteItemId = request.getParameter("deleteItemId");

        if (deleteItemId != null) {
            if (deleteUserItem(userId, deleteItemId)) {
                // Refresh the page to display the updated list after item deletion
                response.sendRedirect("list?user_id=" + userId);
            } else {
                // Handle the error, e.g., display an error message
                response.sendRedirect("list?user_id=" + userId + "&error=Item+deletion+failed");
                return;
            }
        }

        String newItem = request.getParameter("newItem");

        if (newItem != null && !newItem.isEmpty()) {
            if (insertUserItem(userId, newItem)) {
                // Refresh the page to display the updated list after item addition
                response.sendRedirect("list?user_id=" + userId);
            } else {
                // Handle the error, e.g., display an error message
                response.sendRedirect("list?user_id=" + userId + "&error=Item+addition+failed");
            }
        } else {
            // Handle the case where newItem is missing or empty
            response.sendRedirect("list?user_id=" + userId + "&error=Missing+or+empty+item");
        }
    }

    private List<String> getUserItems(String userId) {
        List<String> userItems = new ArrayList<>();

        try {
            Context ctx = new InitialContext();
            DataSource dataSource = (DataSource) ctx.lookup("java:/MySqlDS");

            try (Connection connection = dataSource.getConnection()) {
                String sql = "SELECT list_item FROM user_list WHERE user_id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, userId);

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        while (resultSet.next()) {
                            userItems.add(resultSet.getString("list_item"));
                        }
                    }
                }
            }
        } catch (SQLException | javax.naming.NamingException e) {
            e.printStackTrace();
        }

        return userItems;
    }

    private boolean deleteUserItem(String userId, String deleteItemId) {
        try {
            Context ctx = new InitialContext();
            DataSource dataSource = (DataSource) ctx.lookup("java:/MySqlDS");

            try (Connection connection = dataSource.getConnection()) {
                String sql = "DELETE FROM user_list WHERE user_id = ? AND list_item = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, userId);
                    preparedStatement.setString(2, deleteItemId);

                    int rowsAffected = preparedStatement.executeUpdate();

                    return rowsAffected > 0;
                }
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean insertUserItem(String userId, String newItem) {
        try {
            Context ctx = new InitialContext();
            DataSource dataSource = (DataSource) ctx.lookup("java:/MySqlDS");

            try (Connection connection = dataSource.getConnection()) {
                String sql = "INSERT INTO user_list (user_id, list_item) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, userId);
                    preparedStatement.setString(2, newItem);

                    int rowsAffected = preparedStatement.executeUpdate();

                    return rowsAffected > 0;
                }
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }

        return false;
    }
}



