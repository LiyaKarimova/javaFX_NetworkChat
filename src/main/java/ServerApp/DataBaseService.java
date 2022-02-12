package ServerApp;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class DataBaseService {
    private static Connection connection;
    private static Statement stmt;

    public DataBaseService() {
        try {
            connect();
//            String nick = getNickByLoginPass("log1","pass1");
//            if (nick != null){
//                log.info(nick);
//            } else System.err.println("null");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }


    public static void main(String[] args) {
        try {
            connect();
            String nick = getNickByLoginPass("log1","pass1");
            if(nick != null){
                log.info(nick);
            } else System.err.println("null");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    public static void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users","root","r6Y89dF3");
        stmt = connection.createStatement();
    }

    public static void disconnect() {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static String getNickByLoginPass (String log, String pas) throws SQLException {
        String query = "SELECT * FROM users.user WHERE login = '"+log+"' AND pass = '"+pas+"';";
        ResultSet rs = stmt.executeQuery(query);
        if(rs.next()){
            return rs.getString("nick");
        } else return null;
    }

}


