import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Server {
    private static final String REQUEST_DATA_SEPARATOR = ":";
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private Map<String, Set<SocketChannel>> subjectSubscribers;


    public Server(int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        subjectSubscribers = new HashMap<>();
    }

    public void run() throws IOException {
        while (true) {
            selector.select();
            for (Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                 iterator.hasNext();
            ) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isReadable()) {
                    processClient(key);
                }
                if (key.isAcceptable()) {
                    acceptClient();
                }
            }
        }
    }

    private void acceptClient() throws IOException {
        SocketChannel clientChannel = serverSocketChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
    }

    private void processClient(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        clientChannel.read(buffer);
        String message = new String(buffer.array()).trim();

        String[] parts = message.split(REQUEST_DATA_SEPARATOR);
        if(parts.length < 2) return;

        String command = parts[0];
        String subject = parts[1];

        switch(command){
            case "subscribe" -> {
                log("new sub from :", key);
                subscribeClient(clientChannel, subject);
            }
            case "unsubscribe" -> {
                log("new unsub from :", key);
                unsubscribeClient(clientChannel, subject);
            }
            case "message" -> {
                log("new msg from :", key);
                notifySubscribers(message);
            }
        }
        System.out.println("clients' status:" + subjectSubscribers);
    }

    private void subscribeClient(SocketChannel channel, String subject) {
        Set<SocketChannel> subscribers = subjectSubscribers.getOrDefault(subject, new HashSet<>());
        subscribers.add(channel);
        subjectSubscribers.put(subject, subscribers);
    }

    private void unsubscribeClient(SocketChannel channel, String subject) {
        Set<SocketChannel> subscribers = subjectSubscribers.getOrDefault(subject, new HashSet<>());
        subscribers.remove(channel);
        subjectSubscribers.put(subject, subscribers);
    }

    private void notifySubscribers(String message) {
        String[] parts = message.split(REQUEST_DATA_SEPARATOR);
        String subject = parts[1];
        String msg = parts[2];

        Set<SocketChannel> subscribers = subjectSubscribers
                .getOrDefault(subject, new HashSet<>());
        var info = "[" + subject + "]" + ":" + msg;

        sendMessage(subscribers, info);
    }

    private void sendMessage(Collection<SocketChannel> subs, String msg) {
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        subs.forEach((subscriber) -> {
            try {
                subscriber.write(buffer);
                buffer.rewind();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void log(String msg, SelectionKey src) throws IOException {
        System.out.println(msg + ((SocketChannel) src.channel()).getRemoteAddress());
    }

    public static void main(String[] args) {
        int port = 8070;
        try {
            Server server = new Server(port);
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
