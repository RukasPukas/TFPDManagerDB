package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReturnAccountInformationSerivce {
	
	//RETURN USER SECURITY LEVEL METHOD
	 static int ReturnSecurityLevel(String enteredUsername)
		        throws SQLException {

		    String sql = """
		            SELECT userSecurityLevel
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

		        try (ResultSet securityLevelResult =
		                     statement.executeQuery()) {

		            if (securityLevelResult.next()) {
		                return securityLevelResult.getInt(
		                        "userSecurityLevel"
		                );
		            }

		            throw new SQLException(
		                    "Authenticated user could not be found."
		            );
		        }
		    }
		}
	//Return First Name Method	 
	 static String ReturnFirstName(String enteredUsername)
			        throws SQLException {

			    String sql = """
			            SELECT firstName
			            FROM users
			            WHERE userName = ?
			            LIMIT 1
			            """;

			    try (Connection connection =DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql))
			     {
			        statement.setString(1, enteredUsername);

			        try (ResultSet firstNameResult = statement.executeQuery()) 
			        {

			            if (firstNameResult.next()) 
			            {
			                return firstNameResult.getString("firstName");
			            }

			            throw new SQLException("Issue locating first name assoicatied with the acocunt.");
			        }
			    }
			}
	 
		//Return First Name Method	 
 static String ReturnLastName(String enteredUsername)
			        throws SQLException {

			    String sql = """
			            SELECT lastName
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

			        try (ResultSet lastNameResult =
			                     statement.executeQuery()) {

			            if (lastNameResult.next()) {
			                return lastNameResult.getString("lastName");
			            }

			            throw new SQLException(
			                    "Issue locating last name assoicatied with the acocunt."
			            );
			        }
			    }
			}

}
