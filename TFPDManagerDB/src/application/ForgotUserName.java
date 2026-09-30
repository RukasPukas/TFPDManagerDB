package application;
import java.sql.SQLException;

import jakarta.mail.MessagingException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;



public final class ForgotUserName 
{


	    private ForgotUserName()  { }

	    
  //Establish Stage
	    public static void showWindow(Stage launchStage)
	    {
	    	Stage userNameRecoveryStage = new Stage();
	    	Group root = new Group();
	    	userNameRecoveryStage.setOnCloseRequest(event -> {
	    	    event.consume();
	    	});
	    	userNameRecoveryStage.setTitle("Forgot Username");
	    	userNameRecoveryStage.setWidth(400);
	    	userNameRecoveryStage.setHeight(250);
	    	userNameRecoveryStage.setResizable(false);
			LinearGradient pagreGradient = new LinearGradient(
			        0, 0,
			        0, 1,
			        true,
			        CycleMethod.NO_CYCLE,
			        new Stop(0, Color.web("#F7FAFC")),
			        new Stop(1, Color.web("#234C6B")));
	    	Scene scene = new Scene(root,pagreGradient);
			Image icon = new Image("TFPDLogoFull.jpg");
			userNameRecoveryStage.getIcons().add(icon);
			
			
			
//Enter Email Label Prompt
			Text emailPrompt = new Text();
			emailPrompt.setText("Please Enter your associated Email:");
			emailPrompt.setFont(Font.font("Menlo", FontWeight.BOLD, 18));
			emailPrompt.setFill(Color.web("#364359"));
			emailPrompt.setX((40));
			emailPrompt.setY(25);
			root.getChildren().add(emailPrompt);
			
			
			
//Email Text Field
			TextField emailTF = new TextField();
			emailTF.setLayoutX(40);
			emailTF.setLayoutY(75);
			emailTF.setPrefWidth(300);
			root.getChildren().add(emailTF);
	    	
	    
			
			
//Submit Button
			Button submitButton = new Button("Submit");
			submitButton.setLayoutX(75);
			submitButton.setFont(Font.font("Menlo", 18));
			submitButton.setLayoutY(125);
			submitButton.setPrefWidth(100);
			submitButton.setOnMouseEntered(e -> {
				submitButton.setStyle("-fx-background-color: white;");
			});
			submitButton.setOnMouseExited(e -> {
				submitButton.setStyle("-fx-text-fill: #364359;");
			});
			submitButton.setOnAction(event -> {
			    System.out.println("Forgot Username Submit Button Pressed..");
			    String submittedEmail = new String(emailTF.getText().toUpperCase().trim());
//generate an empty field alert for email
			    if (submittedEmail.isEmpty()) {
			    	Alert emailEmptyAlert = new Alert(AlertType.INFORMATION);
			    	emailEmptyAlert.setTitle("Empty Field Warning");
			    	emailEmptyAlert.setHeaderText("ATTENTION:");
			    	emailEmptyAlert.setContentText("This field cannot be blank.");
			    	emailEmptyAlert.showAndWait();
			    	return;
			    }
			    
			    try {
			        boolean accountFound =
			                ForgotUsernameService.sendUsernameToEmail(
			                        submittedEmail
			                );

			        if (accountFound) 
			        {
			        	
				    	Alert emailFoundAlert = new Alert(AlertType.INFORMATION);
				    	emailFoundAlert.setTitle("EMAIL found!");
				    	emailFoundAlert.setHeaderText("ATTENTION:");
				    	emailFoundAlert.setContentText("The EMAIL associated has been found.");
				    	emailFoundAlert.showAndWait();
					    launchStage.show();
					    userNameRecoveryStage.close();
			            
			        } 
			        else
			        {
				    	Alert emailNotFoundAlert = new Alert(AlertType.INFORMATION);
				    	emailNotFoundAlert.setTitle("EMAIL NOT FOUND");
				    	emailNotFoundAlert.setHeaderText("ATTENTION:");
				    	emailNotFoundAlert.setContentText("The EMAIL address entered is not contained in the database.");
				    	emailNotFoundAlert.showAndWait();
			            
			        }

			    } catch (SQLException exception) {
			    	Alert dataBaseAccessAlert = new Alert(AlertType.INFORMATION);
			    	dataBaseAccessAlert.setTitle("DATABASE UNREACHABLE");
			    	dataBaseAccessAlert.setHeaderText("ATTENTION:");
			    	dataBaseAccessAlert.setContentText("The database was unable to be reached. Please reach out to your IT Administrator.");
			    	dataBaseAccessAlert.showAndWait();
			        exception.printStackTrace();

			    } catch (MessagingException exception) {
			    	Alert emailConnectionAlert = new Alert(AlertType.INFORMATION);
			    	emailConnectionAlert.setTitle("MAILING SYSTEM UNREACHABLE");
			    	emailConnectionAlert.setHeaderText("ATTENTION:");
			    	emailConnectionAlert.setContentText("The EMAIL management system failed. Please contact LukiSoft for further assitance.");
			    	emailConnectionAlert.showAndWait();
			        exception.printStackTrace();


			    } catch (IllegalStateException exception) {
			    	Alert emailNotConfiguredAlert = new Alert(AlertType.INFORMATION);
			    	emailNotConfiguredAlert.setTitle("MAILING SYSTEM HAS NOT BEEN CONFIGURED");
			    	emailNotConfiguredAlert.setHeaderText("ATTENTION:");
			    	emailNotConfiguredAlert.setContentText("The EMAIL management system has not yet been configured. Please contact LukiSoft for further assitance.");
			    	emailNotConfiguredAlert.showAndWait();
			    	exception.printStackTrace();
			    }

			    
			});
			root.getChildren().add(submitButton);

			
			
			
//Cancel Button
			Button cancelButton = new Button("Cancel");
			cancelButton.setLayoutX(200);
			cancelButton.setFont(Font.font("Menlo", 18));
			cancelButton.setLayoutY(125);
			cancelButton.setPrefWidth(100);
			cancelButton.setOnMouseEntered(e -> {
				cancelButton.setStyle("-fx-background-color: white;");
			});
			cancelButton.setOnMouseExited(e -> {
				cancelButton.setStyle("-fx-text-fill: #364359;");
			});
			cancelButton.setOnAction(event -> {
			    System.out.println("Forgot Username Cancel Button Pressed..");
			    launchStage.show();
			    userNameRecoveryStage.close();
  
			});
			root.getChildren().add(cancelButton);
			launchStage.hide();
	        
	        

	       

	        userNameRecoveryStage.setScene(scene);
	        userNameRecoveryStage.show();

	    	
	    }
	    


}
	


