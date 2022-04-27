package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.eleks.academy.whoami.core.Character;
import com.eleks.academy.whoami.core.Player;

public class RandomPlayer implements Player {

	private final String name;
	private Map <String, List <String>> availableQuestions;
	private Iterator <String> keysIterator;
	private List<String> values =  new ArrayList<>();
	private List<String> correctAnswers = new ArrayList<>();
	private Map<String, List<String>> availableGuesses;

	public RandomPlayer(String name, Map <String, List <String>> availableQuestions, Map<String, List<String>> availableGuesses) {
		this.name = name;
		this.availableQuestions = new HashMap<>(availableQuestions);
		this.availableGuesses = new HashMap<>(availableGuesses);
		keysIterator = availableQuestions.keySet().iterator();
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getQuestion() {
		if(this.values.isEmpty()) {
			if (this.keysIterator.hasNext()) {
				this.values = new ArrayList<>(this.availableQuestions.remove(keysIterator.next()));
			}
		}
		String question = values.remove(0);
		System.out.println("Player: " + this.name + ". Asks: Am I a " + question + "?");
		return question;
	}

	@Override
	public String answerQuestion(String question, Character character){
		String answer = "No";
		for (var feature : character.getCharacteristics()){
			if(question.equalsIgnoreCase(feature)){
				answer = "Yes";
				break;
			}
		}
		System.out.println("Player: " + this.name + ". Answers: " + answer);
		return answer;
	}

	@Override
	public String answerGuess(String guess, Character character) {
		String answer = "No";
		if (character.getName().equalsIgnoreCase(guess)){
			answer = "Yes";
		}
		System.out.println("Player: " + this.name + ". Answers: " + answer);
		return answer;
	}

	@Override
	public String getGuess() {
		int correct = 0;
		String quess = "";
		this.keysIterator = this.availableGuesses.keySet().iterator();
		while (this.keysIterator.hasNext()){
			String temp = keysIterator.next();
			this.values = availableGuesses.get(temp);
			quess = values.get(0);
			for (var value: values){
				for (var answer : this.correctAnswers){
					if(answer.equalsIgnoreCase(value)) {
						correct++;
						break;
					}
				}
			}
			if(correct > (values.size() - 1) / 2) {
				System.out.println("Player: "+ this.name + " Am I " + quess + "?");
				return quess;
			}
			correct = 0;
		}
		System.out.println("Player: "+ this.name + "Am I " + quess);
		return quess;
	}

	@Override
	public boolean isReadyForGuess() {
		return this.availableQuestions.isEmpty();
	}

	@Override
	public void setCorrectAnswers(String characteristic){
		this.correctAnswers.add(characteristic);
		if(this.keysIterator.hasNext()) this.values = new ArrayList<>(this.availableQuestions.remove(keysIterator.next()));
	}
}
