package com.eleks.academy.whoami.core.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;

import com.eleks.academy.whoami.core.Player;

class RandomGameTest {

	private RandomGame game = new RandomGame();
	private List<String> guesses = List.of("Batman", "Superman", "Superwoman", "Robin", "Tanos");
	private List<String> questions = List.of("Am i a human?", "Am i a character from a movie?", "Am i a male?",
			"Am i a female?");

	@Test
	void declineAddPlayerWhenInvalidCharacterSuggestion() throws InterruptedException, ExecutionException {
		Player player1 = new RandomPlayer("BotA", "", questions, guesses);
		game.addPlayer(player1);
		Player player2 = new RandomPlayer("BotB", "12", questions, guesses);
		game.addPlayer(player2);
		Player player3 = new RandomPlayer("BotC", "  Car car", questions, guesses);
		game.addPlayer(player3);
		Player player4 = new RandomPlayer("BotD", "C@r$", questions, guesses);
		game.addPlayer(player4);

		assertFalse(game.isConcretePlayerAdded(player1));
		assertFalse(game.isConcretePlayerAdded(player2));
		assertFalse(game.isConcretePlayerAdded(player3));
		assertFalse(game.isConcretePlayerAdded(player4));
	}

	@Test
	void allPlayersReceiveCharacters() {

	}
}
