package com.example.quizserver;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class QuestionHandling implements Runnable{

    public boolean correctAnswer = false;
    private BlockingQueue<String> blockingQueue;
    private List<String> listOfAnswers;
    private List<String> listOfQuestions;
    private Label questionLabel;
    private Label answerLabel;
    private Integer index;
    private String winner;

    @Override
    public void run() {
        for (index = 0; index < listOfQuestions.size(); index++) {
            String answerFromClient = null;
            try {
                answerFromClient = blockingQueue.take();
                String correctAnswerString = listOfAnswers.get(index).toUpperCase();
                String[] temp = answerFromClient.split(";", 0);

                String answer = temp[0].toUpperCase();
                winner = temp[1];
                if(answer.equals(correctAnswerString)) {
                    Platform.runLater(() -> {
                        updateWinner(winner, answer);
                        updateQuestion(index);
                    });

                } else {
                    index--;
                    //wyswietlenie niepoprawnej odpowiedzi
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        Platform.runLater(this::gameOver);

    }

    public QuestionHandling(BlockingQueue<String> blockingQueue, List<String> listOfAnswers,
                            List<String> listOfQuestions, Label questionLabel, Label answerLabel) {
        this.blockingQueue = blockingQueue;
        this.listOfAnswers = listOfAnswers;
        this.listOfQuestions = listOfQuestions;
        this.questionLabel = questionLabel;
        this.answerLabel = answerLabel;
    }

    private void gameOver() {
        questionLabel.setText("NIE MA WIĘCEJ PYTAŃ");
        answerLabel.setText("-");
    }

    private void updateWinner(String winner, String answer) {
        String text = "Odpowiedź na poprzednie pytanie: " + answer + ", Gracz: " + winner;
        answerLabel.setText(text);

    }

    private void updateQuestion(int i) {
        if(i < listOfQuestions.size()) {
            String question = listOfQuestions.get(i);
            questionLabel.setText(question);
        }
    }

}
