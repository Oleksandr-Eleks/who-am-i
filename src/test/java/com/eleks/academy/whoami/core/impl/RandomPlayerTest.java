package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;

class RandomPlayerTest {

	Game game = new RandomGame(List.of());
	Player player = new TestPlayer("botA", "demono");
	
private final class TestPlayer implements Player {
		
		private final String name;
		public List<String> characters = new ArrayList<>();
		private List<String> guesses = List.of("Batman", "Superman", "Robin", "Tanos");
		private List<String> questions = List.of("i human?", "i character?", "i male?",
				"Am i a female?");
		
		public TestPlayer(String name, String character) {
			this.name = name;
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
			return CompletableFuture.completedFuture(characters.remove(0));
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
