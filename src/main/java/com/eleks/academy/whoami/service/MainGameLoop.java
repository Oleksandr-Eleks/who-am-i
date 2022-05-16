package com.eleks.academy.whoami.service;

import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Create for future merging RandomGame and SynchronousGame logic
 * Implementation MainGameLoop will be bases on GameState`s
 */
public class MainGameLoop {

	private CompletableFuture<List<SynchronousPlayer>> syncPlayers = new CompletableFuture<>();
	private CompletableFuture<List<CharacterSuggestion>> charactersList = new CompletableFuture<>();

	public MainGameLoop(CompletableFuture<List<SynchronousPlayer>> syncPlayers,
						CompletableFuture<List<CharacterSuggestion>> charactersList) {
		this.syncPlayers = syncPlayers;
		this.charactersList = charactersList;
	}

}
