package ServerApp;

import ClientApp.HelloController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {
    private final int PORT = 8189;
    private List<ClientHandler> clients;
    private AuthService authService;
    private DataBaseService dataBaseService;

    public AuthService getAuthService() {
        return authService;
    }

    public DataBaseService getDataBaseService() {
        return dataBaseService;
    }

    public MyServer() {
        Socket socket;
        try (ServerSocket server = new ServerSocket(PORT)) {
            authService = new BaseAuthService();
            authService.start();
            clients = new ArrayList<>();
            while (true) {
                System.out.println("Сервер ожидает подключения");
                socket = server.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            System.out.println("Ошибка в работе сервера");
        } finally {
            if (authService != null) {
                authService.stop();
            }
        }
    }

    public synchronized boolean isNickBusy (String nick) {
        for (ClientHandler o : clients) {
            if (o.getName().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void privateMsg (String msg, String name) {
        String[] parts = msg.split("\\s");
        String nick = parts [1];
        for (ClientHandler o : clients) {
            System.out.println(o.getName());
            if (o.getName().equals(nick)) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 2; i < parts.length; i++) {
                    stringBuilder.append(parts[i]).append(" ");
                }
                o.sendMsg(name + " private " + ": " + stringBuilder);
                return;
            }
        }
    }

    public synchronized void broadcastMsg(String msg) {
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
    }

    public synchronized void unsubscribe(ClientHandler o) {
        clients.remove(o);
    }

    public synchronized void subscribe(ClientHandler o) {
        clients.add(o);
    }
}


