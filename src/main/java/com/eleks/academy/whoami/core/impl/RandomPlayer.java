package com.eleks.academy.whoami.core.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import com.eleks.academy.whoami.core.Player;

public class RandomPlayer implements Player {

	private final String name;
	private final String character;
	private List<String> guesses = List.of("Batman", "Superman", "Superwoman", "Robin");
	private List<String> questions = List.of("i human?", "i character?", "i male?", "Am i a female?");

	public RandomPlayer() {
		this.name = generateName();
		this.character = generateCharacter();
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
		return null;
	}

	@Override
	public Future<String> answerQuestion(String playerName, String question, String character) {
		String answer = Math.random() < 0.5 ? "yes" : "no";
		System.out.println(name + " answers:\n---> " + answer);
		return CompletableFuture.completedFuture(answer);
	}

	@Override
	public Future<String> askGuess() {
		int randomPos = (int) (Math.random() * guesses.size());
		String guess = guesses.remove(randomPos);
		System.out.println(name + " guesses: Am I " + guess);
		return CompletableFuture.completedFuture(guess);
	}

	@Override
	public Future<String> answerGuess(String playerName, String guess, String character) {
		String answer = Math.random() < 0.5 ? "yes" : "no";
		System.out.println(name + " answers:\n---> " + answer);
		return CompletableFuture.completedFuture(answer);
	}

	@Override
	public Future<String> isReadyForGuess() {
		String result = questions.isEmpty() ? "yes" : "no";
		return CompletableFuture.completedFuture(result);
	}

	@Override
	public void close() {
	}

	private String generateName() {
		int token = ((int) (Math.random() * (65535 - 49152)) + 49152);
		return "Player" + Integer.toString(token);
	}

	private String generateCharacter() {
		List<String> characters = List.of("Batman", "Superman", "Superwoman", "Robin");
		return characters.remove(0);
	}

	@Override
	public String getQuestion() {
		return null;
	}

	@Override
	public String getGuess() {
		return null;
	}

}
