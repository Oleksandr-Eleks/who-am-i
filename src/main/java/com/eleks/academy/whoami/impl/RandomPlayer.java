package com.eleks.academy.whoami.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.utils.PropertyUtils;
import static com.eleks.academy.whoami.constants.Constants.YES;
import static com.eleks.academy.whoami.constants.Constants.NO;
import static com.eleks.academy.whoami.constants.Constants.GUESS_QUESTIONS;
import static com.eleks.academy.whoami.constants.Constants.CHARACTERS;
public class RandomPlayer implements Player {

	private String name;

	private Map<String, String> guessQuestions;
	private Map<String, String> allQuestions;
	private Map<String, List<String>> characters;
	private String numberQuestion;

	public RandomPlayer(String name) {
		this.name = "Bot-" + name;
		this.guessQuestions = PropertyUtils.readPropertyFile(GUESS_QUESTIONS);
		this.allQuestions = PropertyUtils.readPropertyFile(GUESS_QUESTIONS);
		this.characters = PropertyUtils.getCharactersWithQuestions(CHARACTERS);
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getAssumptionOrClarification() {
		return new Random().nextInt(2);
	}

	@Override
	public String getQuestion() {
		int randomQuestion = new Random().nextInt(guessQuestions.size() + 1);
		String question = guessQuestions.remove(String.valueOf(randomQuestion));
		System.out.println("Player: " + name + ". Asks: " + question);
		return question;
	}

	@Override
	public String answerQuestion(String question, String character) {
		return characters.get(character).stream().filter(que -> {if(allQuestions.get(que).equals(question)){
			this.numberQuestion = que;
			return true;
		} else {
			return false;
		}
		}).count() > 0 ? YES : NO;
	}

	@Override
	public void removeCharacters(boolean yesOrNot) {
		if(yesOrNot){
			Map<String, List<String>> removeCharacters = characters.entrySet().stream().filter(character -> !character.getValue().contains(numberQuestion))
					.collect(Collectors.toMap(characterKey -> characterKey.getKey(), characterValue -> characterValue.getValue()));
			removeCharacters.entrySet().forEach(character -> characters.remove(character.getKey(), character.getValue()));
		} else {
			Map<String, List<String>> removeCharacters = characters.entrySet().stream().filter(character -> character.getValue().contains(numberQuestion))
					.collect(Collectors.toMap(characterKey -> characterKey.getKey(), characterValue -> characterValue.getValue()));
			removeCharacters.entrySet().forEach(character -> characters.remove(character.getKey(), character.getValue()));
		}
	}

	@Override
	public String getGuess() {
		int randomCharacter = new Random().nextInt(characters.keySet().size());
		String character[] = characters.keySet().toArray(new String[characters.keySet().size()]);
		String guess = character[randomCharacter];
		System.out.println("Player: " + name + ". Guesses: Am I " + guess + " ?");
		return guess;
	}

	@Override
	public String answerGuess(String guess, String character) {
		String answer = guess.equals(character) ? YES : NO;
		System.out.println("Player: " + name + ". Answers: " + answer);
		return answer;
	}
}
