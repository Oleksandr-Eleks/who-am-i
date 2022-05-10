package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.core.impl.Answer;
import com.eleks.academy.whoami.core.impl.PersistentPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class WaitingForPlayers extends AbstractGameState {

	private final Lock lock = new ReentrantLock();

	private final Integer maxPlayers;
	private final Map<String, Player> players;

	public WaitingForPlayers(Integer maxPlayers) {
		super(null, maxPlayers);

		this.maxPlayers = maxPlayers;
		this.players = new HashMap<>(this.maxPlayers);
	}

	@Override
	public GameState next() {
		return new SuggestingCharacters(this.players);
	}

	@Override
	public GameState makeTurn(Answer answer) {
		this.lock.lock();

		try {
			Optional.of(this.players)
					.filter(map -> map.size() < this.maxPlayers)
					.filter(map -> !map.containsKey(answer.getPlayer()))
					.ifPresentOrElse(
							map -> map.put(answer.getPlayer(), new PersistentPlayer(answer.getPlayer())),
							() -> {
								throw new GameException("Cannot enroll to the game");
							}
					);

			return Optional.of(this)
					.filter(WaitingForPlayers::finished)
					.map(WaitingForPlayers::next)
					.orElse(this);
		} finally {
			this.lock.unlock();
		}
	}

	@Override
	public boolean hasPlayer(String player) {
		return this.players.containsKey(player);
	}

	@Override
	public Integer getPlayersInGame() {
		return this.players.size();
	}

	private Boolean finished() {
		return this.players.size() >= this.maxPlayers;
	}

}
