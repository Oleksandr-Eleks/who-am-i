package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Player;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Future;

@RequiredArgsConstructor(staticName = "of")
public class PersistentPlayer implements Player {

	private final String name;

	public String getId() {
		return this.name;
	}

	@Override
	public Future<String> getName() {
		return null;
	}

	@Override
	public Future<String> suggestCharacter() {
		return null;
	}

	@Override
	public String getQuestion() {
		return null;
	}

	@Override
	public String answerQuestion(String question, String character) {
		return null;
	}

	@Override
	public String getGuess() {
		return null;
	}

	@Override
	public boolean isReadyForGuess() {
		return false;
	}

	@Override
	public String answerGuess(String guess, String character) {
		return null;
	}

	@Override
	public void close() {

	}
}
