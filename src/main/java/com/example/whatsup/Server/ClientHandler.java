package com.example.whatsup.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread{
    private ArrayList<ClientHandler> clients;
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients){
        try{
            this.socket = socket;
            this.clients = clients;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try{
            String msg;
            while ((msg = bufferedReader.readLine()) != null){
                if(msg.equalsIgnoreCase("salir")){
                    break;
                }
                for (ClientHandler clientHandler: clients){
                    clientHandler.printWriter.println(msg);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try {
                bufferedReader.close();
                printWriter.close();
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
