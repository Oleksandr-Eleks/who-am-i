package com.eleks.academy.whoami.core.state;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.core.impl.Answer;
import com.eleks.academy.whoami.core.impl.PersistentPlayer;

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
		return new SuggestingCharacters(players);
	}

	@Override
	public GameState makeTurn(Answer answer) {
		Map<String, SynchronousPlayer> nextPlayers = new HashMap<>(players);

		if (nextPlayers.containsKey(answer.getPlayer())) {
			throw new GameException("Cannot enroll to the game");
		} else {
			nextPlayers.put(answer.getPlayer(), new PersistentPlayer(answer.getPlayer()));

		}
		if (players.size() == getMaxPlayers()) {
			return new SuggestingCharacters(players);
		} else {
			return new WaitingForPlayers(getMaxPlayers(), nextPlayers);
		}

	@Override
	public Optional<SynchronousPlayer> findPlayer(String player) {
		return Optional.ofNullable(players.get(player));
	}

}
