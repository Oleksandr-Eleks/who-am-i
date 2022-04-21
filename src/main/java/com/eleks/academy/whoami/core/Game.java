package com.eleks.academy.whoami.core;

public interface Game {
	
	void addPlayer(Player player);
	
	boolean makeTurn();
	
	void assignCharacters();
	
	boolean isFinished();

	void changeTurn();

	void initGame();

	int playersNum();

}
