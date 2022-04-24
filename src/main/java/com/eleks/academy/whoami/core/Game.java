package com.eleks.academy.whoami.core;

import java.util.List;

public interface Game {
	
	void addPlayer(Player player);
	
	boolean makeTurn();
	
	void assignCharacters();
	
	boolean isFinished();

	void changeTurn();

	List<Player> getListOfWinners();

	void initGame();

}
