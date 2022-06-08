package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.SynchronousPlayer;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class PersistentPlayer implements Player, SynchronousPlayer {

	private final String name;
	private final CompletableFuture<String> character = new CompletableFuture<>();


	private Queue<String> questionQueue;
	private volatile CompletableFuture<String> question;
	private volatile CompletableFuture<String> currentAnswer;
	private volatile CompletableFuture<Boolean> readyForAnswerFuture;

	public PersistentPlayer(String name) {
		this.name = Objects.requireNonNull(name);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getCharacter() {
		return null;
	}

	@Override
	public Future<String> suggestCharacter() {
		return character;
	}

	@Override
	public Future<String> getQuestion() {
		return null;
	}

	@Override
	public Future<String> answerQuestion(String question, String character) {
		return null;
	}

	@Override
	public Future<String> getGuess() {
		return null;
	}

	@Override
	public Future<Boolean> isReadyForGuess() {
		return null;
	}

	@Override
	public Future<String> answerGuess(String guess, String character) {
		return null;
	}

	@Override
	public void close() {

	}

	@Override
	public void setCharacter(String character) {
		if (!this.character.complete(character)) {
			throw new IllegalStateException("Character has already been suggested!");
		}
	}

}
