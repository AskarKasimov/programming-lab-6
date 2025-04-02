package ru.askar.clientLab6.connection;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import ru.askar.common.CommandAsList;

public class TcpClientHandler implements ClientHandler {
    private final ConcurrentLinkedQueue<Object> outputQueue = new ConcurrentLinkedQueue<>();
    private String host = "";
    private int port = -1;
    private Selector selector;
    private SocketChannel channel;
    private volatile boolean running = false;

    @Override
    public void start() throws IOException {
        if (host.isEmpty() || port == -1) {
            throw new IllegalStateException("Нужно указать хост и порт");
        }
        selector = Selector.open();
        channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(host, port));
        channel.register(selector, SelectionKey.OP_CONNECT);
        outputQueue.clear();
        running = true;
        System.out.println("Подключён к серверу " + host + ":" + port);

        new Thread(
                        () -> {
                            try {
                                while (running) {
                                    selector.select(100);
                                    processSelectedKeys();
                                    processOutputQueue();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        })
                .start();
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
                Object dto = deserialize(buf);
                if (dto instanceof ArrayList<?> list
                        && !list.isEmpty()
                        && list.get(0) instanceof CommandAsList) { // избегаю type erasure
                    ArrayList<CommandAsList> commandsAsList = (ArrayList<CommandAsList>) list;
                    System.out.println("Клиент получил команды от сервера: " + commandsAsList);
                } else {
                    System.out.println("Клиент не смог обработать ответ сервера");
                }
                key.attach(null); // Сброс состояния
            }
        }
    }

    private void processOutputQueue() throws IOException {
        if (channel.isConnected()) {
            while (!outputQueue.isEmpty()) {
                Object message = outputQueue.poll();
                ByteBuffer data = serialize(message);
                ByteBuffer header = ByteBuffer.allocate(4).putInt(data.limit()).flip();
                channel.write(new ByteBuffer[] {header, data});
            }
        }
    }

    @Override
    public void sendMessage(Object message) {
        outputQueue.add(message);
    }

    private ByteBuffer serialize(Object dto) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(dto);
            return ByteBuffer.wrap(bos.toByteArray());
        }
    }

    private Object deserialize(ByteBuffer buffer) throws IOException {
        try (ObjectInputStream ois =
                new ObjectInputStream(
                        new ByteArrayInputStream(buffer.array(), 0, buffer.limit()))) {
            return ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void stop() throws IOException {
        running = false;
        selector.close();
        channel.close();
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

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }
}
