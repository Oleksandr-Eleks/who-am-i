package com.eleks.academy.whoami.core;

import com.eleks.academy.whoami.core.impl.Answer;

import java.util.Optional;

public interface SynchronousGame {

	Optional<SynchronousPlayer> findPlayer(String player);

	String getId();

	String getPlayersInGame();

	String getStatus();

	boolean isAvailable();

	void makeTurn(Answer answer);

	String getTurn();
}
