package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Player;

import java.util.ArrayList;
import java.util.List;

public class RandomPlayer implements Player {

    private String name;
    private List<String> availableQuestions;
    private List<String> availableGuesses;

    public RandomPlayer(String name, List<String> availableQuestions, List<String> availableGuesses) {
        this.name = name;
        this.availableQuestions = new ArrayList<String>(availableQuestions);
        this.availableGuesses = new ArrayList<String>(availableGuesses);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getQuestion() {
        String question;
        if (name.equals("Bot")) {
            question = availableQuestions.remove(availableQuestions.size() - 1);
        } else {
            question = availableQuestions.remove(0);
        }
        System.out.println("Player: " + name + ". Asks: " + question);
        return question;
    }

    @Override
    public String answerQuestion(String question, String character, String nameAsker) {
        String answer = Math.random() < 0.5 ? "Yes" : "No";
        System.out.println("Player: " + name + ". Answers: " + answer);
        return answer;
    }

    @Override
    public String answerGuess(String guess, String character) {
        String answer;
        if (guess.equals(character)) {
            answer = "Yes";
        } else {
            answer = "No";
        }
        System.out.println("Player: " + name + ". Answers: " + answer);
        return answer;
    }

    @Override
    public String getGuess() {
        int randomPos = (int)(Math.random() * this.availableGuesses.size());
        String guess = this.availableGuesses.remove(randomPos);
        System.out.println("Player: " + name + ". Guesses: Am I " + guess);
        return guess;
    }

    @Override
    public boolean isReadyForGuess() {
        return availableQuestions.isEmpty();
    }

    @Override
    public void clearQuestions() {
        availableQuestions.clear();
    }

}
