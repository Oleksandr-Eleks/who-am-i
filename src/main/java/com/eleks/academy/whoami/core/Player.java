package com.eleks.academy.whoami.core;

import java.util.concurrent.Future;

public interface Player {

	Future<String> getName();

	Future<String> suggestCharacter();

	// TODO: return Future<String>
	String getQuestion();

	// TODO: return Future<String>
	String answerQuestion(String question, String character);

	// TODO: return Future<String>
	String getGuess();

	// TODO: return Future<String>
	boolean isReadyForGuess();

	// TODO: return Future<String>
	String answerGuess(String guess, String character);

	void close();

}
