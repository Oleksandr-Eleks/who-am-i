package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Player;

import java.util.concurrent.Future;

public record PersistentPlayer(String name) implements Player {

	@Override
	public Future<String> getName() {
		return null;
	}

	@Override
	public Future<String> suggestCharacter() {
		return null;
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
}
