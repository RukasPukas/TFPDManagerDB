package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserNameAuthenticator {

    private UserNameAuthenticator() {
    }

    public static boolean ReturnUserName(String userName)
            throws SQLException {

        String sql = """
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
            statement.setString(1, userName);

            try (
                ResultSet results =
                        statement.executeQuery()
            ) {
                return results.next();
            }
        }
    }
}