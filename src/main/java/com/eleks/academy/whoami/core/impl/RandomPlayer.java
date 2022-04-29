package com.eleks.academy.whoami.core.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import com.eleks.academy.whoami.core.Player;

public class RandomPlayer implements Player {
	
	private final String name;
	private final String character;
	private List<String> guesses = List.of("Batman", "Superman", "Superwoman", "Robin");
	private List<String> questions = List.of("i human?", "i character?", "i male?",
			"Am i a female?");
	
	public RandomPlayer() {
		this.name = generateName();
		this.character = generateCharacter();
	}

	@Override
	public Future<String> getName() {
		return CompletableFuture.completedFuture(name);
	}

	@Override
	public Future<String> getCharacter() {
		return CompletableFuture.completedFuture(character);
	}
	
	@Override
	public Future<String> getQuestion() {
		if (questions.isEmpty()) {
			return null;
		}
		String question = questions.remove(0);
		System.out.println(name + " asks: " + question);
		return null;
	}

	@Override
	public boolean answerQuestion(String question, String playerName, String character) {
		boolean answer = Math.random() < 0.5;
		System.out.println(name + " answers:\n---> " + answer);
		return answer;
	}

	@Override
	public String getGuess() {
		int randomPos = (int) (Math.random() * guesses.size());
		String guess = guesses.remove(randomPos);
		System.out.println(name + " guesses: Am I " + guess);
		return guess;
	}

	@Override
	public boolean answerGuess(String guess, String playerName, String character) {
		boolean answer = guess.toLowerCase().contains(character.toLowerCase());
		System.out.println(name + " answers:\n---> " + answer);
		return answer;
	}

	@Override
	public boolean isReadyForGuess() {
		return questions.isEmpty();
	}

	@Override
	public void close() {}
	
	private String generateName() {
		int token = ((int) (Math.random() * (65535 - 49152)) + 49152);
		return "Player" + Integer.toString(token);
	}
	
	private String generateCharacter() {
		List<String> characters = List.of("Batman", "Superman", "Superwoman", "Robin");
		return characters.remove(0);
	}
}
