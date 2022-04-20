package com.eleks.academy.whoami.core;

public interface Player {

    String getName();

    String getQuestion();

    String answerQuestion(String question, String character, String nameAsker);

    String getGuess();

    boolean isReadyForGuess();

    String answerGuess(String guess, String character);

    void clearQuestions();

}
