import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String host;
    private final int port;
    private DataInputStream in;
    private DataOutputStream out;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        
        try(Socket socket = new Socket(host, port);
            Scanner scanner = new Scanner(System.in);) {

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            Thread inputThread = new Thread(() -> {
                try {
                    while (true) {
                        String message = in.readUTF();
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading message from server: " + e);
                }
            });
            inputThread.start();

            Thread outputThread = new Thread(() -> {
                try {
                    while (true) {
                        String message = scanner.nextLine();
                        out.writeUTF(message);
                        if (message.equals("bye")) {
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error sending message to server: " + e);
                } finally {
                    close();
                }
            });
            outputThread.start();

            inputThread.join();
            outputThread.join();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error connecting to server: " + e);
        }
    }

    private void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (IOException e) {
            System.err.println("Error closing client: " + e);
        }
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 12345;
        Client client = new Client(host, port);
        client.start();
    }
}
