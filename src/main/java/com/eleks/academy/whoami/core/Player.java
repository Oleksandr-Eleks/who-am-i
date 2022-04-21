package com.eleks.academy.whoami.core;

import java.net.Socket;

public interface Player {

	String getName();

	String getQuestion();
	
	Socket getPlayerSocket();
	
	String getGuess();

	String answerQuestion(String question, String character);

	boolean isReadyForGuess();

	String answerGuess(String guess, String character);

}
