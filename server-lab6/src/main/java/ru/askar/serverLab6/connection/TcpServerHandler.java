package ru.askar.serverLab6.connection;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import ru.askar.common.CommandAsList;
import ru.askar.common.CommandToExecute;
import ru.askar.common.cli.CommandExecutor;
import ru.askar.serverLab6.collectionCommand.CollectionCommand;

public class TcpServerHandler implements ServerHandler {
    private final CommandExecutor<CollectionCommand> collectionCommandExecutor;
    private final ArrayList<CommandAsList> commandList;
    private final ConcurrentLinkedQueue<Object> outputQueue = new ConcurrentLinkedQueue<>();
    private int port = -1;
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private boolean running = false;

    public TcpServerHandler(
            CommandExecutor<CollectionCommand> commandExecutor,
            ArrayList<CommandAsList> commandList) {
        this.collectionCommandExecutor = commandExecutor;
        this.commandList = commandList;
    }

    @Override
    public void start() throws IOException {
        if (port == -1) {
            throw new IllegalStateException("Порт не задан");
        }
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(this.port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        outputQueue.clear();
        running = true;
        System.out.println("Сервер запущен на порту " + port);

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
        sendMessage(commandList);
        System.out.println(
                "Клиент подключен: "
                        + clientChannel.getRemoteAddress()
                        + ", список команд отправлен.");
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
                if (dto instanceof CommandToExecute command) {
                    // execute
                    try {
                        System.out.println("Получена команда " + command);
                        CollectionCommand collectionCommand =
                                collectionCommandExecutor.getCommand(command.name());
                        collectionCommand.setObject(command.object());
                        collectionCommand.execute(command.args());
                    } catch (Exception e) {
                        System.out.println(
                                "Ошибка при выполнении полученной команды "
                                        + command.name()
                                        + " : "
                                        + e.getMessage());
                    }
                } else {
                    System.out.println("Сервер не смог обработать полученное сообщение");
                }
                key.attach(null); // Сброс состояния
            }
        }
    }

    private void processOutputQueue() throws IOException {
        for (SelectionKey key : selector.keys()) {
            if (key.channel() instanceof SocketChannel channel && key.isValid()) {
                while (!outputQueue.isEmpty()) {
                    Object message = outputQueue.poll();
                    ByteBuffer data = serialize(message);
                    ByteBuffer header = ByteBuffer.allocate(4).putInt(data.limit()).flip();
                    channel.write(new ByteBuffer[] {header, data});
                }
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
