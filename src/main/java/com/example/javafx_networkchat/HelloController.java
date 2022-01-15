package com.example.javafx_networkchat;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.lang.reflect.Array;
import java.util.*;

public class HelloController {
    @FXML
    private Label chatText;

    @FXML
    private Button sendButton;

    @FXML
    private TextField message;

//    @FXML
//    private TextArea dialog;

    @FXML
    private ListView dialog;
   // public ObservableList<String> list;


    @FXML
    public void initialize (){
       // list = FXCollections.observableArrayList();
//      ArrayList <String> messageArray = new ArrayList<>();
        //ObservableList<String> list = FXCollections.observableArrayList();
        //list.add(message.getText());

    }

    @FXML
    public void onSendButtonClick() {
        String str = message.getText();
       // list.add(str);
        //dialog.setItems(list);
        dialog.getItems().add(str);
        message.clear();
    }

//    @FXML
//    private void
}