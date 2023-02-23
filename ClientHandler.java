import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final Server server;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            while (true) {
                String message = in.readUTF();
                System.out.println("Message received from " + clientSocket + ": " + message);
                if (message.equals("bye")) {
                    sendMessage("goodbye");
                    break;
                }
                server.broadcastMessage(message, this);
            }
        } catch (IOException e) {
            System.err.println("Error handling client " + clientSocket + ": " + e);
        } finally {
            close();
            server.removeClient(this);
            System.out.println("Client disconnected: " + clientSocket);
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
            System.out.println("["+ getTime() + "]" + " Message sent to " + clientSocket + ": " + message);
        } catch (IOException e) {
            System.err.println("Error sending message to " + clientSocket + ": " + e);
        }
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    private void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing client " + clientSocket + ": " + e);
        }
    }
}
