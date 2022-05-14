package com.eleks.academy.whoami.core;

import java.util.Optional;

import com.eleks.academy.whoami.core.impl.Answer;

public interface SynchronousGame {

	Optional<SynchronousPlayer> findPlayer(String player);

	String getId();

	String getPlayersInGame();

	String getStatus();

	boolean isAvailable();

	void makeTurn(Answer answer);

	String getTurn();
	
}
