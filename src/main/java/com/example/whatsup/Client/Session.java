package com.example.whatsup.Client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.awt.Desktop;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.security.*;
import java.util.ResourceBundle;

import static com.example.whatsup.Client.Client_Controller.user;
import static com.example.whatsup.Client.Client_Controller.users;

public class Session extends Thread implements Initializable {
    @FXML
    public Pane pn_pf;
    @FXML
    public Label lbl_Name;
    @FXML
    public Label lbl_Email;

    @FXML
    public Pane pn_chat;
    @FXML
    public TextArea TA_Room;
    @FXML
    public TextField tf_msgField;

    @FXML
    public Label lbl_ClientName;
    @FXML
    public Button btn_profile;
    @FXML
    private Button btn_plain;
    @FXML
    private Button btn_sym;
    @FXML
    private Button btn_asym;
    @FXML
    private Button btn_firm;
    @FXML
    private Button btn_cert;
    @FXML
    private Label lbl_key;
    @FXML
    private TextField tf_key;
    private int PubKey = 0;
    private int PrivKey = 0;
    private String firm = null;
    public InetAddress host;
    public boolean toggleChat = false, toggleProfile = false;
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    Socket socket;

    public void connectSocket(){
        try{
            host = InetAddress.getLocalHost();
            socket = new Socket(host, 1800);
            System.out.println("Socket conectado al servidor");
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        String encryptmsg;
        try{
            while (true) {
                String msg = bufferedReader.readLine();
                String[] tokens = msg.split("~");
                String cmd = tokens[0];
                System.out.println(cmd);
                StringBuilder fullmsg = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fullmsg.append(tokens[i]);
                }
                System.out.println(fullmsg);
                if (cmd.equalsIgnoreCase(Client_Controller.user + ":")) {
                    continue;
                } else if (fullmsg.toString().equalsIgnoreCase("Adios" )) {
                    TA_Room.appendText("**Otro usuario ha salido del chat**\n");
                    break;
                }
                if (PrivKey > 0 && PubKey == 0) {
                    encryptmsg = tokens[0];
                    encryptmsg += Encrypt(tokens[1], PrivKey);
                    TA_Room.appendText(encryptmsg + "\n");
                } else if (PubKey != 0) {
                    encryptmsg = tokens[0];
                    encryptmsg += Encrypt(tokens[1], PubKey);
                    TA_Room.appendText(encryptmsg + "\n");
                }else {
                    TA_Room.appendText(tokens[0] + tokens[1] + "\n");
                }
            }
            bufferedReader.close();
            printWriter.close();
            socket.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleProfileButton(ActionEvent event){
        if(event.getSource().equals(btn_profile) && !toggleProfile){
            pn_pf.toFront();
            pn_chat.toBack();
            toggleProfile = true;
            toggleChat = false;
            btn_profile.setText("Regresar");
            setProfile();
        } else if (event.getSource().equals(btn_profile) && toggleProfile) {
            pn_chat.toFront();
            pn_pf.toBack();
            toggleProfile = false;
            toggleChat = false;
            btn_profile.setText("Perfil");
        }
    }

    public void setProfile(){
        for(User i: users){
            if (Client_Controller.user.equalsIgnoreCase(i.username)){
                lbl_Name.setText(i.Fullname);
                lbl_Name.setOpacity(1);
                lbl_Email.setText(i.email);
                lbl_Email.setOpacity(1);
            }
        }
    }

    public void handleSendEvent(MouseEvent event){
        send();
        for(User i: users){
            System.out.println(i.username);
        }
    }

    public void send(){
        String msg = tf_msgField.getText();
        printWriter.println(user+ ":~ " + msg);
        TA_Room.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        TA_Room.appendText("Yo: " + msg + "\n");
        tf_msgField.setText("");
        if (msg.equalsIgnoreCase("Adios")|| msg.equalsIgnoreCase("LogOut")){
            System.exit(0);
        }
    }

    public void sendMessageByKey(KeyEvent event){
        if (event.getCode().toString().equals("ENTER")){
            send();
        }
    }

    public String Encrypt(String msg, int key){
        StringBuilder encrypted = new StringBuilder();
        for(int i=0; i<msg.length(); i++)
        {
            char letter = msg.charAt(i);
            if (letter >= 'a' && letter <= 'z'){
                letter = (char)(letter + key);
                if(letter > 'z') {
                    letter = (char)(letter - 'z' + 'a' - 1);
                }
                encrypted.append(letter);
            }
            else if (letter >= 'A' && letter <= 'Z'){
                letter = (char)(letter + key);
                if (letter > 'Z'){
                    letter = (char)(letter - 'Z' + 'A' -1);
                }
                encrypted.append(letter);
            }
            else{
                encrypted.append(letter);
            }
        }
        return encrypted.toString();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        lbl_ClientName.setText(user);
        connectSocket();
        btn_asym.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lbl_key.setOpacity(1);
                tf_key.setOpacity(1);
                PrivKey = 0;
                PubKey = Integer.parseInt(tf_key.getText());
            }
        });
        btn_sym.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lbl_key.setOpacity(0);
                tf_key.setOpacity(0);
                PrivKey = 13;
                PubKey = 0;
            }
        });
        btn_plain.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lbl_key.setOpacity(0);
                tf_key.setOpacity(0);
                PrivKey = 0;
                PubKey = 0;
            }
        });
        btn_cert.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(firm != null && PubKey != 0){
                    try {
                        File user_cert = new File("src/main/java/com/example/whatsup/Certificados/"+user + "_cert.txt");
                        if (!Desktop.isDesktopSupported()){
                            System.out.println("Desktop is not supported");
                            return;
                        }
                        Desktop desktop = Desktop.getDesktop();
                        if (user_cert.createNewFile()){
                            System.out.println("Certificado creado: " + user_cert.getName());
                            try {
                                FileWriter write_cert = new FileWriter(user_cert);
                                write_cert.write("\nFirma: " + firm + "\nNombre: " +
                                        lbl_Name.getText() + "\nEmail: " + lbl_Email.getText() + "\nLlave: " + PubKey);
                                write_cert.close();
                                System.out.println("Se ha escrito el certificado: " + user_cert);
                                if (user_cert.exists()){desktop.open(user_cert);}
                            }catch (IOException e){
                                System.out.println("Error al modificar el certificado");
                                e.printStackTrace();
                            }
                        }else {
                            System.out.println("Certificado ya existe");
                        }
                    }catch (IOException e)
                    {
                        System.out.println("Error al crear el certificado");
                        e.printStackTrace();
                    }
                }
            }
        });
        btn_firm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (PubKey != 0){
                    String efirm;
                    Signature signature;
                    try {
                        signature = Signature.getInstance("SHA256withRSA");
                        SecureRandom secureRandom = new SecureRandom();
                        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                        KeyPair keyPair = keyPairGenerator.generateKeyPair();
                        signature.initSign(keyPair.getPrivate(), secureRandom);
                        byte[] data = (user+PubKey).getBytes("UTF-8");
                        signature.update(data);
                        efirm = String.valueOf(signature.sign());
                        signature.initVerify(keyPair.getPublic());

                    } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException |
                             UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    firm = efirm + user + PubKey;
                    System.out.println("Firma: " + firm);
                    TA_Room.appendText("**La firma del usuario " + user + " es: " + firm + "**\n");
                }
            }
        });
    }
}
