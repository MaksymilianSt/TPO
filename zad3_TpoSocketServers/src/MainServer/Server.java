package MainServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Server {

    public static final int SERVER_PORT = 9876;
    private final Map<String, Integer> translatorServers;

    public Server(Map<String, Integer> translatorServers) {
        this.translatorServers = translatorServers;
        System.out.println("MainServer Server started");
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            while (true) {
                Socket newClient = serverSocket.accept();
                new Thread(() -> handleClient(newClient))
                        .start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleClient(Socket client) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String clientReq = in.readLine();
            logger(client, clientReq);

            String[] args = clientReq.split(",");
            String worldToTranslate = args[0];
            String language = args[1];
            Integer port = Integer.valueOf(args[2]);

            if (!translatorServers.containsKey(language)) {
                Socket socket = new Socket(InetAddress.getLocalHost(), port);
                new PrintWriter(socket.getOutputStream(), true)
                        .println("Błędny język");

                socket.close();
            } else {
                String reqToTranslate = worldToTranslate + "," + client.getInetAddress().toString().substring(1) + "," + port;

                Socket translationSocket = new Socket(InetAddress.getLocalHost(), translatorServers.get(language));
                new PrintWriter(translationSocket.getOutputStream(), true)
                        .println(reqToTranslate);

                translationSocket.close();
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTranslator(String languageCode, Integer port) {
        translatorServers.put(languageCode, port);
    }

    private static void logger(Socket socket, String message) {
        System.out.println("{SERVER::} client : " + socket.getInetAddress() + ":" + socket.getPort() + ": " + message);
    }

    public static void main(String[] args) {
        new Server(
                Map.of(
                        "EN", 7776,
                        "FR", 7777,
                        "JP", 7778
                )
        );
    }
}
