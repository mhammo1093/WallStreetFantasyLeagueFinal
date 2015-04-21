package com.capstone.hammond.wallstreetfantasyleaguefinal;

/**
 * Created by Mike on 4/4/2015.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionManager {


    private static String url = "jdbc:oracle:thin:@141.216.24.31:1521:fsdb";
    private static String driverName = "oracle.jdbc.driver.OracleDriver";
    private static String username = "fsuser";
    private static String password = "fsuser";
    private static Connection con;


    public static Connection getConnection() {
        try {
            Class.forName(driverName);
            try {
                if(con !=null)
                    return con;
                else
                    return con = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                // log an exception. for example:
                System.out.println("Failed to create the database connection.");
            }
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found.");
        }
        return con;
    }
}


