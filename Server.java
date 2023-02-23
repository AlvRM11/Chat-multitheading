import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final int port;
    private final List<ClientHandler> clients = new ArrayList<>();

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                clientHandler.start();
                System.out.println("New client connected: " + clientSocket);
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (!client.equals(sender)) {
                    client.sendMessage(message);
                }
            }
        }
    }

    public void removeClient(ClientHandler client) {
        synchronized (clients) {
            clients.remove(client);
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 12345;
        Server server = new Server(port);
        server.start();
    }
}
