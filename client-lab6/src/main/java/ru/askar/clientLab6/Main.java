package ru.askar.clientLab6;

import ru.askar.Message;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

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

            // Создаем объект DTO
            Message dto = new Message(123, "Heelllooo");

            // Сериализуем объект в байты
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(dto);
            objectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();

            // Отправка данных на сервер
            ByteBuffer buffer = ByteBuffer.wrap(data);
            socketChannel.write(buffer);

            // Чтение ответа от сервера
            ByteBuffer responseBuffer = ByteBuffer.allocate(1024);
            while (socketChannel.read(responseBuffer) <= 0) {
                // Ожидание данных от сервера
            }

            responseBuffer.flip();
            byte[] responseData = new byte[responseBuffer.remaining()];
            responseBuffer.get(responseData);

            try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(responseData))) {
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