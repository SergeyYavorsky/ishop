package mypkg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Just-a-man on 11/17/2015.
 */
public class DataManager {

    private String dbURL = "";
    private String dbUserName = "";
    private String dbPassword = "";

    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }
    public String getDbURL() {
        return dbURL;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }
    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
    public String getDbPassword() {
        return dbPassword;
    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(getDbURL(), getDbUserName(), getDbPassword());
         }
        catch (Exception e) {
            System.out.println("Could not connect to DB: " + e.getMessage());
        }
        return conn;
    }
}
