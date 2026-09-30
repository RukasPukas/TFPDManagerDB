package application;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginAuthenticator {
	
	private LoginAuthenticator() {}
	
	public static boolean credentialsAreValid(
			String userName,
			String userPassword)
	
	throws SQLException {
		String sql = 
				"""
				SELECT id
				FROM users
				Where userName = ?
				AND userPassword = ?
				LIMIT 1
				""";
		
		try (
				Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)
			)
		{
			statement.setString(1, userName);
			statement.setString(2,userPassword);
		
		
		try(ResultSet results = statement.executeQuery()) {
			return results.next();
		}
		
	}
	
	

	}
}
