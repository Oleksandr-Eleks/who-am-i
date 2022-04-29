package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import com.eleks.academy.whoami.core.Player;

public class RandomPlayer implements Player {

	private String name;
	private List<String> characters = new ArrayList<>();
	private List<String> questions;
	private List<String> guesses;

	public RandomPlayer(String name, String character, List<String> questions, List<String> guesses) {
		this.name = name;
		this.characters.add(character);
		this.questions = new ArrayList<>(questions);
		this.guesses = new ArrayList<>(guesses);
	}

	@Override
	public Future<String> getName() {
		return CompletableFuture.completedFuture(name);
	}

	@Override
	public Future<String> getCharacter() {
		String character = characters.get(0);
//		characters.remove(0);
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
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
