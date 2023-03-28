package com.example.quizserver;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AppController {
    private static final Integer PORT = 4447;
    private List<String> listOfQuestions;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private InternetConnection internetConnection;

    BlockingQueue<String> blockingQueue;
    @FXML
    private Label answerLabel;

    @FXML
    private Label numberOfPlayers;

    @FXML
    private Label questionLabel;

    @FXML
    private Button startButton;

    @FXML
    private Label timerLabel;


    @FXML
    public void initialize() {
        try{
            serverSocket = new ServerSocket(PORT);
            clientSocket = serverSocket.accept();
            System.out.println("Connected 46");

            blockingQueue = new ArrayBlockingQueue<>(3);
            System.out.println("Kolejka 49");
            internetConnection = new InternetConnection(clientSocket, blockingQueue);
            System.out.println("IC 52");

            listOfQuestions = getQuestionsFromFile();
            System.out.println("56 lista");

            internetConnection.getTextFromClient();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void startGame(ActionEvent event) {
        System.out.println("duppa");
        printQuestions(listOfQuestions);

    }

    private void printQuestions(List<String> listOfQuestions) {

    }

    private List<String> getQuestionsFromFile() {
        return null;
    }
}
