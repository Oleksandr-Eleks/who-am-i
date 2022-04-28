package com.eleks.academy.whoami.core;

import java.net.Socket;

public interface Player {

	String getName();

<<<<<<< Updated upstream
	String getQuestion();
	
	Socket getPlayerSocket();
	
	String getGuess();

	String answerQuestion(String question, String character);

	boolean isReadyForGuess();

	String answerGuess(String guess, String character);

=======
	Future<String> getQuestion();
	
	Future<String> answerQuestion(String question, String character);
	
	Future<String> getGuess();
	
	// TODO: return Future<String>
	boolean isReadyForGuess();

	Future<String> answerGuess(String guess, String character);
	
>>>>>>> Stashed changes
}
