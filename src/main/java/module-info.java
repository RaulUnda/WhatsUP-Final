module com.example.whatsup {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;

    exports com.example.whatsup.Client;
    opens com.example.whatsup.Client to javafx.fxml;
    exports com.example.whatsup.Server;
    opens com.example.whatsup.Server to javafx.fxml;
}