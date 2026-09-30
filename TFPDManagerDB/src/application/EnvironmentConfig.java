package application;

public final class EnvironmentConfig {

    private static final String SMTP_USER_VARIABLE =
            "TFPD_SMTP_USER";

    private static final String SMTP_PASSWORD_VARIABLE =
            "TFPD_SMTP_PASSWORD";
    
    private static final String DATABASE_USER_VARIABLE =
    		"DATABASE_USER";
    private static final String DATABASE_USER_PASSWORD = 
    		"DATABASE_PW";

    private EnvironmentConfig() {
        // Prevent objects of this utility class from being created.
    }

    public static String getSmtpUser() {
        return getRequiredVariable(SMTP_USER_VARIABLE).trim();
    }

    public static String getSmtpPassword() {
        return getRequiredVariable(SMTP_PASSWORD_VARIABLE);
    }
    
    public static String getDatabaseUser() {
    	return getRequiredVariable(DATABASE_USER_VARIABLE);
    }
   
    public static String getDatabasePW() {
    	return getRequiredVariable(DATABASE_USER_PASSWORD );
    }

    private static String getRequiredVariable(String variableName) {

        String value = System.getenv(variableName);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing environment variable: " + variableName
            );
        }

        return value;
    }
}