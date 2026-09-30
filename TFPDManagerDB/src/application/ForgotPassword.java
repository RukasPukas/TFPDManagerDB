package application;

import java.sql.SQLException;

import jakarta.mail.MessagingException;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ForgotPassword
{
	private ForgotPassword(){}
	
	  //Establish Stage
    public static void showWindow(Stage launchStage)
    {
    	
//Establish stage
    	Stage userPWRecoveryStage = new Stage();
    	Group root = new Group();
    	userPWRecoveryStage.setOnCloseRequest(event -> {
        event.consume();
   	});
    	userPWRecoveryStage.setTitle("Forgot Password");
    	userPWRecoveryStage.setWidth(400);
    	userPWRecoveryStage.setHeight(300);
    	userPWRecoveryStage.setResizable(false);
		LinearGradient pagreGradient = new LinearGradient(
		        0, 0,
		        0, 1,
		        true,
		        CycleMethod.NO_CYCLE,
		        new Stop(0, Color.web("#F7FAFC")),
		        new Stop(1, Color.web("#234C6B")));
    	Scene scene = new Scene(root,pagreGradient);
		Image icon = new Image("TFPDLogoFull.jpg");
		userPWRecoveryStage.getIcons().add(icon);
		
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
		emailTF.setLayoutY(50);
		emailTF.setPrefWidth(300);
		root.getChildren().add(emailTF);
		
//Enter Security Answer Label Prompt
		Text securityQuestionPrompt = new Text();
		securityQuestionPrompt.setText("What is your mother's first name:");
		securityQuestionPrompt.setFont(Font.font("Menlo", FontWeight.BOLD, 18));
		securityQuestionPrompt.setFill(Color.web("#364359"));
		securityQuestionPrompt.setX((40));
		securityQuestionPrompt.setY(110);
		root.getChildren().add(securityQuestionPrompt);
		
//security answer Text Field
		TextField securityTF = new TextField();
		securityTF.setLayoutX(40);
		securityTF.setLayoutY(135);
		securityTF.setPrefWidth(300);
		root.getChildren().add(securityTF);
    	
//cancel button
		Button cancelButton = new Button("Cancel");
		cancelButton.setLayoutX(200);
		cancelButton.setFont(Font.font("Menlo", 18));
		cancelButton.setLayoutY(185);
		cancelButton.setPrefWidth(100);
		cancelButton.setOnMouseEntered(e -> {
			cancelButton.setStyle("-fx-background-color: white;");
		});
		cancelButton.setOnMouseExited(e -> {
			cancelButton.setStyle("-fx-text-fill: #364359;");
		});
		cancelButton.setOnAction(event -> {
		    System.out.println("Forgot User Password Cancel Button Pressed..");
		    launchStage.show();
		    userPWRecoveryStage.close();
		});
		root.getChildren().add(cancelButton);
		
//Submit button
		
		Button submitButton = new Button("Submit");
		submitButton.setLayoutX(75);
		submitButton.setFont(Font.font("Menlo", 18));
		submitButton.setLayoutY(185);
		submitButton.setPrefWidth(100);
		submitButton.setOnMouseEntered(e -> {
			submitButton.setStyle("-fx-background-color: white;");
		});
		submitButton.setOnMouseExited(e -> {
			submitButton.setStyle("-fx-text-fill: #364359;");
		});
		submitButton.setOnAction(event -> {
		    System.out.println("Forgot User Password Submit Button Pressed..");
		    String submittedEmail = new String(emailTF.getText().toUpperCase().trim());
		    String submittedSecurityQuestion = new String(securityTF.getText().toUpperCase().trim());
//generate an empty field alert for email
		    if (submittedEmail.isEmpty() || submittedSecurityQuestion.isEmpty()) 
		    {
		    	Alert emailEmptyAlert = new Alert(AlertType.INFORMATION);
		    	emailEmptyAlert.setTitle("Empty Field Warning");
		    	emailEmptyAlert.setHeaderText("ATTENTION:");
		    	emailEmptyAlert.setContentText("EMAIL and/or security answer cannot be blank.");
		    	emailEmptyAlert.showAndWait();
		    	return;
		    }
		    System.out.println(
		            "Forgot User Password Submit Button Pressed..."
		    );




		    try {
		        boolean accountVerified =
		                ForgotPasswordService.resetPasswordAndEmail(
		                        submittedEmail,
		                        submittedSecurityQuestion
		                );

		        if (accountVerified) {

		            Alert successAlert =
		                    new Alert(AlertType.INFORMATION);

		            successAlert.setTitle(
		                    "Password Reset Successful"
		            );

		            successAlert.setHeaderText(
		                    "Temporary Password Sent"
		            );

		            successAlert.setContentText(
		                    "A temporary password has been sent "
		                    + "to the email address associated "
		                    + "with your account."
		            );

		            successAlert.showAndWait();

		            // Return to the login window
		            launchStage.show();
		            userPWRecoveryStage.close();

		        } else {

		            Alert verificationAlert =
		                    new Alert(AlertType.INFORMATION);

		            verificationAlert.setTitle(
		                    "Account Verification Failed"
		            );

		            verificationAlert.setHeaderText(
		                    "Information Did Not Match"
		            );

		            verificationAlert.setContentText(
		                    "The email address or security answer "
		                    + "was incorrect."
		            );

		            verificationAlert.showAndWait();
		        }

		    } catch (SQLException exception) {

		        Alert databaseAlert =
		                new Alert(AlertType.ERROR);

		        databaseAlert.setTitle(
		                "Database Error"
		        );

		        databaseAlert.setHeaderText(
		                "DATABASE UNREACHABLE"
		        );

		        databaseAlert.setContentText(
		                "The database could not be accessed. "
		                + "Please contact your administrator."
		        );

		        databaseAlert.showAndWait();
		        exception.printStackTrace();

		    } catch (MessagingException exception) {

		        Alert emailAlert =
		                new Alert(AlertType.ERROR);

		        emailAlert.setTitle(
		                "Email Error"
		        );

		        emailAlert.setHeaderText(
		                "MAILING SYSTEM UNREACHABLE"
		        );

		        emailAlert.setContentText(
		                "The temporary password could not be emailed. "
		                + "Please contact your administrator."
		        );

		        emailAlert.showAndWait();
		        exception.printStackTrace();

		    } catch (IllegalStateException exception) {

		        Alert configurationAlert =
		                new Alert(AlertType.ERROR);

		        configurationAlert.setTitle(
		                "Email Not Configured"
		        );

		        configurationAlert.setHeaderText(
		                "MAILING SYSTEM NOT CONFIGURED"
		        );

		        configurationAlert.setContentText(
		                exception.getMessage()
		        );

		        configurationAlert.showAndWait();
		        exception.printStackTrace();
		    }
		});
		root.getChildren().add(submitButton);
		
		
		
//final stage call
		userPWRecoveryStage.setScene(scene);
        userPWRecoveryStage.show();
		
    }
	
}
