package ru.askar.clientLab6;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import ru.askar.Message;

public class Main {

    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 12345;

        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(serverAddress, port));

            while (!socketChannel.finishConnect()) {
                System.out.println("Установка соединения...");
            }

            System.out.println("Соединение установлено.");

            Message dto = new Message(123, "Heelllooo");

            // сериализация
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(dto);
            objectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();

            // объединение длины и сообщения в один буфер
            ByteBuffer combinedBuffer = ByteBuffer.allocate(4 + data.length);
            combinedBuffer.putInt(data.length); // длина сообщения (4 байта int'а)
            combinedBuffer.put(data); // само сообщение
            combinedBuffer.flip();

            // отправка
            socketChannel.write(combinedBuffer);

            // чтение длины сообщения (4 байта int'а)
            ByteBuffer responseLengthBuffer = ByteBuffer.allocate(4);
            while (responseLengthBuffer.hasRemaining()) {
                socketChannel.read(responseLengthBuffer);
            }
            responseLengthBuffer.flip();
            int responseLength = responseLengthBuffer.getInt();

            // получаем само сообщение
            ByteBuffer responseBuffer = ByteBuffer.allocate(responseLength);
            while (responseBuffer.hasRemaining()) {
                socketChannel.read(responseBuffer);
            }
            responseBuffer.flip();

            // десериализация сообщения
            byte[] responseData = new byte[responseBuffer.remaining()];
            responseBuffer.get(responseData);

            try (ObjectInputStream objectInputStream =
                    new ObjectInputStream(new ByteArrayInputStream(responseData))) {
                Message responseDTO = (Message) objectInputStream.readObject();
                System.out.println("Ответ от сервера: " + responseDTO);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
