package com.eleks.academy.whoami.core;

import java.util.concurrent.Future;

public interface Player {

	Future<String> getName();
	
	Future<String> suggestCharacter();

	// TODO: return Future<String>
	String getQuestion();
	
	// TODO: return Future<String>
	String answerQuestion(String question, String character);
	
	String getGuess();
	
	boolean isReadyForGuess();

	String answerGuess(String guess, String character);
	
}
