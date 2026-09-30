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

public class ForgotPasswordService
{
    private static final SecureRandom RANDOM = new SecureRandom();
//set characters available for random generated password
    private static final String PASSWORD_CHARACTERS =
            
            "123456789";

    private static final int PASSWORD_LENGTH = 8;
    
    public static boolean resetPasswordAndEmail(
            String submittedEmail,
            String submittedSecurityAnswer)
            throws SQLException, MessagingException {

        String findUserSql = """
                SELECT id
                FROM users
                WHERE userEmail = ?
                  AND LOWER(userSecurityAnswer) = LOWER(?)
                LIMIT 1
                """;

        String updatePasswordSql = """
                UPDATE users
                SET userPassword = ?
                WHERE id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                int userId;

                try (PreparedStatement findStatement =
                         connection.prepareStatement(findUserSql)) {

                    findStatement.setString(
                            1,
                            submittedEmail.trim()
                    );

                    findStatement.setString(
                            2,
                            submittedSecurityAnswer.trim()
                    );

                    try (ResultSet results =
                             findStatement.executeQuery()) {

                        if (!results.next()) {
                            connection.rollback();
                            return false;
                        }

                        userId = results.getInt("id");
                    }
                }

                String temporaryPassword =
                        generateTemporaryPassword();

                try (PreparedStatement updateStatement =
                         connection.prepareStatement(
                                 updatePasswordSql
                         )) {

                    updateStatement.setString(
                            1,
                            temporaryPassword
                    );

                    updateStatement.setInt(
                            2,
                            userId
                    );

                    int updatedRows =
                            updateStatement.executeUpdate();

                    if (updatedRows != 1) {
                        throw new SQLException(
                                "The password could not be updated."
                        );
                    }
                }

                sendRecoveryEmail(
                        submittedEmail,
                        temporaryPassword
                );

                connection.commit();

                return true;

            } catch (
                    SQLException
                    | MessagingException
                    | RuntimeException exception) {

                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    exception.addSuppressed(
                            rollbackException
                    );
                }

                throw exception;
            }
        }
            
        }
        
        private static String generateTemporaryPassword() {

            StringBuilder password =
                    new StringBuilder(PASSWORD_LENGTH);

            for (int index = 0;
                 index < PASSWORD_LENGTH;
                 index++) {

                int characterIndex =
                        RANDOM.nextInt(
                                PASSWORD_CHARACTERS.length()
                        );

                password.append(
                        PASSWORD_CHARACTERS.charAt(
                                characterIndex
                        )
                );
            }

            return password.toString();
        }

        private static void sendRecoveryEmail(
                String recipientEmail,
                String temporaryPassword)
                throws MessagingException {

            String smtpUser =
                    EnvironmentConfig.getSmtpUser();

            String smtpPassword =
                    EnvironmentConfig.getSmtpPassword();

            Properties properties = new Properties();

            properties.put(
                    "mail.smtp.auth",
                    "true"
            );

            properties.put(
                    "mail.smtp.host",
                    "smtp.gmail.com"
            );

            properties.put(
                    "mail.smtp.port",
                    "587"
            );

            properties.put(
                    "mail.smtp.starttls.enable",
                    "true"
            );

            properties.put(
                    "mail.smtp.starttls.required",
                    "true"
            );

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
                    "TFPD Manager DB Temporary Password"
            );

            message.setText(
                    """
                    A password recovery request was submitted for your account.

                    Your temporary password is:

                    %s

                    Please sign in and change this password as soon as possible.

                    If you did not request this password reset, contact your administrator.
                    """.formatted(temporaryPassword)
            );

            Transport.send(message);
    }
}
