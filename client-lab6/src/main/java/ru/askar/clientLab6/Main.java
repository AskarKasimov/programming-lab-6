package ru.askar.clientLab6;

import ru.askar.common.Message;

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

            System.out.println("Подключение к серверу " + serverAddress + " на порт " + port);

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

            // Чтение длины ответа (4 байта int'а)
            ByteBuffer responseLengthBuffer = ByteBuffer.allocate(4);
            long startTime = System.currentTimeMillis();
            if (waitingForResponse(socketChannel, responseLengthBuffer, startTime)) return;
            int responseLength = responseLengthBuffer.getInt();

            // Чтение самого ответа
            ByteBuffer responseBuffer = ByteBuffer.allocate(responseLength);
            startTime = System.currentTimeMillis();
            if (waitingForResponse(socketChannel, responseBuffer, startTime)) return;

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

    private static boolean waitingForResponse(SocketChannel socketChannel, ByteBuffer responseLengthBuffer, long startTime) throws IOException {
        while (responseLengthBuffer.hasRemaining()) {
            if (System.currentTimeMillis() - startTime > 2000) { // Таймаут 5 секунд
                System.out.println("Таймаут чтения: сервер не отвечает.");
                return true;
            }
            int bytesRead = socketChannel.read(responseLengthBuffer);
            if (bytesRead == -1) {
                System.out.println("Сервер закрыл соединение.");
                return true;
            }
//                if (bytesRead == 0) {
//                    Thread.sleep(100); // Пауза, чтобы не загружать CPU
//                }
        }
        responseLengthBuffer.flip();
        return false;
    }
}
