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
        InputStreamReader in = new InputStreamReader(clientSocket.getInputStream());
        BufferedReader br = new BufferedReader(in);

        String text = br.readLine();
        return text;
    }

    public void addToDeque(String text) {
        blockingQueue.add(text);
    }

}