package com.eleks.academy.whoami.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.eleks.academy.whoami.constants.Constants.YES;
import static com.eleks.academy.whoami.constants.Constants.NO;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;
import com.eleks.academy.whoami.utils.PropertyUtils;

public class RandomGame implements Game {

	private Map<String, String> playersCharacter = new HashMap<>();
	private List<Player> players = new ArrayList<>();
	private List<String> availableCharacters;
	private Turn currentTurn;

	public RandomGame() {
		this.availableCharacters = PropertyUtils.getCharacters();
	}

	@Override
	public void initGame() {
		this.currentTurn = new TurnImpl(this.players);
	}

	@Override
	public void addPlayer(Player player) {
		this.players.add(player);
	}

	@Override
	public void assignCharacters() {
		players.stream().forEach(player -> this.playersCharacter.put(player.getName(), this.getRandomCharacter()));
	}

	private String getRandomCharacter() {
		int randomPos = (int)(Math.random() * this.availableCharacters.size());
		return this.availableCharacters.remove(randomPos);
	}









	




	@Override
	public boolean makeTurn() {
		Player currentGuesser = currentTurn.getGuesser();
		Set<String> answers;

		System.out.println("Player " + currentGuesser.getName() + " answer the question. " + "Assumption(0) or Clarification(1)?");
		if(currentGuesser.getAssumptionOrClarification() == 0) {
			System.out.println("Player " + currentGuesser.getName() + " answer the question. " + "Assumption.");
			String question = currentGuesser.getQuestion();
			answers = currentTurn.getOtherPlayers().stream()
					.map(player -> player.answerQuestion(question, this.playersCharacter.get(currentGuesser.getName())))
					.collect(Collectors.toSet());
			long positiveCount = answers.stream().filter(a -> YES.equals(a)).count();
			long negativeCount = answers.stream().filter(a -> NO.equals(a)).count();
			boolean yesOrNot = positiveCount > negativeCount;
			currentGuesser.removeCharacters(yesOrNot);
			return yesOrNot;
		} else {
			System.out.println("Player " + currentGuesser.getName() + " answer the question. " + "Clarification.");
			String guess = currentGuesser.getGuess();
			answers = currentTurn.getOtherPlayers().stream()
					.map(player -> player.answerGuess(guess, this.playersCharacter.get(currentGuesser.getName())))
					.collect(Collectors.toSet());
			long positiveCount = answers.stream().filter(a -> YES.equals(a)).count();
			long negativeCount = answers.stream().filter(a -> NO.equals(a)).count();
			boolean win = positiveCount > negativeCount;
			if (win) {
				players.remove(currentGuesser);
			}
			return win;
		}
	}


	




	


	@Override
	public void changeTurn() {
		this.currentTurn.changeTurn();
	}

	@Override
	public boolean isFinished() {
		return players.size() == 1;
	}
}
