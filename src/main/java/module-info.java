module com.example.javafx_networkchat {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javafx_networkchat to javafx.fxml;
    exports com.example.javafx_networkchat;
}