package ClientApp;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label chatText;

    @FXML
    private Button sendButton;

    @FXML
    private TextField message;

    @FXML
    private TextField login;

    @FXML
    private TextField pass;

    @FXML
    private Button init;



    //    @FXML
//    private TextArea dialog;
    @FXML
    private ListView<String> dialog;
    public ObservableList<String> list = FXCollections.observableArrayList();


    private Client clientService;


    @FXML
    public void initialize() {
        try {
            clientService = new Client(this);
            clientService.messageFromServer();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        login.setText("login1");
        pass.setText("pass1");

    }

    public void onInitButtonClick () throws IOException {
        clientService.sendMessage("/auth " + login.getText() + " " + pass.getText());
        login.clear();
        pass.clear();
    }





    public void printInDialog(String stringFromServer) {

        ObservableList<String> innerList = FXCollections.observableArrayList(dialog.getItems());
        innerList.add(stringFromServer);
        Platform.runLater(() -> dialog.setItems(innerList));

    }

    @FXML
    public void onSendButtonClick() {
        String str = message.getText();
        try {
            clientService.sendMessage(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        message.clear();
    }


}