package application;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class database{

    static DataSource createDataSource(){
        final String url = "jdbc:postgresql://localhost:5432/crypticvault?user=postgres";
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        return dataSource;
    }

    static void insertUserData(Connection conn, String website, String username, String password) throws SQLException{
        PreparedStatement insertStmt =
                conn.prepareStatement("INSERT INTO userInfo(website,username,password) VALUES(?,?,?)");

        insertStmt.setString(1, website);
        insertStmt.setString(2, username);
        insertStmt.setString(3, password);
        int insertRows = insertStmt.executeUpdate();

        System.out.printf("inserted rows %d %n", insertRows);

    }

    static String[] query(Connection conn, String website) throws SQLException {
        String[] userData = new String[2];
        PreparedStatement queryStmt =
                conn.prepareStatement("SELECT username, password FROM userInfo WHERE (website) = (?) ");

        queryStmt.setString(1, website);

        ResultSet resultSet = queryStmt.executeQuery();

        if (resultSet.next()){
            String retrievedUsername = resultSet.getString("username");
            String retrievedPassword = resultSet.getString("password");

            userData[0] = retrievedUsername;
            userData[1] = retrievedPassword;
        }
        else {
            System.out.println("No matching row found.");
        }

        return userData;

    }


}
