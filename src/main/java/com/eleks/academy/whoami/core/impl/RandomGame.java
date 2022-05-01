package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;

public class RandomGame implements Game {

	private final static int DURATION = 2;
	private final static TimeUnit UNIT = TimeUnit.MINUTES;

	private List<Player> players;
	private List<String> characters;
	private Map<String, String> playersCharacters = new ConcurrentHashMap<>();
	private Player currentPlayer;
	private List<String> playersAnswers = new ArrayList<>();
	private List<String> gameResult = new ArrayList<>();

	public RandomGame(List<Player> players) {
		this.players = new ArrayList<>(players.size());
		this.characters = new ArrayList<>(players.size());
		players.parallelStream().forEach(this::addPlayer);
		this.players.parallelStream().forEach(this::addPlayerCharacter);
	}

	@Override
	public void init() {
		displayPlayers();
		assignCharacters();

		while (!isFinished()) {
			System.out.println("\tTurn started...\n");
			makeTurn();
			System.out.println("\n\tTurn ended!");
		}
		displayResults();
	}

	@Override
	public void addPlayer(Player player) {
		Future<String> suggestedName = player.askName();
		try {
			String name = suggestedName.get(DURATION, UNIT);
			if (validatePlayerName(name)) {
				players.add(player);
				System.out.println("Player [" + player.getName() + "] connected...");
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} catch (TimeoutException e) {
			System.err.println("Player did not provide a nickname within %d %s".formatted(DURATION, UNIT));
		}

	}

	private boolean validatePlayerName(String name) {
		if (name == null || name.isBlank()) {
			return false;
		}
		Pattern pattern = Pattern.compile("^\\s*[a-zA-Z0-9]+\\s*$");
		Matcher matcher = pattern.matcher(name);
		return matcher.matches();
	}

	@Override
	public void addPlayerCharacter(Player player) {
		Future<String> suggestedCharacter = player.askCharacter();
		try {
			String character = suggestedCharacter.get(DURATION, UNIT);
			if (validateCharacter(character)) {
				characters.add(character.strip());
				System.out.println("Player [" + player.getName() + "] character added...");
			}
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
		} catch (TimeoutException e) {
			System.err.println("Player did not suggest a character within %d %s".formatted(DURATION, UNIT));
		}
	}

	private boolean validateCharacter(String character) {
		if (character == null || character.isBlank()) {
			return false;
		}
		Pattern pattern = Pattern.compile("^\\s*[a-zA-Z]+\\s*$");
		Matcher matcher = pattern.matcher(character);
		return matcher.matches();
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
	public void makeTurn() {
		boolean massYes;
		currentPlayer = players.remove(0);
		try {

			if (!gameResult.contains(currentPlayer.getName())) {

				if (currentPlayer.isReadyForGuess().get(DURATION, UNIT).contentEquals("yes")) {
					playersAnswers.removeAll(playersAnswers);
					currentPlayer.askGuess().get(DURATION, UNIT);
					players.parallelStream().forEach(this::giveGuess);
					massYes = countAnswers();

					if (massYes) {
						gameResult.add(currentPlayer.getName());
					}

				} else {

					do {
						playersAnswers.removeAll(playersAnswers);
						currentPlayer.askQuestion().get(DURATION, UNIT);
						players.parallelStream().forEach(this::giveQuestion);
						massYes = countAnswers();
					} while (massYes);
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} catch (TimeoutException e) {
			System.err.println("Player didn't provide an answer within %d %s".formatted(DURATION, UNIT));
		}
		players.add(currentPlayer);
	}

	private void giveQuestion(Player player) {
		Future<String> askPlayer = player.answerQuestion(currentPlayer.getName(), currentPlayer.getQuestion(), playersCharacters.get(currentPlayer.getName()));
		try {
			String answer = askPlayer.get(DURATION, UNIT);
			playersAnswers.add(answer);
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
		} catch (TimeoutException e) {
			System.err.println("Player didn't provide an answer within %d %s".formatted(DURATION, UNIT));
		}
	}

	private void giveGuess(Player player) {
		Future<String> askPlayer = player.answerGuess(currentPlayer.getName(), currentPlayer.getGuess(), playersCharacters.get(currentPlayer.getName()));
		try {
			String answer = askPlayer.get(DURATION, UNIT);
			playersAnswers.add(answer);
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
		} catch (TimeoutException e) {
			System.err.println("Player didn't provide an answer within %d %s".formatted(DURATION, UNIT));
		}
	}

	private boolean countAnswers() {
		long yes = playersAnswers.stream().filter(ans -> ans.equals("yes")).count();
		long no = playersAnswers.stream().filter(ans -> ans.equals("no")).count();
		System.out.println("[YES] = " + yes + " [NO] = " + no);
		return yes > no;
	}

	@Override
	public boolean isFinished() {
		return players.size() == gameResult.size();
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

	public boolean isConcretePlayerAdded(Player player) {
		return players.contains(player);
	}

	public boolean isConcreteCharacterAdded(String character) {
		return characters.contains(character);
	}
	
}
