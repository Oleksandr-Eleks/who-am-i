package com.eleks.academy.whoami.core;

// TODO: Change default methods to abstract, drop the old version ones
public interface Game {

	boolean makeTurn();

	boolean isFinished();

	void changeTurn();

	void initGame();

	void play();

}
