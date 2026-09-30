package application;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.scene.control.Alert;

public class CreateNewUserService {
	
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String PASSWORD_CHARACTERS = "123456789";
    private static final int PASSWORD_LENGTH = 8;

    public static void createNewUserAuthenticator(String newFirstName, String newLastName,String newUsername,String newEmail,String newMaiden,String newSecurityLevel, boolean sendEmail)
            throws SQLException
    {
        System.out.println("\nBegin CreateNewUserService Class");

        // Convert position into database security level
        int securityLevelConversion = 0;

        if (newSecurityLevel.equals("Part-Time EMS")) {securityLevelConversion = 1;}
        if (newSecurityLevel.equals("POC-Fire")) {securityLevelConversion = 1;}
        if (newSecurityLevel.equals("POC-Fire Lieutenant")) {securityLevelConversion = 3;}
        if (newSecurityLevel.equals("Full-Time")) {securityLevelConversion = 2;}
        if (newSecurityLevel.equals("Full-Time Lieutenant")) {securityLevelConversion = 3;}
        if (newSecurityLevel.equals("POC-Fire Captain")) {securityLevelConversion = 3;}
        if (newSecurityLevel.equals("Full-Time Captain")) {securityLevelConversion = 4;}
        if (newSecurityLevel.equals("Assistant Chief")) {securityLevelConversion = 5;}
        if (newSecurityLevel.equals("Deputy Chief")) {securityLevelConversion = 6;}
        if (newSecurityLevel.equals("Chief")) {securityLevelConversion = 7;}
       
        if (securityLevelConversion == 0)
        {
            throw new IllegalArgumentException("Invalid security level: " + newSecurityLevel +" was attempted to have been entered.");
        }


        // CHECK FOR EXISTING USERNAME OR EMAIL -----------------------------

        String checkSQL = """
                SELECT id
                FROM users
                WHERE userName = ?
                   OR userEmail = ?
                LIMIT 1
                """;


        // INSERT NEW USER --------------------------------------------------

        String insertSQL = """
                INSERT INTO users
                (
                    firstName,
                    lastName,
                    userName,
                    userEmail,
                    userPassword,
                    userSecurityAnswer,
                    userSecurityLevel
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;


        try (Connection connection = DatabaseConnection.getConnection(); 
        		PreparedStatement checkStatement = connection.prepareStatement(checkSQL)) 
        {

            checkStatement.setString(1, newUsername);
            checkStatement.setString(2, newEmail);


            // Check whether account already exists
            try (ResultSet results = checkStatement.executeQuery()) 
            {

                if (results.next()) {

                    System.out.println("Username or email already exists.");

                    Alert accountExists = new Alert(Alert.AlertType.ERROR);
                    accountExists.setTitle("CANNOT CREATE ACCOUNT");
                    accountExists.setHeaderText("Account with same credentials already exists.");
                    accountExists.showAndWait();
                    return;
                }
            }


            System.out.println("Username and email are available.");


            // Actually insert the account
            try (PreparedStatement insertStatement =connection.prepareStatement(insertSQL)) 
            {
            	
            	String newSetPassword = generateTemporaryPassword();

                insertStatement.setString(1,newFirstName);
                insertStatement.setString(2,newLastName);
                insertStatement.setString(3,newUsername);
                insertStatement.setString(4,newEmail);
                insertStatement.setString(5, newSetPassword);
                insertStatement.setString(6,newMaiden);
                insertStatement.setInt(7,securityLevelConversion);
                int rowsInserted = insertStatement.executeUpdate();


                if (rowsInserted == 1) 
                {

                    System.out.println("New user successfully created.");
                    try 
                    {
						sendWelcomeEmail(newFirstName, newLastName, newUsername, newEmail, newSetPassword, sendEmail);
						Alert accountCreationSuccessfulMessage = new Alert(Alert.AlertType.ERROR);
						accountCreationSuccessfulMessage.setTitle("New Account Created. ");
						accountCreationSuccessfulMessage.setHeaderText("The specified account has been created.\n Please have the new user check their email.");
						accountCreationSuccessfulMessage.showAndWait();
						
					} 
                    catch (MessagingException e)
                	{
						
						e.printStackTrace();
						System.out.println("Welcome Email was not sent");
						Alert emailGenerationError = new Alert(Alert.AlertType.ERROR);
						emailGenerationError.setTitle("Error sending welcome message. ");
						emailGenerationError.setHeaderText("Please contact IT assistasnce at LukiSoft.");
						emailGenerationError.showAndWait();
					}

                } 
                else 
                {
					System.out.println("Datbase could not be updated");
					Alert emailGenerationError = new Alert(Alert.AlertType.ERROR);
					emailGenerationError.setTitle("There was an error updating the database with the new user credentials.. ");
					emailGenerationError.setHeaderText("Please contact IT assistasnce at LukiSoft.");
					emailGenerationError.showAndWait();
                    throw new SQLException("New user could not be created.");
                }
            }
        }
    }
    
    
    private static String generateTemporaryPassword() 
    {

        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int index = 0; index < PASSWORD_LENGTH; index++) 
        {

            int characterIndex = RANDOM.nextInt(PASSWORD_CHARACTERS.length());
            password.append(PASSWORD_CHARACTERS.charAt(characterIndex));}
        System.out.println("Temporary password set to: " + password);

        return password.toString();
    }
    
    private static void sendWelcomeEmail(String newFirstName, String newLastName, String newUsername, String newEmail, String newSetPassword, boolean sendEmail) 
    		throws MessagingException 
    {
    	if(sendEmail == true)
    	{
    	      String smtpUser = EnvironmentConfig.getSmtpUser();
    	        String smtpPassword = EnvironmentConfig.getSmtpPassword();
    	        
    	        Properties properties = new Properties();
    	        properties.put("mail.smtp.auth", "true");
    	        properties.put("mail.smtp.host","smtp.gmail.com");
    	        properties.put("mail.smtp.port","587");
    	        properties.put("mail.smtp.starttls.enable", "true");
    	        properties.put("mail.smtp.starttls.required","true");
    	    	properties.put("mail.smtp.connectiontimeout","10000");
    	    	properties.put("mail.smtp.timeout","10000");
    	    	properties.put("mail.smtp.writetimeout","10000");

    	        Session session = Session.getInstance(properties, new Authenticator() 
    	        {
    	            protected PasswordAuthentication getPasswordAuthentication() 
    	            {
    	                return new PasswordAuthentication(smtpUser, smtpPassword);
    	            }
    	        });

    	        MimeMessage message = new MimeMessage(session);
    	        message.setFrom(new InternetAddress(smtpUser));
    	        message.setRecipient(Message.RecipientType.TO, new InternetAddress(newEmail));
    	        message.setSubject("Welcome to TFPD Manager DB " +  newFirstName + " " + newLastName);
    	        message.setContent(
    	                """
    	                <html>
    	                <body style="margin: 0; padding: 30px; background-color: #ebeced; font-family: Arial, sans-serif;">

    	                    <div style="max-width: 600px; margin: auto; background-color: white; border-radius: 12px; overflow: hidden; box-shadow: 0px 2px 8px rgba(0,0,0,0.20);">

    	                        <div style="background-color: #234C6B;padding: 20px; text-align: center;">
    	                           
    	                             <h1 style="color: white; margin: 0; font-size: 24px;">
    	                                TFPD Manager DB
    	                            </h1>
    	                        </div>

    	                        <div style="padding: 30px; color: #333333;">

    	                            <h2 style="color: #234C6B;">Your Account Has Been Created</h2>

    	                            <p>A new TFPD Manager DB account has been created for you.</p>

    	                            <p>Your username is:</p>
    	                            <div style="background-color: #ebeced; border-radius: 8px; padding: 15px; text-align: center; font-size: 20px; font-weight: bold; color: #234C6B;">
    	                            %s
    	                            </div>
    	                            
    	                            <p> Your password has been set to:</p>
    	                        	<div style="background-color: #ebeced; border-radius: 8px; padding: 15px; text-align: center; font-size: 20px; font-weight: bold; color: #234C6B;">
    	                            %s
    	                            </div>

    	                            <p style="
    	                                margin-top: 25px;
    	                                font-size: 13px;
    	                                color: #666666;
    	                            ">
    	                                This is an automated message from the
    	                                TFPD Manager DB.
    	                            </p>

    	                        </div>

    	                    </div>

    	                </body>
    	                </html>
    	                """.formatted(newUsername, newSetPassword),

    	                "text/html; charset=UTF-8"
    	        );

    	        Transport.send(message);
    	        System.out.println("Welcome Email was sent");
    	}
    	else 
    	{
    		System.out.println("\nWelcome message declined.\n");
    	}

  
}
}