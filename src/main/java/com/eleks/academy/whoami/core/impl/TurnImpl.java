package com.eleks.academy.whoami.core.impl;

import java.util.List;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;

public class TurnImpl implements Turn {

	private List<Player> players;
	private int currentPlayer = 0;

	public TurnImpl(List<Player> players) {
		this.players = players;
	}

	@Override
	public Player getGuesser() {
		return players.get(currentPlayer);
	}

	@Override
	public List<Player> getOtherPlayers() {
		return players.stream().filter(player -> !player.getName().equals(getGuesser().getName())).toList();
	}

	@Override
	public void changeTurn() {
		currentPlayer = currentPlayer + 1 >= players.size() ? 0 : currentPlayer + 1;
	}

}
