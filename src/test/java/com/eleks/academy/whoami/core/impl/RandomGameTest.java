package com.eleks.academy.whoami.core.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;

class RandomGameTest {

	@Test
	void askAllPlayersForCharacterSuggestions() {
		TestPlayer p1 = new TestPlayer("P1", "c");
		TestPlayer p2 = new TestPlayer("P2", "c");
		Game game = new RandomGame(List.of(p1, p2), List.of());
		game.initGame();
		assertAll(new Executable[] {
				() -> assertTrue(p1.suggested),
				() -> assertTrue(p2.suggested),
		});
	}

	@Test
	void addPlayerFailedObtainSuggestion() {
		TestPlayer p1 = new TestPlayer("P2", "");
		RandomGame game = new RandomGame(List.of(p1), List.of());
		game.addCharacter(p1);
		assertAll(new Executable[] {
				() -> assertFalse(game.isPlayerAdded(p1))
		});

	}

	private static final class TestPlayer implements Player {
		private final String name;
		private boolean suggested;
		private String character;

		public TestPlayer(String name, String character) {
			super();
			this.name = name;
			this.character = character;
		}

		@Override
		public Future<String> getName() {
			return CompletableFuture.completedFuture(name);
		}

		@Override
		public String getNameOnly() {
			return this.name;
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
		}
		
	}

}
