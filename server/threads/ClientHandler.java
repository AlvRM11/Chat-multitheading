package server.threads;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ClientHandler extends Thread {
    
    private final Socket clientSocket;

    private List<DataOutputStream> toClientsStreams;
    private List<String> allMessages;

    public ClientHandler(Socket clientSocket, List<DataOutputStream> toClientsStreams, List<String> allMessages) {
        this.clientSocket = clientSocket;
        this.toClientsStreams = toClientsStreams;
        this.allMessages = allMessages;
    }

    @Override
    public void run() {
        try {
            
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            String name = in.readUTF();
            System.out.println(name + " se ha conectado");

            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            
            toClientsStreams.add(out);
            // FORMATEO allMessages en un solo mensaje y lo envío por el out

            out.writeUTF(formatedMessages());

            while (true) {
                String messageReceived = in.readUTF();

                System.out.println("Message received from " + name + ": " + messageReceived);

                // FORMATEO EL MENSAJE A [HH:mm:ss] NOMBRE: MENSAJE
                String formattedMessage ="[" + getTime() + "] " + name + ": " + messageReceived;
                allMessages.add(formattedMessage);

                for (DataOutputStream dataOutputStream : this.toClientsStreams) {
                    dataOutputStream.writeUTF(formattedMessage);
                }
    
                if (messageReceived.equals("bye")) {
                    toClientsStreams.remove(out);
                    out.writeUTF("goodbye");

                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error handling client " + clientSocket + ": " + e);
        } finally {
            System.out.println("Client disconnected: " + clientSocket);
        }
    }


    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    private String formatedMessages() {
        String formatedMessages = "";

        for (String message : this.allMessages) {
            formatedMessages += message + "\n";
        }

        return formatedMessages;
    }

}
