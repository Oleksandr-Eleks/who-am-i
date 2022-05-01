package com.eleks.academy.whoami.core.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.eleks.academy.whoami.core.Player;

class RandomGameTest {
	
	private static final int DURATION = 5;
	private static final TimeUnit UNIT = TimeUnit.SECONDS;
	private RandomGame game = new RandomGame(List.of());

	@Test
	void declineAddPlayerCharacterWhenInvalidCharacter() throws InterruptedException, ExecutionException {
		Player player1 = new TestPlayer("BotA", "");
		Player player2 = new TestPlayer("BotB", "12");
		Player player3 = new TestPlayer("BotC", "  Car car");
		Player player4 = new TestPlayer("BotD", "C@r$");
		Player player5 = new TestPlayer("BotD", null);

		game.addPlayerCharacter(player1);
		game.addPlayerCharacter(player2);
		game.addPlayerCharacter(player3);
		game.addPlayerCharacter(player4);
		game.addPlayerCharacter(player5);

		assertAll(new Executable[] {
				() -> assertFalse(game.isConcretePlayerAdded(player1)),
				() -> assertFalse(game.isConcretePlayerAdded(player2)),
				() -> assertFalse(game.isConcretePlayerAdded(player3)),
				() -> assertFalse(game.isConcretePlayerAdded(player4)),
				() -> assertFalse(game.isConcretePlayerAdded(player5))
		});
	}

	@Test
	void acceptAddPlayerCharcterWhenValidCharacter() throws InterruptedException, ExecutionException, TimeoutException {
		Player player1 = new TestPlayer("BotA", "Dino");
		Player player2 = new TestPlayer("BotB", "	Saitama");
		Player player3 = new TestPlayer("BotC", "  Car ");
		Player player4 = new TestPlayer("BotD", "Diamond	");

		game.addPlayerCharacter(player1);
		game.addPlayerCharacter(player2);
		game.addPlayerCharacter(player3);
		game.addPlayerCharacter(player4);
		
		assertTrue(game.isConcreteCharacterAdded(player1.askCharacter().get(DURATION, UNIT).strip()));
		assertTrue(game.isConcreteCharacterAdded(player2.askCharacter().get(DURATION, UNIT).strip()));
		assertTrue(game.isConcreteCharacterAdded(player3.askCharacter().get(DURATION, UNIT).strip()));
		assertTrue(game.isConcreteCharacterAdded(player4.askCharacter().get(DURATION, UNIT).strip()));
	}
	
private final class TestPlayer implements Player {
		
		private final String name;
		private final String character;
		private List<String> characters = new ArrayList<>();
		private List<String> guesses = List.of("Batman", "Superman", "Robin", "Tanos");
		private List<String> questions = List.of("i human?", "i character?", "i male?",
				"Am i a female?");
		
		public TestPlayer(String name, String character) {
			this.name = name;
			this.character = character;
			this.characters.add(character);
		}

		@Override
		public Future<String> askName() {
			return CompletableFuture.completedFuture(name);
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public Future<String> askCharacter() {
			return CompletableFuture.completedFuture(character);
		}

		@Override
		public Future<String> askQuestion() {
			if (questions.isEmpty()) {
				return null;
			}
			String question = questions.remove(0);
			System.out.println(name + " asks: " + question);
			throw new UnsupportedOperationException();
		}

		@Override
		public Future<String> answerQuestion(String playerName, String question, String character) {
			boolean answer = Math.random() < 0.5;
			System.out.println(name + " answers:\n---> " + answer);
			throw new UnsupportedOperationException();
		}

		@Override
		public Future<String> isReadyForGuess() {
			String result = questions.isEmpty() ? "yes" : "no";
			return CompletableFuture.completedFuture(result);
		}

		@Override
		public Future<String> askGuess() {
			int randomPos = (int) (Math.random() * guesses.size());
			String guess = guesses.remove(randomPos);
			System.out.println(name + " guesses: Am I " + guess);
			throw new UnsupportedOperationException();
		}

		@Override
		public Future<String> answerGuess(String playerName, String guess, String character) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void close() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getQuestion() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getGuess() {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
