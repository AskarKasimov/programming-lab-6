package ru.askar.clientLab6;

import ru.askar.common.CommandDTO;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Client {
    private Selector selector;
    private SocketChannel channel;
    private volatile boolean running = true;
    private final ConcurrentLinkedQueue<CommandDTO> outputQueue = new ConcurrentLinkedQueue<>();

    public void connect(String host, int port) throws IOException {
        selector = Selector.open();
        channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(host, port));
        channel.register(selector, SelectionKey.OP_CONNECT);

        new Thread(() -> {
            try {
                while (running) {
                    selector.select(100);
                    processSelectedKeys();
                    processOutputQueue();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void processSelectedKeys() throws IOException {
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iter = keys.iterator();

        while (iter.hasNext()) {
            SelectionKey key = iter.next();
            iter.remove();

            if (key.isConnectable()) {
                handleConnect(key);
            } else if (key.isReadable()) {
                handleRead(key);
            }
        }
    }

    private void handleConnect(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        if (channel.finishConnect()) {
            channel.register(selector, SelectionKey.OP_READ);
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buf = (ByteBuffer) key.attachment();

        if (buf == null) {
            buf = ByteBuffer.allocate(4); // Буфер для размера
            key.attach(buf);
        }

        int read = channel.read(buf);
        if (read == -1) {
            channel.close();
            return;
        }

        if (!buf.hasRemaining()) {
            buf.flip();

            if (buf.capacity() == 4) {
                int size = buf.getInt();
                key.attach(ByteBuffer.allocate(size)); // Новый буфер для данных
            } else {
                CommandDTO dto = deserialize(buf);
                System.out.println("Client received: " + dto);
                key.attach(null);  // Сброс состояния
            }
        }
    }

    private void processOutputQueue() throws IOException {
        if (channel.isConnected()) {
            while (!outputQueue.isEmpty()) {
                CommandDTO message = outputQueue.poll();
                ByteBuffer data = serialize(message);
                ByteBuffer header = ByteBuffer.allocate(4)
                        .putInt(data.limit())
                        .flip();
                channel.write(new ByteBuffer[]{header, data});
            }
        }
    }

    public void sendMessage(CommandDTO message) {
        outputQueue.add(message);
    }

    private ByteBuffer serialize(CommandDTO dto) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(dto);
            return ByteBuffer.wrap(bos.toByteArray());
        }
    }

    private CommandDTO deserialize(ByteBuffer buffer) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(buffer.array(), 0, buffer.limit()))) {
            return (CommandDTO) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    public void stop() throws IOException {
        running = false;
        selector.close();
        channel.close();
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connect("localhost", 1234);
        client.sendMessage(new CommandDTO("REQUEST", "Hello from client!"));
    }
}
