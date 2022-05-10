package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.core.impl.Answer;

import java.util.Map;

// TODO: Implement makeTurn(...) and next() methods, pass a turn to next player
public final class ProcessingQuestion extends AbstractGameState {

	private final Map<String, String> playerCharacterMap;
	private final String currentPlayer;

	public ProcessingQuestion(Map<String, String> playerCharacterMap) {
		super(playerCharacterMap.size(), playerCharacterMap.size());

		this.playerCharacterMap = playerCharacterMap;

		this.currentPlayer = playerCharacterMap.keySet()
				.stream()
				.findAny()
				.orElse(null);
	}

	@Override
	public GameState next() {
		throw new GameException("Not implemented");
	}

	@Override
	public GameState makeTurn(Answer answer) throws GameException {
		return this;
	}

	@Override
	public boolean hasPlayer(String player) {
		return this.playerCharacterMap.containsKey(player);
	}

	@Override
	public String getCurrentTurn() {
		return this.currentPlayer;
	}

}
