package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.core.impl.Answer;
import com.eleks.academy.whoami.core.impl.PersistentPlayer;
import com.eleks.academy.whoami.model.response.PlayerState;
import com.eleks.academy.whoami.model.response.PlayerWithState;

import java.util.*;
import java.util.stream.Collectors;

public final class WaitingForPlayers extends AbstractGameState {

	private final Map<String, PlayerWithState> players;
	private int maxPlayers;
	public WaitingForPlayers(int maxPlayers) {
		super(0, maxPlayers);
		this.maxPlayers = maxPlayers;
		this.players = new HashMap<>(maxPlayers);
	}

	private WaitingForPlayers(int maxPlayers, Map<String, PlayerWithState> players) {
		super(players.size(), maxPlayers);
		this.players = players;
	}

	@Override
	public GameState next() {

		return new SuggestingCharacters (this.players);
	}

	@Override
	public Optional<SynchronousPlayer> findPlayer(String player) {
		return Optional.ofNullable(this.players.get(player).getPlayer());
	}

	@Override
	public int getPlayersInGame() {
		return this.players.size();
	}

	@Override
	public GameState makeTurn(Answer answer) {
		Map<String, PlayerWithState> nextPlayers = new HashMap<>(this.players);
		if (nextPlayers.containsKey(answer.getPlayer()) || maxPlayers == this.getPlayersInGame()) {
		} else {
			PersistentPlayer persistentPlayer = new PersistentPlayer(answer.getPlayer());
			nextPlayers.put(answer.getPlayer(),
					new PlayerWithState(persistentPlayer, null, PlayerState.NOT_READY));
		}
		if (nextPlayers.size() == getMaxPlayers()) {
			return new SuggestingCharacters(nextPlayers);
		} else {
			return new WaitingForPlayers(getMaxPlayers(), nextPlayers);
		}
	}

	@Override
	public GameState leaveGame(String player) {
		Map<String, PlayerWithState> players = new HashMap<>(this.players);
		if (findPlayer(player).isPresent()) {
			players.remove(player);
		}
		if (players.size() == getMaxPlayers()) {
			return new SuggestingCharacters(players);
		} else {
			return new WaitingForPlayers(getMaxPlayers(), players);
		}
	}

	@Override
	public List<PlayerWithState> getPlayersWithState() {
		return players.values().stream().toList();
	}

	@Override
	public List<PlayerWithState> getPlayers() {
		return null;
	}

}
