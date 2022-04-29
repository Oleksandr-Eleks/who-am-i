package com.eleks.academy.whoami.core.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;

import com.eleks.academy.whoami.core.Player;

class RandomGameTest {
	
	private static final int DURATION = 5;
	private static final TimeUnit UNIT = TimeUnit.SECONDS;
	private RandomGame game = new RandomGame(List.of());

	@Test
	void declineAddPlayerCharacterWhenInvalidCharacter() throws InterruptedException, ExecutionException {
		Player player1 = new RandomPlayer("BotA", "");
		game.addPlayerCharacter(player1);
		Player player2 = new RandomPlayer("BotB", "12");
		game.addPlayerCharacter(player2);
		Player player3 = new RandomPlayer("BotC", "  Car car");
		game.addPlayerCharacter(player3);
		Player player4 = new RandomPlayer("BotD", "C@r$");
		game.addPlayerCharacter(player4);
		Player player5 = new RandomPlayer("BotD", null);
		game.addPlayerCharacter(player5);

		assertFalse(game.isConcretePlayerAdded(player1));
		assertFalse(game.isConcretePlayerAdded(player2));
		assertFalse(game.isConcretePlayerAdded(player3));
		assertFalse(game.isConcretePlayerAdded(player4));
		assertFalse(game.isConcretePlayerAdded(player5));
	}

	@Test
	void acceptAddPlayerCharcterWhenValidCharacter() throws InterruptedException, ExecutionException, TimeoutException {
		Player player1 = new RandomPlayer("BotA", "Dino");
		game.addPlayerCharacter(player1);
		Player player2 = new RandomPlayer("BotB", "		Batman");
		game.addPlayerCharacter(player2);
		Player player3 = new RandomPlayer("BotC", "  Car ");
		game.addPlayerCharacter(player3);
		Player player4 = new RandomPlayer("BotD", "Diamond	");
		game.addPlayerCharacter(player4);
		
		assertTrue(game.isConcreteCharacterAdded(player1.getCharacter().get(DURATION, UNIT)));
		assertTrue(game.isConcreteCharacterAdded(player2.getCharacter().get(DURATION, UNIT)));
		assertTrue(game.isConcreteCharacterAdded(player3.getCharacter().get(DURATION, UNIT)));
		assertTrue(game.isConcreteCharacterAdded(player4.getCharacter().get(DURATION, UNIT)));
	}
}
