package com.eleks.academy.whoami.core;

import java.util.concurrent.Future;

public interface Player {

	String getName();

	Future<String> suggestCharacter();

	Future<String> getQuestion();

	Future<String> answerQuestion(String question, String character);

	Future<String> getGuess();

	Future<Boolean> isReadyForGuess();

	Future<String> answerGuess(String guess, String character);

	void close();

}
