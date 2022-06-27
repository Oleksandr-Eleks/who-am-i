package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.SynchronousGame;
import com.eleks.academy.whoami.core.SynchronousPlayer;
import com.eleks.academy.whoami.core.state.GameState;
import com.eleks.academy.whoami.core.state.WaitingForPlayers;
import com.eleks.academy.whoami.model.response.PlayerState;
import com.eleks.academy.whoami.model.response.PlayerWithState;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class PersistentGame implements Game, SynchronousGame {

	private final Lock turnLock = new ReentrantLock();
	private final String id;
	private int maxPlayers;
	private List<PlayerWithState> playerWithStateList = new ArrayList<>();
	private final Queue<GameState> turns = new LinkedBlockingQueue<>();
	private final Queue<PlayerState> playerStates = new LinkedBlockingQueue<>();

	/**
	 * Creates a new game (game room) and makes a first enrolment turn by a current player
	 * so that he won't have to enroll to the game he created
	 *
	 * @param hostPlayer player to initiate a new game
	 */
	public PersistentGame(String hostPlayer, Integer maxPlayers) {
		this.id = String.format("%d-%d",
				Instant.now().toEpochMilli(),
				Double.valueOf(Math.random() * 999).intValue());
		this.turns.add(new WaitingForPlayers(maxPlayers));
	}

	public PersistentGame(Integer maxPlayers) {
		this.id = String.format("%d-%d",
				Instant.now().toEpochMilli(),
				Double.valueOf(Math.random() * 999).intValue());

		this.maxPlayers = maxPlayers;
		this.turns.add(new WaitingForPlayers(this.maxPlayers));
	}

	@Override
	public Optional<SynchronousPlayer> findPlayer(String player) {
		return this.applyIfPresent(this.turns.peek(), gameState -> gameState.findPlayer(player));
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public SynchronousPlayer enrollToGame(String player) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getTurn() {
		return this.applyIfPresent(this.turns.peek(), GameState::getCurrentTurn);
	}

	@Override
	public Optional<GameState> getCurrentTurnInfo() {
		return Optional.ofNullable(this.turns.peek());
	}

	@Override
	public void askQuestion(String player, String message) {

	}

	@Override
	public void answerQuestion(String player, Answer answer) {
		// TODO: Implement method
	}

	@Override
	public SynchronousGame start() {
		this.turnLock.lock();
		try {
			Optional.ofNullable(this.turns.poll())
					.map(GameState::next)
					.ifPresent(this.turns::add);
		} finally {
			this.turnLock.unlock();
		}
		return this;
	}

	@Override
	public boolean isAvailable() {
		return this.turns.peek() instanceof WaitingForPlayers;
	}

	@Override
	public String getStatus() {
		return this.applyIfPresent(this.turns.peek(), GameState::getStatus);
	}

	@Override
	public List<PlayerWithState> getPlayersInGame() {
		return this.applyIfPresent(this.turns.peek(), GameState::getPlayersWithState);
	}

	@Override
	public boolean isFinished() {
		return this.turns.isEmpty();
	}


	@Override
	public void makeTurn(Answer answer) {
		this.turnLock.lock();

		try {
			Optional.ofNullable(this.turns.poll())
					.map(gameState -> gameState.makeTurn(answer))
					.ifPresent(this.turns::add);
		} finally {
			this.turnLock.unlock();
		}
	}

	@Override
	public void changeTurn() {

	}

	@Override
	public void initGame() {

	}

	@Override
	public void play() {

	}

	@Override
	public void removeFromGame(String gameId, String player) {
		Optional<SynchronousPlayer> synchronousPlayer = findPlayer(player);
		if(synchronousPlayer!=null){
			this.turnLock.lock();

			try {
				Optional.ofNullable(this.turns.poll())
						.map(gameState -> gameState.leaveGame(player))
						.ifPresent(this.turns::add);
			} finally {
				this.turnLock.unlock();
			}
		}

	}

	private <T, R> R applyIfPresent(T source, Function<T, R> mapper) {
		return this.applyIfPresent(source, mapper, null);
	}

	private <T, R> R applyIfPresent(T source, Function<T, R> mapper, R fallback) {
		return Optional.ofNullable(source)
				.map(mapper)
				.orElse(fallback);
	}
}
