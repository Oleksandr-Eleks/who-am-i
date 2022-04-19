package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Player;

import java.util.ArrayList;
import java.util.List;

public class RandomPlayer implements Player {

    private String name;
    private List<String> availableQuestions;
    private List<String> availableGuesses;
    private static final String PLAYER = "Player: ";

    public RandomPlayer(String name, List<String> availableQuestions, List<String> availableGuesses) {
        this.name = name;
        this.availableQuestions = new ArrayList<>(availableQuestions);
        this.availableGuesses = new ArrayList<>(availableGuesses);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getQuestion() {
        String question = availableQuestions.remove(0);
        System.out.println(PLAYER + name + ". Asks: " + question);
        return question;
    }

    @Override
    public String answerQuestion(String name, String question, String character) {
        String answer = Math.random() < 0.5 ? "Yes" : "No";
        System.out.println(PLAYER + name + ". Answers: " + answer);
        return answer;
    }


    @Override
    public String answerGuess(String name, String guess, String character) {
        String answer = Math.random() < 0.5 ? "Yes" : "No";
        System.out.println(PLAYER + name + ". Answers: " + answer);
        return answer;
    }

    @Override
    public String getGuess() {
        int randomPos = (int) (Math.random() * this.availableGuesses.size());
        String guess = this.availableGuesses.remove(randomPos);
        System.out.println(PLAYER + name + ". Guesses: Am I " + guess);
        return guess;
    }

    @Override
    public boolean isReadyForGuess() {
        return availableQuestions.isEmpty();
    }

    @Override
    public void sendMessage(String message) {

    }
}
