package com.eleks.academy.whoami.core;

import java.util.concurrent.Future;

public interface Player {

	Future<String> getName();
	
	Future<String> getCharacter();

	Future<String> getQuestion();

	boolean answerQuestion(String question, String playerName, String character);

	boolean isReadyForGuess();

	String getGuess();

	boolean answerGuess(String guess, String playerName,  String character);

	void close();
	
}
