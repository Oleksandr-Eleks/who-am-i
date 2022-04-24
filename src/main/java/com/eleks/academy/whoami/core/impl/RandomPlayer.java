package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.List;
import com.eleks.academy.whoami.core.Player;

public class RandomPlayer implements Player {

	private String name;
	private List<String> questions;
	private List<String> guesses;

	public RandomPlayer(String name, List<String> questions, List<String> guesses) {
		this.name = name;
		this.questions = new ArrayList<String>(questions);
		this.guesses = new ArrayList<String>(guesses);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getQuestion() {
		if (questions.isEmpty()) {
			return "";
		}
		String question = questions.remove(0);
		System.out.println(name + " asks: " + question);
		return question;
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

}
