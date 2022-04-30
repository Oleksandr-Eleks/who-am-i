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
import com.eleks.academy.whoami.core.Turn;

public class RandomGame implements Game {

	private final static int DURATION = 30;
	private final static TimeUnit UNIT = TimeUnit.SECONDS;
	
	private List<Player> players;
	private List<String> characters;
	private Map<String, String> playersCharacters = new ConcurrentHashMap();
	private List<String> gameResult = new ArrayList<>();
	
	private Turn currentGameTurn;

	public RandomGame(List<Player> players) {
		this.players = new ArrayList<>(players.size());
		this.characters = new ArrayList<>(players.size());
		players.parallelStream().forEach(this::addPlayer);
		this.players.parallelStream().forEach(this::addPlayerCharacter);
	}

	@Override
	public void init() {
		displayPlayers();
		while (players.size() != 3) {
			
		}
//		assignCharacters();
//		start();
//
//		while (isFinished() != true) {
//			System.out.println("\tTurn started...\n");
//			boolean isTurnEnded = makeTurn();
//
//			while (isTurnEnded != true) {
//				isTurnEnded = makeTurn();
//			}
//			endTurn();
//		}
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
		Future<String> suggestedCharacter = player.aksCharacter();
		try {
			String character = suggestedCharacter.get(DURATION, UNIT);
			if (validateCharacter(character)) {
				characters.add(character.strip());
				System.out.println("Player [" + player.getName() + "] character added...");
			}			
		} catch (InterruptedException | ExecutionException e) {
			Thread.currentThread().interrupt();
		} catch (TimeoutException e) {
			System.err.println("Player did not suggest a charatern within %d %s".formatted(DURATION, UNIT));
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
	public boolean makeTurn() {
		return true;
	}

	private boolean giveQuestion(String playerQuestion, Player currentPlayer) {
		return true;
	}

	private boolean giveGuess(String playerGuess, Player currentPlayer) {
		return true;
	}

	@Override
	public void endTurn() {
		System.out.println("\n\tTurn ended!");
		currentGameTurn.changeTurn();
	}

	@Override
	public boolean isFinished() {
		return players.size() == 1;
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
//		players.stream().forEach(player -> playersCharacters.put(player.getName(), getRandomCharacter()));

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
	
	public boolean isConcretePlayerAdded(Player player) {
		return players.contains(player);
	}
	
	public boolean isConcreteCharacterAdded(String character) {
		return characters.contains(character);
	}
}
