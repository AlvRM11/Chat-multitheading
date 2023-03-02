package Client.threads;
import java.io.DataInputStream;
import java.io.IOException;


public class Listener extends Thread {
    
    private DataInputStream in;

    public Listener(DataInputStream in) {
        this.in = in;
    }
    
    @Override
    public void run() {

        try{
            while (true) {
                String message = in.readUTF();
                System.out.println(message);
              

                if (message.equals("goodbye")) {
                    this.closeInput();
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e);
        }
    }

    private void closeInput() {
        try {
            if (in != null)
                in.close();
        } catch (IOException e) {
            System.err.println("Error closing client: " + e);
        }
    }
}
