package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;
import com.eleks.academy.whoami.core.impl.exception.NoPlayerNameException;

public class RandomGame implements Game {

	private static final int DURATION = 2;
	private static final TimeUnit UNIT = TimeUnit.MINUTES;

    private final List<Player> players;
    private final List<String> availableCharacters;
    private final Map<String, String> playersCharacters = new ConcurrentHashMap<>();
	private Turn currentTurn;

	private final static String YES = "Yes";
	private final static String NO = "No";
	
	public RandomGame(List<Player> players, List<String> availableCharacters) {
        this.availableCharacters = new ArrayList<String>(availableCharacters);
        this.players = new ArrayList<>(players.size());
        players.parallelStream().forEach(this::addPlayer);
	}

	private void addPlayer(Player player) {
        // TODO: Add test to ensure that player has not been added to the lists when failed to obtain suggestion
		try {
            String name;
            do {
                Future<String> suggestedName = player.getName();
                name = suggestedName.get(DURATION, UNIT);
            } while (!this.correctWord(name));
			this.players.add(player);
            System.out.println("Player \"" + player.getNameOnly() + "\" added");
            this.addCharacter(player);
        } catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			System.err.println("Player did not suggest a character within %d %s".formatted(DURATION, UNIT));
		}
    }

    private void addCharacter(Player player) {
        try {
            String character;
            do {
                Future<String> maybeCharacter = player.suggestCharacter();
                character = maybeCharacter.get(5, TimeUnit.SECONDS);
            } while (!this.correctWord(character));
            this.availableCharacters.add(character);
            System.out.println("Player \"" + player.getNameOnly() + "\" added character \"" + character + "\"");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.err.println("Player did not suggest a character within %d %s".formatted(DURATION, UNIT));
        }
    }

    private boolean correctWord(String word) {
        return !word.isBlank();
    }

    @Override
    public boolean makeTurn() {
        Player currentGuesser = currentTurn.getGuesser();
        Set<String> answers;
        String guessersName = null;
        boolean isReadyForGuess = false;
        try {
            guessersName = currentGuesser.getNameOnly()/*.get(DURATION, UNIT)*/;
            isReadyForGuess = currentGuesser.isReadyForGuess().get(DURATION, UNIT);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            if (guessersName != null) {
                throw new NoPlayerNameException("Failed to obtain a player's name", e);
            }
        }
        if (isReadyForGuess) {
            String guess = null;
            try {
                guess = currentGuesser.getGuess().get(DURATION, UNIT);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }

            String finalGuess = guess;
            String character = this.playersCharacters.get(guessersName);
            answers = currentTurn.getOtherPlayers().parallelStream()
                    .map(player -> {
                        try {
                            return player.answerGuess(finalGuess, character).get(DURATION, UNIT);
                        } catch (InterruptedException | ExecutionException | TimeoutException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }).collect(Collectors.toSet());

            long positiveCount = answers.stream().filter(a -> YES.equals(a)).count();
            long negativeCount = answers.stream().filter(a -> NO.equals(a)).count();

            boolean win = positiveCount > negativeCount;

            if (win) {
                players.remove(currentGuesser);
            }
            return win;
        } else {
            String question = null;
            try {
                question = currentGuesser.getQuestion().get(DURATION, UNIT);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }

            String finalQuestion = question;
            String character = this.playersCharacters.get(guessersName);
            answers = currentTurn.getOtherPlayers().parallelStream()
                    .map(player -> {
                        try {
                            return player.answerQuestion(finalQuestion, character).get(DURATION, UNIT);
                        } catch (InterruptedException | ExecutionException | TimeoutException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }).collect(Collectors.toSet());

            long positiveCount = answers.stream().filter(a -> YES.equals(a)).count();
            long negativeCount = answers.stream().filter(a -> NO.equals(a)).count();

            return positiveCount > negativeCount;
        }
    }

    private void assignCharacters() {
        players.stream().forEach(player -> playersCharacters.put(player.getNameOnly(), getRandomCharacter()));
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
