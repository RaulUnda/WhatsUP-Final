package com.example.whatsup.Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import static com.example.whatsup.Client.Client_Controller.users;

public class Certs implements Initializable {
    @FXML
    public TextArea TA_cert;
    @FXML
    public TextField TF_CName;
    @FXML
    public TextField TF_CKey;
    @FXML
    public Button btn_open;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        btn_open.setOnAction(event -> {
            try {
                String unlock_key = "";
                String doc_name = TF_CName.getText();
                String doc_pass = "Llave: " + TF_CKey.getText();
                Desktop desktop = Desktop.getDesktop();
                File file = new File("src/main/java/com/example/whatsup/Certificados/" + doc_name + "_cert.txt");
                if (file.exists()) {
                    Scanner scanner = new Scanner(file);
                    TA_cert.appendText("Se encontro el certificado con el nombre: " + doc_name + "\n");
                    while (scanner.hasNextLine()){
                        String txt_line = scanner.nextLine();
                        if (txt_line.contains("Llave")){
                            unlock_key = txt_line;
                        }
                    }
                    if (doc_pass.equals(unlock_key)){
                        TA_cert.appendText("Llave valida, acceso permitido\n");
                        //printWriter.println(state, name, Pub_key, type);
                        for(User i: users){
                            i.RA_2 = doc_name + "," + TF_CKey.getText();
                        }
                        desktop.open(file);
                    }
                    else{
                        TA_cert.appendText("Llave invalida, intente de nuevo\n");
                    }
                }else if(!file.exists()){
                    TA_cert.appendText("No existe el certificado con nombre: " + doc_name + "\n");
                }
                TA_cert.appendText("~~~~~~~~~~~~~~~~~~~~~~\n");
            }catch (IOException e){
                e.printStackTrace();
            }
        });
    }
}
