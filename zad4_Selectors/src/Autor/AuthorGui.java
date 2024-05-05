package Autor;

import javax.swing.*;

public class AuthorGui extends JFrame {
    private JTextField subjectInputField;
    private JTextField infoInputField;
    private JButton submitButton;
    private AuthorService client;
    public AuthorGui() {
        try {
            client = new AuthorService("localhost", 8070);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        setTitle("Author GUI");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();

        JPanel subPanel = new JPanel();
        subPanel.add(new JLabel("Subject:"));
        subjectInputField = new JTextField(20);
        subPanel.add(subjectInputField);
        panel.add(subPanel);

        JPanel infoPanel = new JPanel();
        infoPanel.add(new JLabel("Info:"));
        infoInputField = new JTextField(20);
        infoPanel.add(infoInputField);
        panel.add(infoPanel);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
             var subject = subjectInputField.getText();
             var info = infoInputField.getText();
             var msg = "message:" + subject + ":" + info;
             try {
                 client.send(msg);
                 subjectInputField.setText("");
                 infoInputField.setText("");
             }catch (Exception ex){
                 ex.printStackTrace();
             }
        });
        panel.add(submitButton);

        add(panel);
        this.setVisible(true);
        setLocationRelativeTo(null);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AuthorGui::new);
    }
}
