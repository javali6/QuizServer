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

public class AppController implements Initializable {
    private static final Integer PORT = 60669;
    private List<String> listOfQuestions;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            Socket clientSocket = serverSocket.accept();
            BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);

            InternetConnection internetConnection = new InternetConnection(
                    clientSocket, blockingQueue);

            internetConnection.addToDeque(internetConnection.getTextFromClient());
            listOfQuestions = getQuestionsFromFile();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void startGame(ActionEvent event) {
        printQuestions(listOfQuestions);

    }

    private void printQuestions(List<String> listOfQuestions) {

    }

    private List<String> getQuestionsFromFile() {
        return null;
    }
}
