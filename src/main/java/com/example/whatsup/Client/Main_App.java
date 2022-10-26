package com.example.whatsup.Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class Main_App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        URL url = Main_App.class.getResource("/com/example.whatsup/Fxml/Login.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Scene scene = new Scene(fxmlLoader.load(), 330, 560);
        stage.setTitle("WhatsUP");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}