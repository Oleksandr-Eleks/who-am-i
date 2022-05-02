package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.eleks.academy.whoami.core.Player;

public class RandomPlayer implements Player, AutoCloseable {

    private String name;
    private final Collection<String> characterPool;
    private List<String> availableQuestions;
    private List<String> availableGuesses;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public RandomPlayer(String name, Collection<String> characterPool, List<String> availableQuestions,
	    List<String> availableGuesses) {
	this.name = name;
	this.characterPool = Objects.requireNonNull(characterPool);
	this.availableQuestions = new ArrayList<>(availableQuestions);
	this.availableGuesses = new ArrayList<>(availableGuesses);
    }

    @Override
    public Future<String> getName() {
	return CompletableFuture.completedFuture(this.name);
    }

    @Override
    public Future<String> getQuestion() {
	return executor.submit(this::askQueston);
    }

    @Override
    public Future<String> answerQuestion(String question, String character) {
	return executor.submit(this::doAnswerQuestion);
    }

    @Override
    public Future<String> answerGuess(String guess, String character) {
	return executor.submit(this::doAnswerGuess);
    }

    @Override
    public Future<String> getGuess() {
	return executor.submit(this::askGuess);
    }

    @Override
    public Future<Boolean> isReadyForGuess() {
	return CompletableFuture.completedFuture(availableQuestions.isEmpty());
    }

    @Override
    public Future<String> suggestCharacter() {
	return executor.submit(this::doSuggestCharacter);
    }

    @Override
    public void close() {
	executor.shutdown();
	try {
	    executor.awaitTermination(5, TimeUnit.SECONDS);
	} catch (InterruptedException e) {
	    Thread.currentThread().interrupt();
	}
    }

    private String doSuggestCharacter() {
	characterPool.iterator().remove();
	return characterPool.iterator().next();
    }

    private String askGuess() {
	int randomPos = (int) (Math.random() * this.availableGuesses.size());
	String guess = this.availableGuesses.remove(randomPos);
	System.out.println("Player: " + name + ". Guesses: Am I " + guess);
	return guess;
    }

    private String askQueston() {
	String question = availableQuestions.remove(0);
	System.out.println("Player: " + name + ". Asks: " + question);
	return question;
    }

    private String doAnswerQuestion() {
	String answer = Math.random() < 0.5 ? "Yes" : "No";
	System.out.println("Player: " + name + ". Answers: " + answer);
	return answer;
    }

    private String doAnswerGuess() {
	String answer = Math.random() < 0.5 ? "Yes" : "No";
	System.out.println("Player: " + name + ". Answers: " + answer);
	return answer;
    }
}
