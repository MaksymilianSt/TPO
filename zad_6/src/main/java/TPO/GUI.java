package TPO;

import javax.jms.JMSException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private JFrame frame;
    private JTextField messageInput;
    private JTextArea messagesField;
    private MessageService messageService;

    public GUI()  {
        try {
            this.messageService = new MessageService();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        frame = new JFrame("Chat TPO - " + messageService.getUserName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        JLabel inputLabel = new JLabel("Wpisz wiadomość: ");
        inputPanel.add(inputLabel, BorderLayout.WEST);

        messageInput = new JTextField();
        inputPanel.add(messageInput, BorderLayout.CENTER);

        JButton sendButton = new JButton("Wyślij");
        sendButton.addActionListener(e -> {
            String msg = messageInput.getText();
            try {
                messageService.sendMessage(msg);
            } catch (JMSException ex) {
                System.out.println("smt went wrong");
            }
            messagesField.append(messageService.getUserName() + ": " + msg + "\n");
            messageInput.setText("");
        });

        inputPanel.add(sendButton, BorderLayout.EAST);

        frame.add(inputPanel, BorderLayout.SOUTH);

        messagesField = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(messagesField);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);

        messageService.receiveMessages(messagesField);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}