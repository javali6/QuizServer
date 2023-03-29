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

public class AppController implements Runnable {
    private static final Integer PORT = 4447;
    private static final Integer MAX_PLAYERS = 3;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private InternetConnection internetConnection;
    private List<String> listOfQuestions;
    private List<String> listOfAnswers;
    private BlockingQueue<String> blockingQueue;
    private String correctAnswer;
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
    private boolean goodAnswer;
    private boolean threadCondition = true;


    @FXML
    public void initialize() {
        try{
            serverSocket = new ServerSocket(PORT);
            clientSocket = serverSocket.accept();

            blockingQueue = new ArrayBlockingQueue<>(MAX_PLAYERS);
            internetConnection = new InternetConnection(clientSocket, blockingQueue);

            listOfQuestions = new ArrayList<>();
            listOfAnswers = new ArrayList<>();

            getQuestionsFromFile();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void startGame(ActionEvent event) {
        startButton.setDisable(true);
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (threadCondition) {
            printQuestions(listOfQuestions);
            waitForAnswer();

        }
    }

    private void waitForAnswer() {
        try {
            String text = internetConnection.getTextFromClient();
            internetConnection.addToDeque(text);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void checkAnswer(int index) {
        goodAnswer = false;
        String correct = listOfAnswers.get(index);
        var stream = blockingQueue.stream()
                .map(s -> s.split(";", 0)).toList();
        System.out.println("stream: " + stream);

    }

    private void printQuestions(List<String> listOfQuestions) {
        for (int i = 0; i < listOfQuestions.size(); i++) {
            questionLabel.setText(listOfQuestions.get(i));
            checkAnswer(i);
            if(goodAnswer) {
                i++;
            }
        }
        threadCondition = false;
    }

    private void getQuestionsFromFile() {
        Path path = Path.of("D:/Pytania.txt");
        try {
            BufferedReader out = Files.newBufferedReader(path);
            String[] questions;

            while (out.ready()) {
                String line = out.readLine();
                questions = line.split(";", 0);
                listOfQuestions.add(questions[0]);
                listOfAnswers.add(questions[1]);

            }

            System.out.println("Pytania: ");
            System.out.println(listOfQuestions);
            System.out.println("Odpowiedzi: ");
            System.out.println(listOfAnswers);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
