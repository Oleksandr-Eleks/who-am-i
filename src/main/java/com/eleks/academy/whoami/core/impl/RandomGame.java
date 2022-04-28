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

import com.eleks.academy.exception.GameRuntimeException;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;

public class RandomGame implements Game {

    private static final int DURATION = 2;
    private static final TimeUnit UNIT = TimeUnit.MINUTES;

    private Map<String, String> playersCharacter = new ConcurrentHashMap<>();
    private List<Player> players = new ArrayList<>();
    private List<String> availableCharacters;
    private Turn currentTurn;

    private final static String YES = "Yes";
    private final static String NO = "No";

    public RandomGame(List<String> availableCharacters) {
	this.availableCharacters = new ArrayList<String>(availableCharacters);
    }

    @Override
    public void addPlayer(Player player) {
	// TODO: Add test to ensure that player has not been added to the lists when
	// failed to obtain suggestion
	Future<String> maybeCharacter = player.suggestCharacter();
	try {
	    String character = maybeCharacter.get(DURATION, UNIT);
	    this.players.add(player);
	    this.availableCharacters.add(character);
	} catch (InterruptedException | ExecutionException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (TimeoutException e) {
	    System.err.println("Player did not suggest a charatern within %d %s".formatted(DURATION, UNIT));
	}
    }

    @Override
    public boolean makeTurn() {
	Player currentGuesser = currentTurn.getGuesser();
	Set<String> answers;
	String guessersName;
	try {
	    guessersName = currentGuesser.getName().get(DURATION, UNIT);
	} catch (InterruptedException | ExecutionException | TimeoutException e) {
	    throw new GameRuntimeException("Failed to obtain a player's name", e);
	}
	if (currentGuesser.isReadyForGuess()) {
	    boolean win = false;
	    try {
		String guess = currentGuesser.getGuess().get(DURATION, UNIT);
		answers = currentTurn.getOtherPlayers().stream().map(player -> {
		    try {
			return player.answerGuess(guess, this.playersCharacter.get(guessersName)).get(DURATION, UNIT);
		    } catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    } catch (TimeoutException e) {
			System.err.println("Player did not suggest a charatern within %d %s".formatted(DURATION, UNIT));
		    }
		    return "No";
		}).collect(Collectors.toSet());
		long positiveCount = answers.stream().filter(a -> YES.equals(a)).count();
		long negativeCount = answers.stream().filter(a -> NO.equals(a)).count();

		win = positiveCount > negativeCount;
	    } catch (InterruptedException | ExecutionException e) {
		e.printStackTrace();
	    } catch (TimeoutException e) {
		System.err.println("Player did not suggest a answer within %d %s".formatted(DURATION, UNIT));
	    }
	    if (win) {
		players.remove(currentGuesser);
	    }
	    return win;

	} else {
	    long negativeCount = 0;
	    long positiveCount = 0;
	    try {
		String question = currentGuesser.getQuestion().get(DURATION, UNIT);

		answers = currentTurn.getOtherPlayers().stream().map(player -> {
		    try {
			return player.answerQuestion(question, this.playersCharacter.get(guessersName)).get(DURATION,
				UNIT);
		    } catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    } catch (TimeoutException e) {
			System.err.println("Player did not suggest a answer within %d %s".formatted(DURATION, UNIT));
		    }
		    return "No";
		}).collect(Collectors.toSet());
		positiveCount = answers.stream().filter(a -> YES.equals(a)).count();
		negativeCount = answers.stream().filter(a -> NO.equals(a)).count();
	    } catch (InterruptedException | ExecutionException e) {
		throw new GameRuntimeException("Failed to obtain a player's question ", e);
	    } catch (TimeoutException e) {
		throw new GameRuntimeException("Player did not provide a name within %d %s".formatted(DURATION, UNIT));
	    }
	    return positiveCount > negativeCount;
	}

    }

    @Override
    public void assignCharacters() {
	players.stream().map(Player::getName).parallel().map(f -> {
	    // TODO: extract into a configuration parameters
	    try {
		return f.get(DURATION, UNIT);
	    } catch (InterruptedException | ExecutionException e) {
		Thread.currentThread().interrupt();
		throw new GameRuntimeException("Failed to obtain a player's name", e);
	    } catch (TimeoutException e) {
		throw new GameRuntimeException("Player did not provide a name within %d %s".formatted(DURATION, UNIT));
	    }
	}).forEach(name -> this.playersCharacter.put(name, this.getRandomCharacter()));

    }

    @Override
    public void initGame() {
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

}
