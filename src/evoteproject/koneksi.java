/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evoteproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ERZA
 */
public class koneksi {
    Connection koneksi;
    
    public Connection connection(){
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost/evote";
        String user = "root";
        String password = "";
        
        try{
            Class.forName(driver);
            koneksi = DriverManager.getConnection(url,user,password);
            return koneksi;
            
            
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver Gagal");
        } catch (SQLException ex) {
            System.out.println("Koneksi Gagal");
        }
        
        return null;
    }
}
