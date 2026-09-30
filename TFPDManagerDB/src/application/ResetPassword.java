package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ResetPassword 
{
	public static void resetPasswordForm(String enteredUsername, int userSecurityLevel, VBox mainArea, VBox navBar, Group root, Scene scene)
	{
		mainArea.getChildren().clear();
		UserOptionsHub.setUpUserOptionVBox(mainArea, navBar, root, scene);
		
		Text resetPasswordHeader = new Text("Reset Password");
		resetPasswordHeader.setFont(Font.font("Menlo",FontWeight.BOLD,24));
		LinearGradient logoTextGradient = new LinearGradient(0, 0,0, 1,true,CycleMethod.NO_CYCLE,new Stop(0, Color.web("#39414d")),new Stop(1, Color.web("#4872b5")));
		resetPasswordHeader.setFill(logoTextGradient);
		resetPasswordHeader.setTextOrigin(VPos.TOP);
		mainArea.getChildren().add(resetPasswordHeader);	
		
		Text currentPasswordPrompt = new Text ("Current Password:");
		currentPasswordPrompt.setFont(Font.font("Menlo",FontWeight.BOLD,18));
		currentPasswordPrompt.setFill(logoTextGradient);
		currentPasswordPrompt.setTextOrigin(VPos.TOP);
		mainArea.getChildren().add(currentPasswordPrompt);
		
		PasswordField oldPasswordField = new PasswordField();
		oldPasswordField.setMaxWidth(250);
		mainArea.getChildren().add(oldPasswordField);
		
		Text newPasswordPorompt = new Text ("New Password:");
		newPasswordPorompt.setFont(Font.font("Menlo",FontWeight.BOLD,18));
		newPasswordPorompt.setFill(logoTextGradient);
		newPasswordPorompt.setTextOrigin(VPos.TOP);
		mainArea.getChildren().add(newPasswordPorompt);
		
		TextField newPasswordTF = new TextField();
		newPasswordTF.setMaxWidth(250);
		mainArea.getChildren().add(newPasswordTF);
				
		Button submitButton = new Button("Submit Change");
		Button goBackButton = new Button ("Go Back");
		
		mainArea.getChildren().addAll(submitButton,goBackButton);
		
		VBox.setMargin(submitButton, new Insets(25,0,25,0));
		for (javafx.scene.Node node : mainArea.getChildren()) 
		{
			
		    if (node instanceof Button button) 
		    {
		        button.setPrefHeight(50);
		        button.setPrefWidth(300);
		        button.setFont(Font.font("Menlo", FontWeight.BOLD, 16));
		        button.setStyle("-fx-background-color: #ebeced;");
		        button.setMaxWidth(300);
		        button.setMaxHeight(50);
		        button.setOnMouseEntered(event -> {button.setStyle("-fx-background-color: #fafdff");});
		        button.setOnMouseExited(event -> {button.setStyle("-fx-background-color: #ebeced;");});
		        button.setOnMousePressed(event -> {button.setStyle("-fx-background-color: #4f7e9e;");});
		        button.setOnMouseReleased(event ->{button.setStyle("-fx-background-color:  #fafdff;");});
		        button.setCursor(Cursor.HAND);
	    	}
		    
		}
		
		goBackButton.setOnAction(event ->
		{
			UserOptionsHub.showUserOptionsWindow(enteredUsername, userSecurityLevel, mainArea, navBar, root, scene);
			System.out.println(enteredUsername + " backed out of change password menu.");
		});
		
		submitButton.setOnAction(event ->
		{
			System.out.println("\n" +enteredUsername + " initiated changing password...\n");

			Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
			confirmationAlert.setTitle("Confirmation");
			confirmationAlert.setHeaderText("Do you wish to continue?");
			confirmationAlert.setContentText("Please ensure all information is accurate before proceeding.");
			Optional<ButtonType> result =
			        confirmationAlert.showAndWait();

			if (result.isPresent()&& result.get() == ButtonType.OK) 
			{
				String oldPassword = oldPasswordField.getText().trim();
				String newPassword = newPasswordTF.getText().trim();
				
			    try 
			    {
					resetPasswordService(oldPassword, newPassword, enteredUsername,  userSecurityLevel,  mainArea,  navBar,  root,  scene);
					
					
				}
			    catch (SQLException e) {
					System.out.println("Error calling resetPasswordService");
					Alert classCallError = new Alert(Alert.AlertType.ERROR);
					classCallError.setTitle("Error calling method to reset password. ");
					classCallError.setHeaderText("Please contact IT assistasnce at LukiSoft.");
					classCallError.showAndWait();
					e.printStackTrace();
					return;
				}
			}
			
		});
	}
	
	public static void resetPasswordService(String oldPassword, String newPassword, String enteredUsername, int userSecurityLevel, VBox mainArea, VBox navBar, Group root, Scene scene)
			throws SQLException
	{
		 System.out.println("\nBegin reset password service method...");
		 
		 

	        String sqlPasswordMatch = """
	        							SELECT id 
	        							FROM users 
	        							WHERE userName = ? 
										AND userPassword = ? 
										LIMIT 1
										""";
	        
	        String updateSQLuserPassword =	"UPDATE users SET userPassword = ? WHERE username = ?;";
	     
		 
	        try (Connection connection = DatabaseConnection.getConnection(); 
	        		PreparedStatement checkStatement = connection.prepareStatement(sqlPasswordMatch)) 
	        {
	        	System.out.println("Attempting to check old password for account -> " +enteredUsername + ", using the password -> " + oldPassword);
	            checkStatement.setString(1, enteredUsername);
	            checkStatement.setString(2, oldPassword);
	            
	            try (ResultSet results = checkStatement.executeQuery()) 
	            {

	                if (results.next()) 
	                {
	                	System.out.println("\nOld password correctly entered..\n");
	                    try (PreparedStatement updateStatement =connection.prepareStatement(updateSQLuserPassword)) 
	                    {
	                    	updateStatement.setString(1, newPassword);
	                    	updateStatement.setString(2,enteredUsername);
	                    	
	                    	int rowsUpdated = updateStatement.executeUpdate();
	                    	if(rowsUpdated > 0)
	                    	{
	                    		System.out.println("User name ->" + enteredUsername + " has had their password updated to ->" + newPassword +" successfully.");
	    						
	    						Alert passwordNotUpdatedMessage = new Alert(Alert.AlertType.ERROR);
	    						passwordNotUpdatedMessage.setTitle("Password Updated. ");
	    						passwordNotUpdatedMessage.setHeaderText("Password for the account " + enteredUsername + " was updated.");
	    						passwordNotUpdatedMessage.showAndWait();
	    						UserOptionsHub.showUserOptionsWindow(enteredUsername, userSecurityLevel, mainArea, navBar, root, scene);
	    						
	                    		
	                    	}
	                    	else
	                    	{
	    						
	    						System.out.println("Password was not updated.");
	    						Alert passwordNotUpdatedMessage = new Alert(Alert.AlertType.ERROR);
	    						passwordNotUpdatedMessage.setTitle("Error updating user password. ");
	    						passwordNotUpdatedMessage.setHeaderText("Please contact IT assistasnce at LukiSoft.");
	    						passwordNotUpdatedMessage.showAndWait();
	    						return;
	                    	}
	                    }



	                }
	                else
	                {
	                    System.out.println("Old entered password was not correct.");

	                    Alert incorrectOriginalPassword = new Alert(Alert.AlertType.ERROR);
	                    incorrectOriginalPassword.setTitle("CANNOT UPDATE PASSWORD");
	                    incorrectOriginalPassword.setHeaderText("The old password submitted was incorrect.");
	                    incorrectOriginalPassword.showAndWait();
	                    return;
	                }
	            }
	        }
	        
	}
	
}

