package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class WaitingForPlayers extends AbstractGameState {

	private final Map<String, SynchronousPlayer> players;

	public WaitingForPlayers(int maxPlayers) {
		super(0, maxPlayers);
		this.players = new HashMap<>(maxPlayers);
	}

	private WaitingForPlayers(int maxPlayers, Map<String, SynchronousPlayer> players) {
		super(players.size(), maxPlayers);
		this.players = players;
	}

	@Override
	public GameState next() {
		return new SuggestingCharacters(this.players);
	}

	@Override
	public Optional<SynchronousPlayer> findPlayer(String player) {
		return Optional.ofNullable(this.players.get(player));
	}

	@Override
	public int getPlayersInGame() {
		return this.players.size();
	}

}
