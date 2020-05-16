package db;

import java.sql.*;

public class dbConexion {

   private static Connection con = null;

   public static Connection getConnection(){
      try{
         if( con == null ){
            String driver="com.mysql.jdbc.Driver"; 
            String url="jdbc:mysql://localhost/calculation?useSSL=false";
            String pwd="root";
            String usr="root";
            Class.forName(driver);
            con = DriverManager.getConnection(url,usr,pwd);
         }
     }
     catch(ClassNotFoundException | SQLException ex){
        ex.printStackTrace();
     }
     return con;
   }
}