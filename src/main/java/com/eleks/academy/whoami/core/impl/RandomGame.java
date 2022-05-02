package com.eleks.academy.whoami.core.impl;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;
import com.eleks.academy.whoami.core.impl.exception.NoPlayerNameException;

public class RandomGame implements Game {

	private static final int DURATION = 2;
	private static final TimeUnit UNIT = TimeUnit.MINUTES;

	private Map<String, String> playersCharacter = new ConcurrentHashMap<>();
	private final List<Player> players;
	private final List<String> availableCharacters;
	private Turn currentTurn;

	
	private final static String YES = "Yes";
	private final static String NO = "No";
	
	public RandomGame(List<Player> players, List<String> availableCharacters) {
		this.availableCharacters = new ArrayList<String>(availableCharacters);
		this.players = new ArrayList<>(players.size());
		players.forEach(this::addPlayer);
	}

    @Override
	public void addPlayer(Player player) {
		// TODO: Add test to ensure that player has not been added to the lists when failed to obtain suggestion
		Future<String> maybeCharacter = player.suggestCharacter();
		try {
			String character = maybeCharacter.get(DURATION, UNIT);
			this.players.add(player);
			this.availableCharacters.add(character);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			System.err.println("Player did not suggest a character within %d %s".formatted(DURATION, UNIT));
		}
	}

    @Override
    public void addCharacter(Player player) {
        Future<String> maybeCharacter = player.suggestCharacter();
        try {
            String character = maybeCharacter.get(DURATION, UNIT);
            this.availableCharacters.add(character);
            System.out.println("Player [" + player.getNameOnly() + "] character added...");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.err.println("Player did not suggest a character within %d %s".formatted(DURATION, UNIT));
        }
    }

    @Override
    public boolean makeTurn() {
        Player currentGuesser = currentTurn.getGuesser();
        Set<String> answers;
        String guessersName = null;
        boolean isReadyForGuess = false;
        try {
            guessersName = currentGuesser.getName().get(DURATION, UNIT);
            isReadyForGuess = currentGuesser.isReadyForGuess().get(DURATION, UNIT);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            /*
            Try to use isEmpty but intellij say that it can produce NullPointerException
            */
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
            String character = this.playersCharacter.get(guessersName);
            answers = currentTurn.getOtherPlayers().stream()
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
            String character = this.playersCharacter.get(guessersName);
            answers = currentTurn.getOtherPlayers().stream()
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
		players.stream().map(Player::getName).parallel().map(f -> {
			// TODO: extract into a configuration parameters
			try {
				return f.get(DURATION, UNIT);
			} catch (InterruptedException | ExecutionException e) {
				Thread.currentThread().interrupt();
				// TODO: Add custom runtime exception implementation
				throw new RuntimeException("Failed to obtain a player's name", e);
			} catch (TimeoutException e) {
				// TODO: Choose a name from a pool of names, i.e. Anonymous Badger etc.
				throw new RuntimeException("Player did not provide a name within %d %s".formatted(DURATION, UNIT));
			}
		}).forEach(name -> this.playersCharacter.put(name, this.getRandomCharacter()));
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

    public boolean isPlayerAdded(Player player) {
        return players.contains(player);
    }

}
