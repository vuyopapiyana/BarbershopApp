
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author vuyopapiyana
 */
public class DBConnect {

    private Connection conn = null;
    private int connCount = 0;
    public final String dbName = "BarberPAT1.db";

    //Constructor which gets the database connection
    public DBConnect() {
        connCount++;
        try {
            String driver = "org.sqlite.JDBC";
            Class.forName(driver);
            try {
                conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
                System.out.println("Connection successful: " + connCount);

            } catch (SQLException ex) {
                Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
                System.out.print(ex);
                System.out.println("Datbase not found not found exception");
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Class not found exception");
        }

    }

    //Method is used to run queries
    //Methhod receives a String for the SQL query which will be performed
    //Method will return a ResultSet of that SQL query
    public ResultSet query(String SQL) throws SQLException {

        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        rs = stmt.executeQuery(SQL);
        return rs;

    }

    //Method is used to Update the table
    // uses connection statement to execute an update query
    // The method receives a String represesnting the SQL Statement
    public void update(String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        System.out.println("Before Update Stmt");
        stmt.executeUpdate(sql);
        System.out.println("After Update Stmt");

        stmt.close();

    }

    // Method returns an int showing the amount of haircuts a barber can cut.
    //Method receives an int representing the barberID starting from 1
    public int getAmountOfBarbersHaircuts(int barberIndex) {
        int amount = 0;

        try {
            ResultSet rs = query("select count (BarberID) from BarberCut where barberID = " + barberIndex);
            amount = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return amount;
    }

    //Method returns an int showing the amount of haircuts in the Haircut Table [all haircuts in database]
    //Method has no parameters,
    public int getAmountOfHaricutRecords() {
        int c = 0;
        try {
            ResultSet t = query("select* from Haircut");

            while (t.next()) {
                c++;
            }
            t.close();
        } catch (SQLException ex) { 
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, null, ex);
        }

        return c;
    }

}// end class DBConnect
