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

    private JTextField wordField, languageField;
    private JButton sendButton;
    private JLabel responseLabel;
    private ClientService service;


    public ClientGUI() {
        service = new ClientService();
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

            String translatedWord = service.translateWord(word, language);
            String result = String.format("(PL): %s  -> (%s) : %s", word, language, translatedWord);

            responseLabel.setText(result);
        }
    }

    public static void main(String[] args) {
        new ClientGUI();
    }
}
