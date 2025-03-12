package ru.askar.serverLab6;

import ru.askar.Message;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
             Selector selector = Selector.open()) {

            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Сервер запущен на порту " + port);

            while (true) {
                selector.select();
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
        }
    }
}