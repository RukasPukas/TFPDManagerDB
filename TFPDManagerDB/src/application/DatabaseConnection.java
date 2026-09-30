package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/tfpd_manager_db";

    
    private static final String USER = EnvironmentConfig.getDatabaseUser();
    private static final String PASSWORD = EnvironmentConfig.getDatabasePW();

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}