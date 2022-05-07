package com.eleks.academy.whoami.core;

public interface Player {
	Boolean IsPlayerConnected();

	String getName();
	
	String getQuestion();
	
	String answerQuestion(String question, String character, String askingPlayerName);
	
	String getGuess();
	
	boolean isReadyForGuess();

	String answerGuess(String guess, String character, String askingPlayerName);
	
}
