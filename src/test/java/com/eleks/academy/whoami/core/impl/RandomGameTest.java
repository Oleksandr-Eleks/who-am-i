package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomGameTest {

	@Test
	void askAllPlayersForCharacterSuggestions() {
		TestPlayer p1 = new TestPlayer("P1");
		TestPlayer p2 = new TestPlayer("P2");
		Game game = new RandomGame(List.of(p1, p2), List.of("c"));
		game.initGame();
		assertAll(() -> assertTrue(p1.suggested),
				() -> assertTrue(p2.suggested));
	}

	private static final class TestPlayer implements Player {
		private final String name;
		private boolean suggested;

		public TestPlayer(String name) {
			super();
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public Future<String> suggestCharacter() {
			suggested = true;
			return CompletableFuture.completedFuture("char");
		}

		@Override
		public Future<String> getQuestion() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Future<String> answerQuestion(String question, String character) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Future<String> getGuess() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Future<Boolean> isReadyForGuess() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Future<String> answerGuess(String guess, String character) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void close() {
			// TODO Auto-generated method stub
			
		}
		
	}

}
