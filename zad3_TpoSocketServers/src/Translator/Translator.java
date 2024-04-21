package Translator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class Translator implements Runnable {
    public final int SERVER_PORT;
    private final Map<String, String> words;

    public Translator(int serverPort, Map<String, String> words) {
        this.SERVER_PORT = serverPort;
        this.words = words;
    }

    @Override
    public void run() {
        System.out.println("Translation Server started");
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            while (true) {
                Socket newClient = serverSocket.accept();
                new Thread(() -> handleRequest(newClient))
                        .start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(Socket mainServerSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(mainServerSocket.getInputStream()));

            String clientReq = in.readLine();
            logger(mainServerSocket, clientReq);

            String[] args = clientReq.split(",");
            String worldToTranslate = args[0];
            String clientAddress = args[1];
            Integer clientPort = Integer.valueOf(args[2]);

            String response = words.getOrDefault(worldToTranslate, "nie ma takiego słowa :(");

            Socket clientsocket = new Socket(clientAddress, clientPort);
            new PrintWriter(clientsocket.getOutputStream(), true)
                    .println(response);

            clientsocket.close();
            mainServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void logger(Socket socket, String message) {
        System.out.println("{TRANSLATOR::} client : " + socket.getInetAddress() + ":" + socket.getPort() + ": " + message);
    }

    public static void main(String[] args) {
        Translator EN = new Translator(7776, Map.of(
                "dom", "house",
                "pies", "dog",
                "kot", "cat",
                "piwo", "beer"
        ));
        new Thread(EN).start();

        Translator FR = new Translator(7777, Map.of(
                "dom", "Maison",
                "pies", "chien",
                "kot", "chat",
                "piwo", "bière"
        ));
        new Thread(FR).start();

        Translator JP = new Translator(7778, Map.of(
                "dom", "家",
                "pies", "犬",
                "kot", "猫",
                "piwo", "ビール"
        ));
        new Thread(JP).start();
    }
}
