package com.eleks.academy.whoami.core.impl;

import java.util.List;

import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;

public class TurnImpl implements Turn {
	
	private final List<Player> players;
	private int currentPlayerIndex = 0;
	
	public TurnImpl(List<Player> players) {
		this.players = players;
	}
	
	@Override
	public Player getGuesser() {
		return this.players.get(currentPlayerIndex);
	}

	@Override
	public List<Player> getOtherPlayers() {
		return this.players.stream()
				.filter(player -> !player.getName().equals(this.getGuesser().getName()))
				.toList();
	}
	
	@Override
	public void changeTurn() {
		this.currentPlayerIndex = this.currentPlayerIndex + 1 >= this.players.size() ? 0 : this.currentPlayerIndex + 1; 
	}
	
	

}
