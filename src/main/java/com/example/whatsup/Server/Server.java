package com.example.whatsup.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args){
        ServerSocket serverSocket;
        Socket socket;
        try{
            serverSocket = new ServerSocket(1800);
            while (true){
                System.out.println("Esperando clientes...");
                socket = serverSocket.accept();
                System.out.println("Conectado");
                ClientHandler clientThread = new ClientHandler(socket, clients);
                clients.add(clientThread);
                clientThread.start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
