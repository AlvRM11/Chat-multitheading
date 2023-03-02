package Client.threads;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Sender extends Thread {
    private DataOutputStream out;

    public Sender(DataOutputStream out) {
        this.out = out;
    }

    @Override
    public void run() {
        try(Scanner sc = new Scanner(System.in);) {

            System.out.println("Introduce a name:");
            String name = sc.nextLine();
            this.out.writeUTF(name);

            while (true) {
                System.out.println("Introduce a message:");
                String msg = sc.nextLine();

                if (msg.startsWith("message:")) {
                    this.out.writeUTF(msg.substring(8));
                }
                else if (msg.startsWith("bye")) {
                    this.out.writeUTF(msg);
                    break;
                }
                else {
                    System.out.println("The format of the message is: message: <msg>");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
