package ServerApp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;

public class ClientHandler {

    private MyServer myServer;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientHandler(MyServer myServer, Socket socket) {
        try {
            this.myServer = myServer;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.name = "";
            socket.setSoTimeout(1000000);
            new Thread(() -> {
                try {
                    try {
                        authentication();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    readMessages();
                } catch (SocketTimeoutException exception){
                    sendMsg("/timeout");
                    System.out.println("Вылет по таймауту");
                    simpleCloseConnection();
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
                    finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException("Проблемы при создании обработчика клиента");
        }
    }

    public void authentication() throws IOException, SQLException {
        while (true) {
            String str = in.readUTF();
            if (str.startsWith("/auth")) {
                String[] parts = str.split("\\s");
                String nick = myServer.getAuthService().getNickByLoginPass(parts[1], parts[2]);
                if (nick != null) {
                    if (!myServer.isNickBusy(nick)) {
                        socket.setSoTimeout(0);
                        sendMsg("/authok " + nick);
                        name = nick;
                        myServer.broadcastMsg(name + " зашел в чат");
                        myServer.subscribe(this);
                        return;
                    } else {
                        sendMsg("Учетная запись уже используется");
                    }
                } else {
                    sendMsg("Неверные логин/пароль");
                }
            }

        }

    }

    public void readMessages() throws IOException, SQLException {
        while (true) {
            String strFromClient = in.readUTF();
            if (strFromClient.startsWith("/w")) {
                myServer.privateMsg(strFromClient, name);
            } else if (strFromClient.equals("/end")) {
                closeConnection();
                return;
            } else if (strFromClient.startsWith("/change")){
                String oldName = name;
                name = myServer.getAuthService().changeNick(strFromClient);
                if (!myServer.isNickBusy(name)) {
                    myServer.broadcastMsg("Пользователь " + oldName + " сменил ник на " + name);
                } else {
                   sendMsg("Такой ник уже используется");
                }
            } else {
                System.out.println("Отправляю сообщение всем");
                System.out.println("от " + name + ": " + strFromClient);
                myServer.broadcastMsg(name + ": " + strFromClient);
            }
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        myServer.broadcastMsg(name + " вышел из чата");
        simpleCloseConnection();
    }

    public void simpleCloseConnection() {
        myServer.unsubscribe(this);
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }



}
