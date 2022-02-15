package ServerApp;

import java.sql.*;

public class BaseAuthService implements AuthService {
    private static Connection connection;
    private static Statement stmt;


    @Override
    public void start() {
        System.out.println("Сервис аутентификации запущен");
    }

    @Override
    public void stop() {
        System.out.println("Сервис аутентификации остановлен");
    }

    public BaseAuthService() {
        try {
            connect();
            System.out.println("connect");
//            String nick = getNickByLoginPass("log1","pass1");
//            if (nick != null){
//                log.info(nick);
//            } else System.err.println("null");

        } catch (Exception e) {
            e.printStackTrace();
        }
//        finally {
//            disconnect();
//        }

    }

    public static void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users","root","r6Y89dF3");
        stmt = connection.createStatement();
    }

    public static void disconnect() {
        System.out.println("disconnect");
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


    @Override
    public String getNickByLoginPass (String log, String pas) throws SQLException {
        String query = "SELECT * FROM users.user WHERE login = '"+log+"' AND pass = '"+pas+"';";
        ResultSet rs = stmt.executeQuery(query);
        if(rs.next()){
            return rs.getString("nick");
        } else return null;
    }

    @Override
    public String changeNick(String  msg) throws SQLException {
        String[] parts = msg.split("\\s");
        String log = parts [1];
        String pas = parts [2];
        String newNick = parts [3];
        String query = "UPDATE users.user SET nick = 'Liya' WHERE (login = '"+log+"' AND pass = '"+pas+"');";
        stmt.executeUpdate(query);
        return newNick;
        //нужно сделать проверку уникальности ника
    }





}
