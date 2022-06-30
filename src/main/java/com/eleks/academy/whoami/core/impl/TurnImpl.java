package com.eleks.academy.whoami.core.impl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.eleks.academy.whoami.core.Turn;
import com.eleks.academy.whoami.enums.PlayerState;
import com.eleks.academy.whoami.enums.QuestionAnswer;

public class TurnImpl implements Turn {
	
	private List<SynchronousPlayer> players;
	private int currentPlayerIndex = 0;
	private final List<QuestionAnswer> playersAnswers = new ArrayList<>();

	private SynchronousPlayer currentPlayer;
	private Queue<SynchronousPlayer> orderedPlayers;


	public TurnImpl(List<SynchronousPlayer> players) {
		this.players = players;

		Function<SynchronousPlayer, Integer> randomAuthorOrderComparator = value ->
				Double.valueOf(Math.random() * 1000).intValue();
		this.orderedPlayers =
				this.players
						.stream()
						.sorted(Comparator.comparing(randomAuthorOrderComparator))
						.collect(Collectors.toCollection(LinkedList::new));

		this.currentPlayer = this.orderedPlayers.poll();
	}

	public TurnImpl(List<SynchronousPlayer> players, Queue<SynchronousPlayer> orderedPlayers) {
		this.players = players;
		this.orderedPlayers = orderedPlayers;

		currentPlayer.setPlayerState(PlayerState.ASK_QUESTION);

		this.currentPlayer = this.orderedPlayers.poll();
	}

	@Override
	public SynchronousPlayer getGuesser() {
		return this.players.get(currentPlayerIndex);
	}

	@Override
	public List<SynchronousPlayer> getOtherPlayers() {
		return this.players.stream()
				.filter(player -> !player.getName().equals(this.getGuesser().getName()))
				.toList();
	}

	@Override
	public List<QuestionAnswer> getPlayersAnswers() {
		return null;
	}

	@Override
	public Turn changeTurn() {
//		this.currentPlayerIndex = this.currentPlayerIndex + 1 >= this.players.size() ? 0 : this.currentPlayerIndex + 1;
		this.orderedPlayers.add(this.currentPlayer);
		return new TurnImpl(this.players, this.orderedPlayers);

	}
	
	

}
