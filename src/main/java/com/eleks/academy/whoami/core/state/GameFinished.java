package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;

import java.util.Optional;

public final class GameFinished extends AbstractGameState {

	public GameFinished(int playersInGame, int maxPlayers) {
		super(playersInGame, maxPlayers);
	}

	@Override
	public GameState next() {
		return null;
	}

	@Override
	public Optional<SynchronousPlayer> findPlayer(String player) {
		return Optional.empty();
	}
}
