package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserLogoutTimeStampService 
{
	private UserLogoutTimeStampService() {}
	
	public static void recordSuccessfulLogout (String enteredUsername)
			throws SQLException
	{
        String sql =  """
                UPDATE userLoginHistory
                SET logoutTimeStamp = CURRENT_TIMESTAMP
                WHERE userId = (
                    SELECT id
                    FROM users
                    WHERE userName = ?
                    LIMIT 1
                )
                AND logoutTimeStamp IS NULL
                ORDER BY loginTimeStamp DESC
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
            System.out.println("User logout time/date recorded.");

            if (rowsInserted != 1) {
                throw new SQLException(
                        "The successful logout could not be recorded."
                );
            }
        }
		
	}

}
