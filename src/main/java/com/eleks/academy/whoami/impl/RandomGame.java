package com.eleks.academy.whoami.impl;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;
import com.eleks.academy.whoami.utils.PropertyUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.eleks.academy.whoami.constants.Constants.NO;
import static com.eleks.academy.whoami.constants.Constants.YES;

public class RandomGame implements Game {

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
		players.stream().forEach(player -> player.setCharacter(getRandomCharacter()));
	}

	private String getRandomCharacter() {
		int randomPos = (int) (Math.random() * this.availableCharacters.size());
		return this.availableCharacters.remove(randomPos);
	}

	@Override
	public boolean makeTurn() {
		Player currentGuesser = currentTurn.getGuesser();
		Map<String, Long> answers;
		System.out.println("Player: " + currentGuesser.getName() + " answer the question. " + "Assumption(0) or Clarification(1)?");
		if (currentGuesser.getAssumptionOrClarification() == 0) {
			System.out.println("Player: " + currentGuesser.getName() + " answer the question. Assumption.");
			System.out.println("Player: " + currentGuesser.getName() + " is " + currentGuesser.getCharacter() + ".");
			String question = currentGuesser.getQuestion();
			answers = currentTurn.getOtherPlayers().stream()
					.map(player -> player.answerQuestion(question, currentGuesser.getCharacter()))
					.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
			boolean win = isWin(answers);
			currentGuesser.removeCharactersWithQuestions(win);
			return win;
		} else {
			System.out.println("Player: " + currentGuesser.getName() + " answer the question. Clarification.");
			String guess = currentGuesser.getGuess();
			System.out.println("Player: " + currentGuesser.getName() + " is " + currentGuesser.getCharacter() + ".");
			answers = currentTurn.getOtherPlayers().stream()
					.map(player -> player.answerGuess(guess, currentGuesser.getCharacter()))
					.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
			boolean win = isWin(answers);
			if (win) {
				System.out.println("Player: " + currentGuesser.getName() + " is WIN!!!");
				players.remove(currentGuesser);
			} else {
				currentGuesser.removeCharactersWithCharacter();
			}
			return false;
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

	private boolean isWin(Map<String, Long> answers) {
		return (Objects.isNull(answers.get(YES)) ? 0 : answers.get(YES)) > (Objects.isNull(answers.get(NO)) ? 0 : answers.get(NO));
	}

}
