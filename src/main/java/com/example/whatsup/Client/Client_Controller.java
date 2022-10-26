package com.example.whatsup.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Client_Controller {
    @FXML
    public Pane pn_Register;
    @FXML
    public Button btnSignIn;
    @FXML
    public ImageView btn_back;
    @FXML
    public TextField Name_reg;
    @FXML
    public TextField Username_reg;
    @FXML
    public TextField Email_reg;
    @FXML
    public PasswordField Pswd_reg;
    @FXML
    public Label lbl_Intruct;
    @FXML
    public Label lbl_Result;
    @FXML
    public Label lbl_Guide;
    @FXML
    public Label lbl_Repeat;
    @FXML
    public Label lbl_RptEmail;

    @FXML
    public Pane pn_Sign;
    @FXML
    public Button btnSignUp;
    @FXML
    public TextField username;
    @FXML
    public PasswordField password;
    @FXML
    public Label lb_not;

    public static String user, pswd;
    public static ArrayList<User> loggedUsers = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<>();
    public void registraton(){
        if(!Name_reg.getText().equalsIgnoreCase("") && !Username_reg.getText().equalsIgnoreCase("") && !Pswd_reg.getText().equalsIgnoreCase("") && !Email_reg.getText().equalsIgnoreCase("")){
            if(checkUser(Username_reg.getText())){
                if (checkEmail(Email_reg.getText())){
                    User newUser = new User();
                    newUser.username = Username_reg.getText();
                    newUser.password = Pswd_reg.getText();
                    newUser.Fullname = Name_reg.getText();
                    newUser.email = Email_reg.getText();
                    users.add(newUser);
                    lbl_Guide.setOpacity(1);
                    lbl_Result.setOpacity(1);
                    Defaults();
                    if (lbl_Intruct.getOpacity() == 1){
                        lbl_Intruct.setOpacity(0);
                    }
                    if (lbl_Repeat.getOpacity() == 1){
                        lbl_Repeat.setOpacity(0);
                    }
                } else {
                    lbl_RptEmail.setOpacity(1);
                    setOpacity(lbl_Repeat, lbl_Guide, lbl_Intruct, lbl_Result);
                }
            } else {
                lbl_Repeat.setOpacity(1);
                setOpacity(lbl_RptEmail, lbl_Guide, lbl_Intruct, lbl_Result);
            }
        } else{
            lbl_Intruct.setOpacity(1);
            setOpacity(lbl_Repeat, lbl_RptEmail, lbl_Guide, lbl_Result);
        }
    }

    private void setOpacity(Label A, Label B, Label C, Label D){
        if (A.getOpacity() == 1 || B.getOpacity() == 1 || C.getOpacity() == 1 || D.getOpacity() == 1){
            A.setOpacity(0);
            B.setOpacity(0);
            C.setOpacity(0);
            D.setOpacity(0);
        }
    }

    private boolean checkUser(String user){
        for(User user1: users){
            if(user1.username.equals(user)){
                return false;
            }
        }
        return true;
    }
    private boolean checkEmail(String email){
        for(User user : users){
            if(user.email.equals(email)){
                return false;
            }
        }
        return true;
    }
    private void Defaults(){
        Username_reg.setText("");
        Email_reg.setText("");
        Pswd_reg.setText("");
        Name_reg.setText("");
        lbl_Repeat.setOpacity(0);
        lbl_RptEmail.setOpacity(0);
        lbl_Guide.setOpacity(0);
    }

    public void login(){
        user = username.getText();
        pswd = password.getText();
        boolean login = false;
        for (User i: users){
            if(i.username.equalsIgnoreCase(user) && i.password.equalsIgnoreCase(pswd)){
                login = true;
                loggedUsers.add(i);
                System.out.println(i.username);
                break;
            }
        }
        if(login){ChangeWindow();}
        else {lb_not.setOpacity(1);}
    }

    public void ChangeWindow(){
        try{
            Stage stage = (Stage) username.getScene().getWindow();
            URL url = Main_App.class.getResource("/com/example.whatsup/Fxml/Session.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Scene scene = new Scene(fxmlLoader.load(), 330, 560);
            stage.setScene(scene);
            stage.setTitle(user + "");
            stage.setOnCloseRequest(event -> System.exit(0));
            stage.setResizable(false);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event){
        if(event.getSource().equals(btnSignUp)){
            pn_Register.toFront();
            pn_Sign.toBack();
        }
        if (event.getSource().equals(btnSignIn)){
            pn_Sign.toFront();
            pn_Register.toBack();
        }
        lb_not.setOpacity(0);
        username.setText("");
        password.setText("");
    }

    @FXML
    private void handleMouseEvent(MouseEvent event){
        if (event.getSource() == btn_back){
            pn_Sign.toFront();
            pn_Register.toBack();
        }
        Name_reg.setText("");
        Pswd_reg.setText("");
        Email_reg.setText("");
    }
}