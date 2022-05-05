package com.eleks.academy.whoami.core;

import com.eleks.academy.whoami.core.impl.GameStatus;

// TODO: Change default methods to abstract, drop the old version ones
public interface Game {

	default String getId() {
		throw new UnsupportedOperationException();
	}

	default boolean isReadyToStart() {
		throw new UnsupportedOperationException();
	}

	default boolean hasPlayer(String player) {
		throw new UnsupportedOperationException();
	}

	default void addPlayer(String player) {
		throw new UnsupportedOperationException();
	}

	default boolean isAvailable() {
		throw new UnsupportedOperationException();
	}

	default String getPlayersInGame() {
		throw new UnsupportedOperationException();
	}

	default String getTurn() {
		throw new UnsupportedOperationException();
	}

	/**
	 * For usage with {@link Game#isReadyToStart()} only
	 */
	default void startGame() {
		throw new UnsupportedOperationException();
	}

	default void suggestCharacter(String player, String character) {
		throw new UnsupportedOperationException();
	}

	default GameStatus getStatus() {
		throw new UnsupportedOperationException();
	}

	boolean makeTurn();

	boolean isFinished();

	void changeTurn();

	void initGame();

	void play();

}
