package com.eleks.academy.whoami.core.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract sealed class AbstractGameState implements GameState
		permits SuggestingCharacters, WaitingForPlayers, ProcessingQuestion {

	private final int playersInGame;
	private final int maxPlayers;

	// TODO: Implement for each state
	@Override
	public String getStatus() {
		return this.getClass().getName();
	}

	/**
	 * @return {@code null} as default implementation
	 */
	public String getCurrentTurn() {
		return null;
	}

}
