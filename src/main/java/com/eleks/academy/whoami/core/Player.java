package com.eleks.academy.whoami.core;

import java.util.concurrent.Future;

public interface Player {

	Future<String> getName();
	
	Future<String> suggestCharacter();

	String getQuestion();
	
	String answerQuestion(String question, String character);
	
	String getGuess();
	
	boolean isReadyForGuess();

	String answerGuess(String guess, String character);
	
}
