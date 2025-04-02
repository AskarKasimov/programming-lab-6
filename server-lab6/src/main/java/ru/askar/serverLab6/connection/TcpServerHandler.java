package ru.askar.serverLab6.connection;

import ru.askar.common.CommandDTO;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TcpServerHandler implements ServerHandler {
    private int port;
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private boolean running = false;
    private final ConcurrentLinkedQueue<CommandDTO> outputQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void start() throws IOException {
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(this.port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        running = true;

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

            if (key.isAcceptable()) {
                handleAccept(key);
            } else if (key.isReadable()) {
                handleRead(key);
            }
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
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
                System.out.println("Server received: " + dto);
                key.attach(null); // Сброс состояния
            }
        }
    }

    private void processOutputQueue() throws IOException {
        for (SelectionKey key : selector.keys()) {
            if (key.channel() instanceof SocketChannel && key.isValid()) {
                SocketChannel channel = (SocketChannel) key.channel();
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
    }

    @Override
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

    @Override
    public void stop() throws IOException {
        running = false;
        selector.close();
        serverChannel.close();
    }

    @Override
    public boolean getStatus() {
        return running;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }
}
