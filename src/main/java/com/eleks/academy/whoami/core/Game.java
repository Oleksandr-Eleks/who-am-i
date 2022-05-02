package com.eleks.academy.whoami.core;

public interface Game {

	void addPlayer(Player player);

	void addCharacter(Player player);
	
	boolean makeTurn();
	
	boolean isFinished();

	void changeTurn();

	void initGame();
	
	void play();

}
