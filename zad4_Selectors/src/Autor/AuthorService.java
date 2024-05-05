package Autor;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

public class AuthorService {
    private SocketChannel socketChannel;

    public AuthorService(String serverAddress, int port) throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(serverAddress, port));
    }

    public void send(String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        socketChannel.write(buffer);
    }
}
