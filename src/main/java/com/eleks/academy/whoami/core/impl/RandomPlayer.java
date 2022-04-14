package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import com.eleks.academy.whoami.core.Character;
import com.eleks.academy.whoami.core.Player;

public class RandomPlayer implements Player {

	private String name;
	private Map <String, List <String>> availableQuestions;
	private Set<String> keys; //availableQuestions.keySet();
	Iterator <String> keysIterator;
	//private Collection<List<String>> values = availableQuestions.values();
	List<String> values;
	//ListIterator <String> valuesIterator = values.listIterator();
	
	private List<String> availableGuesses;
	
	public RandomPlayer(String name, Map <String, List <String>> availableQuestions, List<String> availableGuesses) {
		this.name = name;
		this.availableQuestions = new HashMap<>(availableQuestions);
		this.availableGuesses = new ArrayList<String>(availableGuesses);
		keys = availableQuestions.keySet();
		keysIterator = keys.iterator();
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getQuestion() {
//		keys = availableQuestions.keySet();
//		keysIterator = keys.iterator();

		if(keysIterator.hasNext()) values = availableQuestions.remove(keysIterator.next()); //!!!!!!!!!!!!!!!!!!! ERROR!!!

		if (values.isEmpty()) {
            System.out.println("Value is empty");
            if (keysIterator.hasNext()) {
                values = availableQuestions.remove(keysIterator.next());
            }
        }
		String question = values.remove(0);
		System.out.println("Player: " + name + ". Asks: Am I a " + question + "?");
		return question;
	}

	@Override
	public String answerQuestion(String question, Character character){
		String answer = "No";
		for (var feature : character.getCharacteristics()){
			if(question.contains(feature)){
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
		int randomPos = (int)(Math.random() * this.availableGuesses.size()); 
		String guess = this.availableGuesses.remove(randomPos);
		System.out.println("Player: " + name + ". Guesses: Am I " + guess);
		return guess;
	}

	@Override
	public boolean isReadyForGuess() {
		return availableQuestions.isEmpty(); //+
	}

	
	
}
