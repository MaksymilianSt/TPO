package TPO;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.swing.*;
import java.util.UUID;

public class MessageService {
    private String url;
    private String queueName;
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private String userName = "user" + UUID.randomUUID().toString().substring(1, 6);


    public MessageService(String url, String queueName) throws JMSException {
        this.url = url;
        this.queueName = queueName;
        this.connectionFactory = new ActiveMQConnectionFactory(url);
        this.connection = connectionFactory.createConnection();
        this.connection.start();
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.destination = session.createQueue(queueName);
    }

    public MessageService() throws JMSException {
        this.url = "tcp://localhost:61616";
        this.queueName = "chatTPO";
        this.connectionFactory = new ActiveMQConnectionFactory(url);
        this.connection = connectionFactory.createConnection();
        this.connection.start();
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        this.destination = session.createQueue(queueName);
    }

    public void sendMessage(String userMessage) throws JMSException {
        MessageProducer producer = session.createProducer(destination);
        TextMessage message = session.createTextMessage(userName + ": " + userMessage);
        producer.send(message);
    }


    public void receiveMessages(JTextArea messageArea) {
        try {
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        messageArea.append(textMessage.getText() + "\n");
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUserName() {
        return userName;
    }
}
