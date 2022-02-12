package ClientApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

// 1. Клиент подключается
// 2. Клиент авторизуется, формат "/auth login password"
// 3. При успешной авторизации в ответ приходит "/authok " + nick
// 4. Клиент может отправлять сообщения
// 5. Если он хочет выйти, отправляется команда /end


public class Client {
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private HelloController controller;


    //конструктор
    public Client(HelloController controller) throws IOException {
        this.controller = controller;
        openConnection();

    }

    private void openConnection() throws IOException {
        socket = new Socket(SERVER_ADDR, SERVER_PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }


    public void messageFromServer() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String strFromServer = in.readUTF();
                        System.out.println("Получил"+strFromServer);
                        controller.printInDialog(strFromServer);
                        if (strFromServer.equalsIgnoreCase("end")) {
                            closeConnection();
                            break;
                        }
                        if (strFromServer.equals("/timeout")) {
                            controller.printInDialog("Ошибка. Перезагрузите приложение");
                            closeConnection();
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Окончание работы");
                    e.printStackTrace();
                }
            }
        }
        );
        thread.start();

    }


    public void sendMessage(String messageFromClient) throws IOException {
        if (!socket.isClosed()) {
            out.writeUTF(messageFromClient);
        }
    }

    private void closeConnection () {
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


    public static class HelloApplication1 extends Application {
        @Override
        public void start(Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication1.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 369, 469);
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        }

        public static void main(String[] args) {
            launch();
        }
    }
}
