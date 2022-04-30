package com.eleks.academy.whoami.core;

import java.util.concurrent.Future;

public interface Player {

	Future<String> askName();
	
	String getName();
	
	Future<String> aksCharacter();

	Future<String> getQuestion();

	Future<String> answerQuestion(String question, String playerName, String character);

	Future<String> isReadyForGuess();

	Future<String> getGuess();

	Future<String> answerGuess(String guess, String playerName,  String character);

	void close();

	
}
