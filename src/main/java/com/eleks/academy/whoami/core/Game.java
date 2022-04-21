package com.eleks.academy.whoami.core;

import java.util.List;

public interface Game {

	void addPlayer(Player player);

	boolean makeTurn();

	void assignCharacters();

	boolean isFinished();

	void changeTurn();

	void initGame();

	List<Player> getPlayers();

}
