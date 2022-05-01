package com.eleks.academy.whoami.core;

import java.util.concurrent.Future;

public interface Player {

	Future<String> askName();
	
	String getName();
	
	Future<String> askCharacter();

	Future<String> askQuestion();
	
	String getQuestion();

	Future<String> answerQuestion(String playerName, String question, String character);

	Future<String> isReadyForGuess();

	Future<String> askGuess();
	
	String getGuess();

	Future<String> answerGuess(String playerName, String guess, String character);

	void close();

	
}
