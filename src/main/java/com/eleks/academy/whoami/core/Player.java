package com.eleks.academy.whoami.core;

public interface Player {

	String getName();
	
	String getQuestion();

	String getGuess();

	default void setAssumptionOrClarification(int numberAnswer){}

	int getAssumptionOrClarification();

	default void removeCharacters(boolean yesOrNot){}

	String answerQuestion(String question, String character);

	default boolean isReadyForGuess(){return true;}

	String answerGuess(String guess, String character);
	
}
