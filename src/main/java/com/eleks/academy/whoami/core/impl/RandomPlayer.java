package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.eleks.academy.whoami.core.Player;

public class RandomPlayer implements Player {
    
    public static final String HUMAN_LIST_KEY = "human";
    public static final String CHARACTER_LIST_KEY = "character";
    public static final String ANIMAL_LIST_KEY = "animal";
    
    private List<String> humansList = List.of("Elon Musk", "Steve Jobs");
    private List<String> charactersList = List.of("Batman", "Superman");
    private List<String> animalsList = List.of("Lion", "Zebra");
    
    Map<String, List<String>> characterMap;

	private String name;
	private List<String> availableQuestions;
	private List<String> availableGuesses;
	
	public RandomPlayer(String name, List<String> availableQuestions, List<String> availableGuesses) {
		this.name = name;
		this.availableQuestions = new ArrayList<String>(availableQuestions);
		this.availableGuesses = new ArrayList<String>(availableGuesses);
		initCharacterMap();
	}
	
	private void initCharacterMap() {
        characterMap = new HashMap<String, List<String>>();
        characterMap.put(HUMAN_LIST_KEY, humansList);
        characterMap.put(CHARACTER_LIST_KEY, charactersList);
        characterMap.put(ANIMAL_LIST_KEY, animalsList);
    }
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getQuestion() {
		String question = availableQuestions.remove(0);
		System.out.println("Player: " + name + ". Asks: " + question);
		return question;
	}

	@Override
	public String answerQuestion(String question, String character) {
        String answer = question.toLowerCase().contains(answersThinkings(character)) ? "Yes" : "No";;
        System.out.println("Player: " + name + ". Answers: " + answer);
        return answer;
	}
	
    private String answersThinkings(String character) {
        for (Entry<String, List<String>> entry : characterMap.entrySet()) {
            if (entry.getValue().contains(character)) {
                return entry.getKey().toString();
            }
        }
        return character;
    }

	@Override
	public String answerGuess(String guess, String character) {
        String answer = guess.toLowerCase().contains(character.toLowerCase()) ? "Yes" : "No";
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
		return availableQuestions.isEmpty();
	}

	
	
}
