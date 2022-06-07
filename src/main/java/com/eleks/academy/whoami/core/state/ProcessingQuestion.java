package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.core.impl.Answer;

import java.util.Map;
import java.util.Optional;

// TODO: Implement makeTurn(...) and next() methods, pass a turn to next player
public final class ProcessingQuestion extends AbstractGameState {

	private final String currentPlayer;
	private final Map<String, SynchronousPlayer> players;

	public ProcessingQuestion(Map<String, SynchronousPlayer> players) {
		super(players.size(), players.size());

		this.players = players;

		this.currentPlayer = players.keySet()
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
	public Optional<SynchronousPlayer> findPlayer(String player) {
		return Optional.ofNullable(this.players.get(player));
	}

	@Override
	public String getCurrentTurn() {
		return this.currentPlayer;
	}

}
