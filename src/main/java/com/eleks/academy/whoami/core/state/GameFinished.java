package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.impl.Answer;
import com.eleks.academy.whoami.core.impl.PersistentPlayer;
import com.eleks.academy.whoami.model.response.PlayerWithState;

import java.util.ArrayList;
import java.util.List;
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

	@Override
	public List<PlayerWithState> getPlayers() {
		return null;
	}

	@Override
	public List<PlayerWithState> getPlayersWithState() {
		return null;
	}

	@Override
	public GameState makeTurn(Answer player) {
		return null;
	}

//	@Override
	public SynchronousPlayer add(SynchronousPlayer player) {
		return player;
	}
}
