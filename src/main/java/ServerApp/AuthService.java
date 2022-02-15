package ServerApp;

import java.sql.SQLException;

public interface AuthService {
    void start();
    String getNickByLoginPass(String login, String pass) throws SQLException;
    String changeNick (String msg) throws SQLException;
    void stop();

}
