package com.eleks.academy.whoami.core;

public interface Game {
	
	void init();

	void displayPlayers();
	
	void assignCharacters();
	
	boolean isFinished();

	void makeTurn();

	void addPlayer(Player player);
	
	void addPlayerCharacter(Player player);
	
	void displayResults();

}
