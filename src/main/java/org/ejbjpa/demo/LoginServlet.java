package org.ejbjpa.demo;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.util.List;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String encryptedPassword = encryptPassword(password);

        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory("MySqlDS");
            em = emf.createEntityManager();

            String jpql = "SELECT u FROM User u WHERE u.email = :email AND u.password = :password";
            TypedQuery<User> query = em.createQuery(jpql, User.class);
            query.setParameter("email", email);
            query.setParameter("password", encryptedPassword);

            List<User> users = query.getResultList();
            if (!users.isEmpty()) {
                int userId = users.get(0).getUserId();
                response.sendRedirect("list.jsp?user_id=" + userId);
            } else {
                response.sendRedirect("error.jsp?error=Login+failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?error=Database+Error");
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
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
