package com.eleks.academy.whoami.core;

import java.util.concurrent.Future;

public interface Player {

	Future<String> getName();
	
	Future<String> suggestCharacter();

	// TODO: return Future<String>
	Future<String> getQuestion();
	
	// TODO: return Future<String>
	Future<String> answerQuestion(String question, String character);
	
	// TODO: return Future<String>
	Future<String> getGuess();
	
	// TODO: return Future<String>
	Future<Boolean> isReadyForGuess();

	// TODO: return Future<String>
	Future<String> answerGuess(String guess, String character);
	
	void close();
	
}
