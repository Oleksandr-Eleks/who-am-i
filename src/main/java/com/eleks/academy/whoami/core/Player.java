package com.eleks.academy.whoami.core;

public interface Player {

	String getName();
	
	String getQuestion();
	
	String answerQuestion(String question, Character character);
	
	String getGuess();
	
	boolean isReadyForGuess();


	String answerGuess(String guess, Character character);

    void setCorrectCharacteristics(String question);
}
