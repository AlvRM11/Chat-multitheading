package server;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import server.threads.ClientHandler;

public class ServerMain {

    public static void main(String[] args) {
        final int PORT = 12345;
        final List<DataOutputStream> TO_CLIENTS_STREAM = new ArrayList<>();
        final List<String> ALL_MESSAGES = new ArrayList<>();

        try( ServerSocket serverSocket = new ServerSocket(PORT);) {
            System.out.println("Server listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                ClientHandler clientHandler = new ClientHandler(clientSocket, TO_CLIENTS_STREAM, ALL_MESSAGES);
                clientHandler.start();

                System.out.println("New client connected: " + clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
