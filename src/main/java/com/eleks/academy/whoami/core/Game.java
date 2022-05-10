package com.eleks.academy.whoami.core;

import com.eleks.academy.whoami.core.impl.Answer;

// TODO: Change default methods to abstract, drop the old version ones
public interface Game {

	default String getId() {
		throw new UnsupportedOperationException();
	}

	default boolean isAvailable() {
		throw new UnsupportedOperationException();
	}

	default boolean hasPlayer(String player) {
		throw new UnsupportedOperationException();
	}

	default String getStatus() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Presentational purpose only
	 *
	 * @return the info of how many players have already joined the game
	 */
	default String getPlayersInGame() {
		throw new UnsupportedOperationException();
	}

	default String getTurn() {
		throw new UnsupportedOperationException();
	}

	default void makeTurn(Answer answer) {
		throw new UnsupportedOperationException();
	}

	boolean makeTurn();

	boolean isFinished();

	void changeTurn();

	void initGame();

	void play();

}
