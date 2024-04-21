package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientGUI extends JFrame implements ActionListener {
    public static final int MAIN_SERVER_PORT = 9876;
    private JTextField wordField, languageField;
    private JButton sendButton;
    private JLabel responseLabel;
    private ServerSocket serverSocket;

    public ClientGUI() {
        try {
            serverSocket = new ServerSocket(6666);
            serverSocket.setSoTimeout(3000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setTitle("Tłumacz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        panel.add(new JLabel("Słowo:"));
        wordField = new JTextField();
        panel.add(wordField);

        panel.add(new JLabel("język:"));
        languageField = new JTextField();
        panel.add(languageField);


        sendButton = new JButton("tłumacz");
        sendButton.addActionListener(this);
        panel.add(sendButton);

        panel.add(new JLabel("odpowiedź:"));
        responseLabel = new JLabel();
        panel.add(responseLabel);

        add(panel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            String word = wordField.getText();
            String language = languageField.getText().toUpperCase();

            String translatedWord = translateWord(word, language);
            String result = String.format("(PL): %s  -> (%s) : %s", word, language, translatedWord);

            responseLabel.setText(result);
        }
    }

    private String translateWord(String word, String translateTo) {
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

    public static void main(String[] args) {
        new ClientGUI();
    }
}
