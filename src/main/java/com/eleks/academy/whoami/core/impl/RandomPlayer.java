package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.List;

import com.eleks.academy.whoami.core.Player;

public record RandomPlayer(String name, List<String> availableQuestions,
						   List<String> availableGuesses) implements Player {

	public RandomPlayer(String name, List<String> availableQuestions, List<String> availableGuesses) {
		this.name = name;
		this.availableQuestions = new ArrayList<>(availableQuestions);
		this.availableGuesses = new ArrayList<>(availableGuesses);
	}


	@Override
	public String getQuestion() {
		String question = availableQuestions.remove(0);
		System.out.println(name + " asks: " + question);
		return question;
	}

	@Override
	public String answerQuestion(String question, String character) {
		String answer = Math.random() < 0.5 ? "Yes" : "No";
		System.out.println(name + " answer: " + answer);
		return answer;
	}


	@Override
	public String answerGuess(String guess, String character) {
		String answer = Math.random() < 0.5 ? "Yes" : "No";
		System.out.println(name + " answer: " + answer);
		return answer;
	}

	@Override
	public String getGuess() {
		int randomPos = (int) (Math.random() * this.availableGuesses.size());
		String guess = this.availableGuesses.remove(randomPos);
		System.out.println(name + " Guesses: Am I " + guess);
		return guess;
	}

	@Override
	public boolean isReadyForGuess() {
		return availableQuestions.isEmpty();
	}


}
