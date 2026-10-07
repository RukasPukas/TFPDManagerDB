package bulletin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.DatabaseConnection;

public class ReturnBulletinInfoService
{
    public static String returnBulletinInfo() throws SQLException
    {
        StringBuilder bulletinInfo = new StringBuilder();

        String sql = """
                SELECT userName, dateEntered, postText
                FROM bulletinPosts
                ORDER BY postId DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery())
        {
            while (results.next())
            {
                String userName = results.getString("userName");
                String dateEntered = results.getString("dateEntered");
                String postText = results.getString("postText");

                bulletinInfo.append(userName)
                            .append(" - ")
                            .append(dateEntered)
                            .append("\n")
                            .append(postText)
                            .append("\n\n");
            }
        }

        return bulletinInfo.toString();
    }
}