package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;

public class RandomGame implements Game {

	static {
		System.out.println("\tGAME STARTED!\n");
	}
	private Turn currentGameTurn;
	private List<String> characters;
	private List<Player> players;
	private Map<String, String> playersCharacters = new HashMap<>();
	private List<String> gameResult = new ArrayList<>();
	private int turnCount = 0;

	public RandomGame(List<String> characters, List<Player> players) {
		this.characters = new ArrayList<>(characters);
		this.players = new ArrayList<>(players);
	}

	@Override
	public void init() {
		displayPlayers();
		assignCharacters();
		start();

		while (isFinished() != true) {
			turnCount++;
			System.out.println("\tTURN #" + turnCount + " STARTED!\n");
			boolean isTurnEnd = makeTurn();

			while (isTurnEnd != true) {
				isTurnEnd = makeTurn();
			}
			endTurn();
		}
		displayResults();
	}

	@Override
	public void displayPlayers() {
		System.out.println("PLAYERS:");
		players.stream().forEach(player -> System.out.println("---> " + player.getName()));
		System.out.println();
	}

	@Override
	public void assignCharacters() {
		players.stream().forEach(player -> playersCharacters.put(player.getName(), getRandomCharacter()));

	}

	private String getRandomCharacter() {
		int randomPos = (int) (Math.random() * characters.size());
		return characters.remove(randomPos);
	}

	@Override
	public void start() {
		currentGameTurn = new TurnImpl(players);

	}

	@Override
	public boolean makeTurn() {
		Player currentPlayer = currentGameTurn.getGuesser();

		if (currentPlayer.isReadyForGuess()) {
			return giveGuess(currentPlayer.getGuess(), currentPlayer);
		} else {
			boolean streak;

			do {
				streak = giveQuestion(currentPlayer.getQuestion(), currentPlayer);
			} while (streak != false);

			return true;
		}

	}

	private boolean giveQuestion(String playerQuestion, Player currentPlayer) {
		if (playerQuestion.isBlank()) {
			return false;
		}
		List<Boolean> playersAnswers;

		playersAnswers = currentGameTurn.getOtherPlayers().stream()
				.map(player -> player.answerQuestion(playerQuestion, playersCharacters.get(currentPlayer.getName())))
				.collect(Collectors.toList());

		long yes = playersAnswers.stream().filter(answer -> answer.equals(true)).count();
		long no = playersAnswers.stream().filter(answer -> answer.equals(false)).count();

		return yes > no;
	}

	private boolean giveGuess(String playerGuess, Player currentPlayer) {
		List<Boolean> playersAnswers;

		playersAnswers = currentGameTurn.getOtherPlayers().stream()
				.map(player -> player.answerGuess(playerGuess, playersCharacters.get(currentPlayer.getName())))
				.collect(Collectors.toList());

		long yes = playersAnswers.stream().filter(answer -> answer.equals(true)).count();
		long no = playersAnswers.stream().filter(answer -> answer.equals(false)).count();

		if (yes > no) {
			gameResult.add(currentPlayer.getName());
			players.remove(currentPlayer);
		}
		return true;
	}

	@Override
	public void endTurn() {
		System.out.println("\n\tTURN ENDED!");
		currentGameTurn.changeTurn();
	}

	@Override
	public boolean isFinished() {
		if (players.size() == 1) {
			gameResult.add(players.get(0).getName());
		}
		return players.size() == 1;
	}

	@Override
	public void addPlayer(Player player) {
		this.players.add(player);
	}

	@Override
	public int countPlayers() {
		return players.size();
	}

	@Override
	public void displayResults() {
		System.out.println("\tGAME FINISHED!\n\tGAME RESULTS:\n");
		for (int i = 0; i < gameResult.size(); i++) {
			if (i == 0) {
				System.out.println("[WINNER]---> " + gameResult.get(i));
				continue;
			}
			System.out.println("---> " + gameResult.get(i));
		}
	}

}
