package Client;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

public class ClientService {
    private SocketChannel socketChannel;

    public ClientService(String serverAddress, int port) throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(serverAddress, port));
    }
    public String receiveMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        socketChannel.read(buffer);
        return new String(buffer.array()).trim();
    }
    public void subscribe(String subject) throws IOException {
        sendRequest("subscribe:" + subject);
    }

    public void unsubscribe(String subject) throws IOException {
        sendRequest("unsubscribe:" + subject);
    }

    private void sendRequest(String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        socketChannel.write(buffer);
    }


}
