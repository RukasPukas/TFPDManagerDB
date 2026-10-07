package bulletin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import application.DatabaseConnection;
import javafx.scene.control.Alert;

public class PostToBulletinService
{
	
	public static void updateBulletinDB(String enteredUsername, String newBulletinText, String dateEntered) throws SQLException
	{
        String updateBulletinLog =	                
        		"""
        		INSERT INTO bulletinposts(userName,dateEntered,postText)
                    VALUES (?, ?, ?)
                    """;
        
        try (Connection connection = DatabaseConnection.getConnection(); 
        		PreparedStatement postStatement = connection.prepareStatement(updateBulletinLog)) 
        {
        	postStatement.setString(1, enteredUsername);
        	postStatement.setString(2, dateEntered);
        	postStatement.setString(3, newBulletinText);
            int rowsInserted = postStatement.executeUpdate();
           
            if (rowsInserted == 1) 
            {
            	System.out.println("Bulletin post recorded to bulletinposts DB.");
            }
            
            else 
            {
				System.out.println("Datbase could not be updated");
				Alert emailGenerationError = new Alert(Alert.AlertType.ERROR);
				emailGenerationError.setTitle("There was an error updating the bulletin board database with the new message.. ");
				emailGenerationError.setHeaderText("Please contact IT assistance at LukiSoft.");
				emailGenerationError.showAndWait();
                throw new SQLException("New bulletin post could not be created.");
            }
        }
	}

}
