package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class UserLoginTimeStampService {

    private UserLoginTimeStampService() {
    }

    public static void recordSuccessfulLogin(String enteredUsername)
            throws SQLException {

        String sql = """
                INSERT INTO userLoginHistory (userId)
                SELECT id
                FROM users
                WHERE userName = ?
                LIMIT 1
                """;

        try (
            Connection connection =
                    DatabaseConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ) {
            statement.setString(1, enteredUsername);

            int rowsInserted = statement.executeUpdate();
            System.out.println("User login time/date recorded.");

            if (rowsInserted != 1) {
                throw new SQLException(
                        "The successful login could not be recorded."
                );
            }
        }
    }
}