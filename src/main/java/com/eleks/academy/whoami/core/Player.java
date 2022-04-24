package com.eleks.academy.whoami.core;

public interface Player {

	String getName();

	String getQuestion();

	boolean answerQuestion(String question, String character);

	boolean isReadyForGuess();

	String getGuess();

	boolean answerGuess(String guess, String character);
	
}
