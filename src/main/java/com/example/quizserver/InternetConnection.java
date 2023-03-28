package com.example.quizserver;



import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;


public class InternetConnection {

    private Socket clientSocket;

    private BlockingQueue<String> blockingQueue;

    public InternetConnection(Socket clientSocket, BlockingQueue<String> blockingQueue) {
        this.clientSocket = clientSocket;
        this.blockingQueue = blockingQueue;
    }

    public String getTextFromClient() throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        String text = dataInputStream.readUTF();
        System.out.println("Odebrano: " + text);
        return text;
    }

    public void addToDeque(String text) {
        blockingQueue.add(text);
    }

}
