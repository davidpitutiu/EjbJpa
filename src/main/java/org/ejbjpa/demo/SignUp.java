package org.ejbjpa.demo;
import jakarta.annotation.Resource;
import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.RequestScoped;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@ManagedBean
@RequestScoped
public class SignUp {
    private String email;
    private String password;

    @Resource(lookup = "java:/jboss/datasources/MySQLDS")
    private DataSource dataSource;

    // ... Getteri și Setteri pentru email și password ...

    public String register() {
        try (Connection connection = dataSource.getConnection()) {
            // Definiți instrucțiunea SQL pentru inserarea datelor în baza de date
            String sql = "INSERT INTO users (email, password) VALUES (?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);

                // Executați instrucțiunea SQL pentru inserare
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // Înregistrarea a fost efectuată cu succes
                    return "login?faces-redirect=true";
                } else {
                    // Înregistrarea a eșuat
                    return "signup?faces-redirect=true";
                }
            }
        } catch (SQLException e) {
            // Gestionarea excepțiilor legate de bază de date
            e.printStackTrace();
            return "signup?faces-redirect=true";
        }
    }
}
