package com.example.quizserver;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class AppController {
    private static final Integer PORT = 4447;
    private static final Integer MAX_PLAYERS = 3;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private InternetConnection internetConnection;
    private QuestionHandling questionHandling;
    private List<String> listOfQuestions;
    private List<String> listOfAnswers;
    private BlockingQueue<String> blockingQueue;
    @FXML
    private Label answerLabel;
    @FXML
    private Label questionLabel;
    @FXML
    private Button startButton;

    @FXML
    public void initialize() {
        try{
            serverSocket = new ServerSocket(PORT);
            clientSocket = serverSocket.accept();

            listOfQuestions = new ArrayList<>();
            listOfAnswers = new ArrayList<>();
            getQuestionsFromFile();

            blockingQueue = new ArrayBlockingQueue<>(MAX_PLAYERS);
            internetConnection = new InternetConnection(clientSocket, blockingQueue);
            questionHandling = new QuestionHandling(blockingQueue, listOfAnswers,
                    listOfQuestions, questionLabel, answerLabel);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void startGame(ActionEvent event) {
        startButton.setDisable(true);
        Thread icThread = new Thread(internetConnection);
        Thread qhThread = new Thread(questionHandling);

        icThread.start();
        qhThread.start();

        questionLabel.setText(listOfQuestions.get(0));
    }



    private void getQuestionsFromFile() {
        Path path = Path.of("D:/Pytania.txt");
        try(BufferedReader out = Files.newBufferedReader(path)) {
            String[] questions;

            while (out.ready()) {
                String line = out.readLine();
                questions = line.split(";", 0);
                listOfQuestions.add(questions[0]);
                listOfAnswers.add(questions[1]);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
