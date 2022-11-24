package com.example.whatsup.Server;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class RA_controller implements Initializable {

    @FXML
    public TextArea TA_logs;
    public int port = 3600;
    private BufferedReader bufferedReader;
    public String username;
    public Socket socket;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (IOException e){
            e.printStackTrace();
        }
        ReceiveData();
    }
    private void ReceiveData(){
        new Thread(()->{
            while (socket.isConnected()){
                try {
                    System.out.println("Esperando...");
                    String info = bufferedReader.readLine();
                    //printWriter.println(state, name, Pub_key, type);
                    String[] tokens = info.split(",");
                    if (Objects.equals(tokens[3], "user")){
                        username = tokens[1];
                    }
                    System.out.println("token 0: " + tokens[0]);
                    switch (tokens[0]) {
                        case "1" ->
                                TA_logs.appendText("[Puerto " + port + "] Se ha creado el certificado para el usuario " + username + "\n");
                        case "2" ->
                                TA_logs.appendText("[Puerto " + port + "] Usuario " + username + " accedio al certificado " + tokens[1] + " con la llave " + tokens[2] + "\n");
                        default -> System.out.println("Esperando...");
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    try {
                        socket.close();
                        bufferedReader.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }).start();
    }
}
