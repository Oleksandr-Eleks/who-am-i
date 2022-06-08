package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class RandomPlayer implements Player {

	private final String name;
	private final Collection<String> characterPool;
	private final List<String> availableQuestions;
	private final List<String> availableGuesses;
	
	public RandomPlayer(String name, Collection<String> characterPool, List<String> availableQuestions, List<String> availableGuesses) {
		this.name = name;
		this.characterPool = Objects.requireNonNull(characterPool);
		this.availableQuestions = new ArrayList<>(availableQuestions);
		this.availableGuesses = new ArrayList<>(availableGuesses);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Future<String> getQuestion() {
		String question = availableQuestions.remove(0);
		System.out.println("Player: " + name + ". Asks: " + question);
		return CompletableFuture.completedFuture(question);
	}

	@Override
	public Future<String> answerQuestion(String question, String character) {
		String answer = Math.random() < 0.5 ? "Yes" : "No";
		System.out.println("Player: " + name + ". Answers: " + answer);
		return CompletableFuture.completedFuture(answer);
	}


	@Override
	public Future<String> answerGuess(String guess, String character) {
		String answer = Math.random() < 0.5 ? "Yes" : "No";
		System.out.println("Player: " + name + ". Answers: " + answer);
		return CompletableFuture.completedFuture(answer);
	}

	@Override
	public Future<String> getGuess() {
		int randomPos = (int) (Math.random() * this.availableGuesses.size());
		String guess = this.availableGuesses.remove(randomPos);
		System.out.println("Player: " + name + ". Guesses: Am I " + guess);
		return CompletableFuture.completedFuture(guess);
	}

	@Override
	public Future<Boolean> isReadyForGuess() {
		return CompletableFuture.completedFuture(availableQuestions.isEmpty());
	}

	@Override
	public Future<String> suggestCharacter() {
		// TODO: remove a suggestion from the collection
		return CompletableFuture.completedFuture(characterPool.iterator().next());
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	
	
}
