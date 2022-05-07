package com.eleks.academy.whoami.core;

public interface Game {

	void addPlayer(Player player);

	void assignCharacters();

	boolean makeTurn();

	boolean isFinished();

	void changeTurn();

	void initGame();

	boolean isPlayerPresent(Player player);

	void play();

}

