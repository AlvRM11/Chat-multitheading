package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import Client.threads.Listener;
import Client.threads.Sender;

public class ClientMain {
    public static void main(String[] args) {
        final String host = "localhost";
        final int port = 12345;

        try {
            Socket socket = new Socket(host, port);
            
            DataInputStream inServer = new DataInputStream(socket.getInputStream());
            Listener listener = new Listener(inServer);
            listener.start();

            DataOutputStream outServer = new DataOutputStream(socket.getOutputStream());
            Sender sender = new Sender(outServer);
            sender.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
