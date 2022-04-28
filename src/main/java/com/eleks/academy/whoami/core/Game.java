package com.eleks.academy.whoami.core;

public interface Game {
	
	boolean makeTurn();
	
	void assignCharacters();
	
	boolean isFinished();

	void changeTurn();

	void initGame();

}
