package application;

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

public final class ForgotUsernameService {

    private ForgotUsernameService() {
    }

    public static boolean sendUsernameToEmail(String submittedEmail)
            throws SQLException, MessagingException {

        String sql = """
                SELECT userName
                FROM users
                WHERE userEmail = ?
                LIMIT 1
                """;

        try (
            Connection connection =
                    DatabaseConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql)
        ) {
            statement.setString(1, submittedEmail);

            try (ResultSet results = statement.executeQuery()) {

                if (!results.next()) {
                    return false;
                }

                String recoveredUsername =
                        results.getString("userName");

                sendRecoveryEmail(
                        submittedEmail,
                        recoveredUsername
                );

                return true;
            }
        }
    }

    private static void sendRecoveryEmail(
            String recipientEmail,
            String recoveredUsername)
            throws MessagingException {

        String smtpUser =
                EnvironmentConfig.getSmtpUser();

        String smtpPassword =
                EnvironmentConfig.getSmtpPassword();

        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");

        properties.put(
                "mail.smtp.connectiontimeout",
                "10000"
        );

        properties.put(
                "mail.smtp.timeout",
                "10000"
        );

        properties.put(
                "mail.smtp.writetimeout",
                "10000"
        );

        Session session = Session.getInstance(
                properties,
                new Authenticator() {

                    @Override
                    protected PasswordAuthentication
                            getPasswordAuthentication() {

                        return new PasswordAuthentication(
                                smtpUser,
                                smtpPassword
                        );
                    }
                }
        );

        MimeMessage message =
                new MimeMessage(session);

        message.setFrom(
                new InternetAddress(smtpUser)
        );

        message.setRecipient(
                Message.RecipientType.TO,
                new InternetAddress(recipientEmail)
        );

        message.setSubject(
                "TFPD Manager DB Username Recovery"
        );

        message.setText(
                """
                A username recovery request was submitted for your account.

                Your username is: %s

                If you did not request this information, you may disregard this email.
                """.formatted(recoveredUsername)
        );
        System.out.println("Attempting to send recovery email...");
        Transport.send(message);
        System.out.println("Recovery email sent successfully.");
    }
}