package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.core.impl.Answer;

public sealed interface GameState permits AbstractGameState {

	GameState next();

	GameState makeTurn(Answer answer) throws GameException;

	/**
	 * Used for presentation purposes only
	 *
	 * @return whether current player belongs to a game
	 */
	boolean hasPlayer(String player);

	/**
	 * Used for presentation purposes only
	 *
	 * @return a player, whose turn is now
	 * or {@code null} if state does not take turns (e.g. {@link SuggestingCharacters})
	 */
	String getCurrentTurn();

	/**
	 * Used for presentation purposes only
	 *
	 * @return the status of the current state to show to players
	 */
	String getStatus();

	/**
	 * Used for presentation purposes only
	 *
	 * @return the count of the players
	 */
	Integer getPlayersInGame();

	/**
	 * Used for presentation purposes only
	 *
	 * @return the maximum allowed count of the players
	 */
	Integer getMaxPlayers();

	static GameState start(String player, Integer maxPlayers) {
		return new WaitingForPlayers(maxPlayers)
				.makeTurn(new Answer(player));
	}

}
