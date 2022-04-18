
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class Client {
    private static String inputMessage;

    public static void main(String args[]) throws Exception {

        Socket clientSocket = new Socket("localhost", 888);

        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

        BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));

        String str;

        System.out.println("Enter your name:");
        new Thread(() -> {
            while (!clientSocket.isClosed()) {
                try {
                    inputMessage = br.readLine();
                    System.out.println(inputMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        clientSocket.close();
                        dos.close();
                        br.close();
                        kb.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
        while (!(str = kb.readLine()).equals("exit")) {
            dos.writeBytes(str + "\n");
        }
        dos.close();
        br.close();
        kb.close();
        clientSocket.close();
    }
}