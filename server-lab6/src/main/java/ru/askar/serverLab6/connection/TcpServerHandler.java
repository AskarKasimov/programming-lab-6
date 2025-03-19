package ru.askar.serverLab6.connection;

import ru.askar.common.Message;
import ru.askar.serverLab6.collection.CollectionManager;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TcpServerHandler implements ServerHandler {

    private boolean status = false;
    private Selector selector;
    private int port;
    private final CollectionManager collectionManager;

    public TcpServerHandler(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean getStatus() {
        return status;
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
    public void stop() {
        status = false;
    }

    @Override
    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            status = true;
            selector = Selector.open();

            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (status) {
                selector.select(); // происходит блокировка, пока не произойдет событие на одном из
                // каналов
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (key.isAcceptable()) {
                        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                        SocketChannel clientChannel = serverChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("Подключен клиент: " + clientChannel.getRemoteAddress());
                    }

                    if (key.isReadable()) {
                        SocketChannel clientChannel = (SocketChannel) key.channel();

                        // чтение длины сообщения (4 байта int'а)
                        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
                        clientChannel.read(lengthBuffer);
                        lengthBuffer.flip();
                        int messageLength = lengthBuffer.getInt();

                        // чтение объекта
                        ByteBuffer messageBuffer = ByteBuffer.allocate(messageLength);
                        while (messageBuffer.hasRemaining()) {
                            clientChannel.read(messageBuffer);
                        }
                        messageBuffer.flip();

                        // десериализация
                        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(messageBuffer.array()))) {
                            Message receivedDTO = (Message) objectInputStream.readObject();
                            System.out.println("Получено от клиента: " + receivedDTO);

                            // подготовка и отправка ответа
                            Message responseDTO = new Message(14, "receivedDTO.getPriority() * 2");
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                            objectOutputStream.writeObject(responseDTO);
                            objectOutputStream.flush();

                            byte[] responseData = byteArrayOutputStream.toByteArray();
                            ByteBuffer responseBuffer = ByteBuffer.allocate(4 + responseData.length);
                            responseBuffer.putInt(responseData.length);
                            responseBuffer.put(responseData);
                            responseBuffer.flip();
                            clientChannel.write(responseBuffer);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            clientChannel.close();
                        }
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (selector != null) {
                try {
                    selector.close(); // Закрываем selector
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Сервер завершил работу.");
        }
    }
}
