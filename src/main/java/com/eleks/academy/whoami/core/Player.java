package com.eleks.academy.whoami.core;

public interface Player {

	String getName();

	String getQuestion();

	String answerQuestion(String question, String character);

	String getGuess();

	String answerGuess(String guess, String character);

	void setCharacter(String character);

	String getCharacter();

	int getAssumptionOrClarification();

	default void removeCharactersWithQuestions(boolean win) {}

	default void removeCharactersWithCharacter() {}

}
