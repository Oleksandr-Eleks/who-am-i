package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.core.impl.Answer;

import java.util.Optional;

public sealed interface GameState permits AbstractGameState {

	static GameState start(String player, int maxPlayers) {
		return new WaitingForPlayers(maxPlayers)
				.makeTurn(new Answer(player));
	}

	GameState next();

	GameState makeTurn(Answer answer) throws GameException;

	Optional<SynchronousPlayer> findPlayer(String player);

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
	int getPlayersInGame();

	/**
	 * Used for presentation purposes only
	 *
	 * @return the maximum allowed count of the players
	 */
	int getMaxPlayers();

}
