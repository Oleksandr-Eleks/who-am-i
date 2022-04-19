package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eleks.academy.whoami.core.Character;
import com.eleks.academy.whoami.core.CharactersBase;
import com.eleks.academy.whoami.core.Player;

public class RandomPlayer implements Player, CharactersBase {

	private final String name;
	private Map <String, List <String>> availableQuestions;
	private Set<String> keys;
	private Iterator <String> keysIterator;
	private List<String> values;
	private List<String> correctCharacteristics = new ArrayList<>();
	private List<String> availableGuesses;

	public RandomPlayer(String name, Map <String, List <String>> availableQuestions, List<String> availableGuesses) {
		this.name = name;
		this.availableQuestions = new HashMap<>(availableQuestions);
		this.availableGuesses = new ArrayList<>(availableGuesses);
		keys = new HashSet<>(availableQuestions.keySet());
		keysIterator = keys.iterator();
		values = new ArrayList<>(availableQuestions.remove(keysIterator.next()));
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getQuestion() {

		//if(keysIterator.hasNext()) values = availableQuestions.remove(keysIterator.next()); //!!!!!!!!!!!!!!!!!!! ERROR!!!
		if(values.isEmpty()) {
			if (keysIterator.hasNext()) {
				System.out.println("Values is empty " + name);
//				values.add(availableGuesses.remove(keysIterator.next()));
//					String temp = keysIterator.next();
//					availableQuestions.remove(temp);
//					values.add(temp);
					values = new ArrayList<>(availableQuestions.remove(keysIterator.next()));

			}
		}
		String question = values.remove(0);
		System.out.println("Player: " + name + ". Asks: Am I a " + question + "?");
		return question;
	}

	@Override
	public String answerQuestion(String question, Character character){//!!!ERROR!!!
		String answer = "No";
		for (var feature : character.getCharacteristics()){
			if(question.equals(feature)){ //!!!ERROR!!!
				answer = "Yes";
				break;
			}
		}
		System.out.println("Player: " + name + ". Answers: " + answer);
		return answer;
	}
	

	@Override
	public String answerGuess(String guess, Character character) {
		String answer = "No";
		if (character.getName().equals(guess)){
			answer = "Yes";
		}
		//String answer = Math.random() < 0.5 ? "Yes" : "No";
		System.out.println("Player: " + name + ". Answers: " + answer);
		return answer;
	}

	@Override
	public String getGuess() {
		boolean b = false;
		while (!b){

		}


		int randomPos = (int)(Math.random() * this.availableGuesses.size());
		String guess = this.availableGuesses.remove(randomPos);
		System.out.println("Player: " + name + ". Guesses: Am I " + guess);
		return guess;
	}

	@Override
	public boolean isReadyForGuess() {
		return availableQuestions.isEmpty(); //+
	}

	@Override
	public void setCorrectCharacteristics(String characteristic){
		correctCharacteristics.add(characteristic);
	}

	
	
}
