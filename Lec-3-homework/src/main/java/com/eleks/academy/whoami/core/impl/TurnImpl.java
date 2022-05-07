package com.eleks.academy.whoami.core.impl;

import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;

public class TurnImpl implements Turn {
	
	private Queue<Player> players;
	
	public TurnImpl(Queue<Player> players) {
		this.players = players;
	}
	
	@Override
	public Player getGuesser() {
		return this.players.peek();
	}

	@Override
	public List<Player> getOtherPlayers() {
		return this.players.stream()
				.filter(player -> !player.getName().equals(this.getGuesser().getName()))
				.collect(Collectors.toList());
	}
	
	@Override
	public void changeTurn() {
		var currentPlayer = this.players.poll();
		this.players.add(currentPlayer);
	}	

}
