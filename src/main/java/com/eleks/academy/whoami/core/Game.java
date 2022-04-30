package com.eleks.academy.whoami.core;

public interface Game {
	
	boolean makeTurn();
	
	boolean isFinished();

	void changeTurn();

	void initGame();
	
	void play();

}
