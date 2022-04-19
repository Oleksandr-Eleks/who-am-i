package com.eleks.academy.whoami.core;

public interface Player {

    String getName();

    String getQuestion();

    String answerQuestion(String name, String question, String character);

    String getGuess();

    boolean isReadyForGuess();

    String answerGuess(String name, String guess, String character);

    void sendMessage(String message);

}
