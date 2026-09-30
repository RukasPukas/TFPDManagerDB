module TFPDManagerDB {

    requires javafx.controls;
    requires javafx.graphics;
    requires java.sql;
    requires mysql.connector.j;
    requires jakarta.mail;
    requires jakarta.activation;
	requires javafx.base;
	requires java.desktop;
    opens application to javafx.graphics, javafx.fxml;
    exports application;
}