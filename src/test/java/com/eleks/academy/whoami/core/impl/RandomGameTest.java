package com.eleks.academy.whoami.core.impl;

import static org.junit.jupiter.api.Assertions.*;

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
		TestPlayer p1 = new TestPlayer("P1");
		TestPlayer p2 = new TestPlayer("P2");
		Game game = new RandomGame(List.of(p1, p2), List.of("c"));
		game.initGame();
		assertAll(new Executable[] {
				() -> assertTrue(p1.suggested),
				() -> assertTrue(p2.suggested),
		});
	}

	private static final class TestPlayer implements Player {
		private final String name;
		private boolean suggested;

		public TestPlayer(String name) {
			super();
			this.name = name;
		}

		@Override
		public Future<String> getName() {
			return CompletableFuture.completedFuture(name);
		}

		@Override
		public Future<String> suggestCharacter() {
			suggested = true;
			return CompletableFuture.completedFuture("char");
		}

		@Override
		public Future<String> getQuestion() {
		    return CompletableFuture.completedFuture("char");
		}

		@Override
		public Future<String> answerQuestion(String question, String character) {
		    return CompletableFuture.completedFuture("char");
		}

		@Override
		public Future<String> getGuess() {
		    return CompletableFuture.completedFuture("char");
		}

		@Override
		public Future<Boolean> isReadyForGuess() {
		    return CompletableFuture.completedFuture(true);
		}

		@Override
		public Future<String> answerGuess(String guess, String character) {
		    return CompletableFuture.completedFuture("char");
		}

		@Override
		public void close() {
			// TODO Auto-generated method stub
			
		}
		
	}

}
