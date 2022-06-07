package com.eleks.academy.whoami.core.state;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.exception.GameException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

// TODO: Implement makeTurn(...) and next() methods, pass a turn to next player
public final class ProcessingQuestion extends AbstractGameState {

	private final SynchronousPlayer currentPlayer; //той шо питає
	private final Map<String, SynchronousPlayer> players; //всі інші
	private final Queue<SynchronousPlayer> orderedPlayers;

	public ProcessingQuestion(Map<String, SynchronousPlayer> players) {
		super(players.size(), players.size());

		this.players = players;

		Function<SynchronousPlayer, Integer> randomAuthorOrderComparator = value ->
				Double.valueOf(Math.random() * 1000).intValue();

		this.orderedPlayers =
				this.players.values()
						.stream()
						.sorted(Comparator.comparing(randomAuthorOrderComparator))
						.collect(Collectors.toCollection(LinkedList::new));

		this.currentPlayer = this.orderedPlayers.poll();
	}

	private ProcessingQuestion(Map<String, SynchronousPlayer> players, Queue<SynchronousPlayer> orderedPlayers) {
		super(players.size(), players.size());

		this.players = players;
		this.orderedPlayers = orderedPlayers;

		this.currentPlayer = this.orderedPlayers.poll();
	}

	@Override
	public GameState next() {
		//TODO: Check if player won
		this.orderedPlayers.add(this.currentPlayer);

		return new ProcessingQuestion(this.players, this.orderedPlayers);
	}

	@Override
	public Optional<SynchronousPlayer> findPlayer(String player) {
		return Optional.ofNullable(this.players.get(player));
	}

	@Override
	public String getCurrentTurn() {
		return this.currentPlayer.getName();
	}

}
