package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;

public class RandomGame implements Game {

	static {
		System.out.println("Game created. W8 players...");
	}
	private Turn currentGameTurn;
	private List<String> characters;
	private CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();
	private Map<String, String> playersCharacters = new HashMap<>();
	private List<String> gameResult = new ArrayList<>();

	public RandomGame(List<String> characters) {
		this.characters = new ArrayList<>(characters);
	}

	@Override
	public void init() {
		displayPlayers();
		assignCharacters();
		start();

		while (isFinished() != true) {
			System.out.println("\tTurn started...\n");
			boolean isTurnEnded = makeTurn();

			while (isTurnEnded != true) {
				isTurnEnded = makeTurn();
			}
			endTurn();
		}
		displayResults();
	}

	@Override
	public boolean makeTurn() {
		Player currentPlayer = currentGameTurn.getGuesser();
		/*
		 * TODO: parallel answers -> count -> display result -> -> peek another
		 * player(Guesser) (maybe better use some concurrent Queue)
		 * 
		 */
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
		List<Boolean> playersAnswers = new ArrayList<>(players.size() - 1);

		playersAnswers = currentGameTurn.getOtherPlayers().stream().parallel().map(player -> player.answerQuestion(playerQuestion,
				currentPlayer.getName(), playersCharacters.get(currentPlayer.getName()))).collect(Collectors.toList());

		long yes = playersAnswers.stream().filter(answer -> answer.equals(true)).count();
		long no = playersAnswers.stream().filter(answer -> answer.equals(false)).count();

		System.out.println("[Yes] = " + yes + " [No] = " + no);
		return yes > no;
	}

	private boolean giveGuess(String playerGuess, Player currentPlayer) {
		List<Boolean> playersAnswers;

		playersAnswers = currentGameTurn.getOtherPlayers().stream().parallel().map(player -> player.answerGuess(playerGuess,
				currentPlayer.getName(), playersCharacters.get(currentPlayer.getName()))).collect(Collectors.toList());

		long yes = playersAnswers.stream().filter(answer -> answer.equals(true)).count();
		long no = playersAnswers.stream().filter(answer -> answer.equals(false)).count();

		if (yes > no) {
			gameResult.add(currentPlayer.getName());
			players.remove(currentPlayer);
		}
		System.out.println("[Yes] = " + yes + " [No] = " + no);
		return true;
	}

	@Override
	public void endTurn() {
		System.out.println("\n\tTurn ended!");
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
		players.add(player);
		System.out.println("Pl-size(): " + players.size());
	}

	@Override
	public int countPlayers() {
		return players.size();
	}

	@Override
	public void displayPlayers() {
		System.out.println("Players:");
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
	public void displayResults() {
		System.out.println("\tGame finished!\n\tGame results:\n");
		for (int i = 0; i < gameResult.size(); i++) {
			if (i == 0) {
				System.out.println(gameResult.get(i) + " <---[WINNER]");
				continue;
			}
			System.out.println("---> " + gameResult.get(i));
		}
	}

}
