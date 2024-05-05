package Client;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ClientGui extends JFrame{
    private JTextField subscriberSubjectInputField;
    private JTextField unsubscriberSubjectInputField;
    private JTextField infoInputField;
    private JButton subscirbeButton;
    private JButton unsubscirbeButton;
    private ClientService client;

    public ClientGui() {
        try {
            client = new ClientService("localhost", 8070);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        setTitle("Client GUI");
        setSize(650, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();


        JPanel subPanel = new JPanel();
        subPanel.add(new JLabel("Subscribe Subject:"));
        subscriberSubjectInputField = new JTextField(20);
        subPanel.add(subscriberSubjectInputField);
        subscirbeButton = new JButton("subscirbe");
        subscirbeButton.setBackground(Color.GREEN);
        subscirbeButton.addActionListener(e -> {
            var sub = subscriberSubjectInputField.getText();
            try {
                client.subscribe(sub);
                subscriberSubjectInputField.setText("");
            }catch(Exception ex){
                ex.printStackTrace();
            }
        });
        subPanel.add(subscirbeButton);
        panel.add(subPanel);

        JPanel unSubPanel = new JPanel();
        unSubPanel.add(new JLabel("UnSubscribe Subject:"));
        unsubscriberSubjectInputField = new JTextField(20);
        unSubPanel.add(unsubscriberSubjectInputField);
        unsubscirbeButton = new JButton("unsubscirbe");
        unsubscirbeButton.setBackground(Color.RED);
        unsubscirbeButton.addActionListener(e -> {
            var sub = unsubscriberSubjectInputField.getText();
            try {
                client.unsubscribe(sub);
                unsubscriberSubjectInputField.setText("");
            }catch(Exception ex){
                ex.printStackTrace();
            }
        });
        unSubPanel.add(unsubscirbeButton);

        panel.add(unSubPanel);

        infoInputField = new JTextField("");
        infoInputField.setColumns(40);
        panel.add(infoInputField);

        add(panel);
        this.setVisible(true);
        setLocationRelativeTo(null);
        updateResponseMessage();

    }
    private void updateResponseMessage(){
        Thread updateThread = new Thread(() -> {
            try {
                while (true) {
                    var msg = client.receiveMessage();
                    SwingUtilities.invokeLater(() -> {
                        infoInputField.setText(msg);
                    });
                }
            } catch ( IOException ex) {
                ex.printStackTrace();
            }
        });
        updateThread.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientGui::new);
    }
}
