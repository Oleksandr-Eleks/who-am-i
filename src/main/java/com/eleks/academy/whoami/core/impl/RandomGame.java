package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
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
		this.players = new ArrayList<>(players);
		players.forEach(this::addPlayer);
		this.characters = new ArrayList<>(players.size());
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
		Future<String> suggestedName = player.getName();
		try {
			String name = suggestedName.get(DURATION, UNIT);
			if (validatePlayerName(name)) {
				players.add(player);
				System.out.println("Player [" + player.getName().get() + "] connected...");
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
		Future<String> suggestedCharacter = player.getCharacter();
		try {
			String character = suggestedCharacter.get(DURATION, UNIT);
			if (validateCharacter(character)) {
				characters.add(character.strip());
				System.out.println("Player [" + player.getName().get() + "] character added...");
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
//		Player currentPlayer = currentGameTurn.getGuesser();
//		/*
//		 * TODO: parallel answers -> count -> display result -> -> peek another
//		 * player(Guesser) (maybe better use some concurrent Queue)
//		 * 
//		 */
//		if (currentPlayer.isReadyForGuess()) {
//			return giveGuess(currentPlayer.getGuess(), currentPlayer);
//		} else {
//			boolean streak;
//
//			do {
//				String question = "";
//				try {
//					question = currentPlayer.getQuestion().get(5, TimeUnit.SECONDS);
//				} catch (ExecutionException | TimeoutException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				streak = giveQuestion(question, currentPlayer);
//			} while (streak != false);
//
//			return true;
//		}
		return true;
	}

	private boolean giveQuestion(String playerQuestion, Player currentPlayer) {
		if (playerQuestion.isBlank()) {
			return false;
		}
		List<Boolean> playersAnswers = new ArrayList<>();

//		playersAnswers = currentGameTurn.getOtherPlayers().stream().parallel().map(player -> player.answerQuestion(playerQuestion,
//				currentPlayer.getName(), playersCharacters.get(currentPlayer.getName()))).collect(Collectors.toList());

		long yes = playersAnswers.stream().filter(answer -> answer.equals(true)).count();
		long no = playersAnswers.stream().filter(answer -> answer.equals(false)).count();

		System.out.println("[Yes] = " + yes + " [No] = " + no);
		return yes > no;
	}

	private boolean giveGuess(String playerGuess, Player currentPlayer) {
		List<Boolean> playersAnswers;

//		playersAnswers = currentGameTurn.getOtherPlayers().stream().parallel().map(player -> player.answerGuess(playerGuess,
//				currentPlayer.getName(), playersCharacters.get(currentPlayer.getName()))).collect(Collectors.toList());

//		long yes = playersAnswers.stream().filter(answer -> answer.equals(true)).count();
//		long no = playersAnswers.stream().filter(answer -> answer.equals(false)).count();

//		if (yes > no) {
//			gameResult.add(currentPlayer.getName());
		players.remove(currentPlayer);
//		}
//		System.out.println("[Yes] = " + yes + " [No] = " + no);
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
//			gameResult.add(players.get(0).getName());
		}
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
