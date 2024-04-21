package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientService {
    public static final int MAIN_SERVER_PORT = 9876;
    private ServerSocket serverSocket;

    public ClientService() {
        try {
            serverSocket = new ServerSocket(6666);
            serverSocket.setSoTimeout(3000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String translateWord(String word, String translateTo) {
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), MAIN_SERVER_PORT);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

            String req = word + "," + translateTo + "," + serverSocket.getLocalPort();
            printWriter.println(req);
            socket.close();

            Socket accept = serverSocket.accept();
            String response = new BufferedReader(new InputStreamReader(accept.getInputStream()))
                    .readLine();

            System.out.println("Server response : " + response);
            accept.close();

            return response;
        } catch (IOException e) {
            return " something went wrong:(";
        }
    }
}
