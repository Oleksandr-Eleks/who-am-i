package com.eleks.academy.whoami.core;

public interface Game {
	
	void init();

	void displayPlayers();
	
	void assignCharacters();
	
	void start();

	boolean isFinished();

	boolean makeTurn();

	void endTurn();

	void addPlayer(Player player);
	
	int countPlayers();
	
	void displayResults();
}
