package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class RandomGame implements Game {

	private static final int DURATION = 2;
	private static final TimeUnit UNIT = TimeUnit.MINUTES;

	private final Map<String, String> playersCharacter = new ConcurrentHashMap<>();
	private final List<Player> players;
	private final List<String> availableCharacters;
	private Turn currentTurn;


	private static final String YES = "Yes";
	private static final String NO = "No";

	public RandomGame(List<Player> players, List<String> availableCharacters) {
		this.availableCharacters = new ArrayList<>(availableCharacters);
		this.players = new ArrayList<>(players.size());
		players.forEach(this::addPlayer);
	}

	private void addPlayer(Player player) {
		// TODO: Add test to ensure that player has not been added to the lists when failed to obtain suggestion
		Future<String> maybeCharacter = player.suggestCharacter();
		try {
			String character = maybeCharacter.get(DURATION, UNIT);
			this.players.add(player);
			this.availableCharacters.add(character);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			System.err.printf("Player did not suggest a charater within %d %s%n", DURATION, UNIT);
		}
	}

	@Override
	public boolean makeTurn() {
		Player currentGuesser = currentTurn.getGuesser();
		Set<String> answers;

		String guessersName = currentGuesser.getName();

		try {
			if (currentGuesser.isReadyForGuess().get()) {
				String guess = currentGuesser.getGuess().get();
				answers = currentTurn.getOtherPlayers().stream()
						.map(player -> {
							try {
								return player.answerGuess(guess, this.playersCharacter.get(guessersName)).get();
							} catch (InterruptedException | ExecutionException e) {
								throw new RuntimeException(e);
							}
						})
						.collect(Collectors.toSet());
				long positiveCount = answers.stream().filter(YES::equals).count();
				long negativeCount = answers.stream().filter(NO::equals).count();

				boolean win = positiveCount > negativeCount;

				if (win) {
					players.remove(currentGuesser);
				}
				return win;

			} else {
				String question = currentGuesser.getQuestion().get();
				answers = currentTurn.getOtherPlayers().stream()
						.map(player -> {
							try {
								return player.answerQuestion(question, this.playersCharacter.get(guessersName)).get();
							} catch (InterruptedException | ExecutionException e) {
								throw new RuntimeException(e);
							}
						})
						.collect(Collectors.toSet());
				long positiveCount = answers.stream().filter(YES::equals).count();
				long negativeCount = answers.stream().filter(NO::equals).count();
				return positiveCount > negativeCount;
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}

	}

	private void assignCharacters() {
		players.stream().map(Player::getName).parallel()
				.forEach(name -> this.playersCharacter.put(name, this.getRandomCharacter()));

	}

	@Override
	public void initGame() {
		this.assignCharacters();
		this.currentTurn = new TurnImpl(this.players);
	}


	@Override
	public boolean isFinished() {
		return players.size() == 1;
	}

	private String getRandomCharacter() {
		int randomPos = (int) (Math.random() * this.availableCharacters.size());
		// TODO: Ensure player never receives own suggested character
		return this.availableCharacters.remove(randomPos);
	}

	@Override
	public void changeTurn() {
		this.currentTurn.changeTurn();
	}

	@Override
	public void play() {
		boolean gameStatus = true;

		while (gameStatus) {
			boolean turnResult = this.makeTurn();

			while (turnResult) {
				turnResult = this.makeTurn();
			}
			this.changeTurn();
			gameStatus = !this.isFinished();
		}
	}

}
