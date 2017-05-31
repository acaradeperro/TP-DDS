package model;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 * Created by Colo on 31/5/2017.
 */
public class DbManager {

    static Connection con;

    public static void conectarBd() throws Exception {
        Class.forName(com.mysql.jdbc.Driver.class.getName());
        con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/bd?serverTimezone=UTC", "root", "root");
    }

    public static Connection getConector(){
        return con;
    }

}
