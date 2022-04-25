package com.eleks.academy.whoami;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.impl.RandomGame;
import com.eleks.academy.whoami.impl.RandomPlayer;

public class App {
	public static void main(String[] args) {
		Game game = new RandomGame();
		game.addPlayer(new RandomPlayer("Test1"));
		game.addPlayer(new RandomPlayer("Test2"));
		game.addPlayer(new RandomPlayer("Test3"));
		game.addPlayer(new RandomPlayer("Test4"));
		game.addPlayer(new RandomPlayer("Test5"));
		game.assignCharacters();
		game.initGame();

		while (!game.isFinished()) {
			boolean turnResult = game.makeTurn();
			while (turnResult) {
				turnResult = game.makeTurn();
			}
			if (!game.isFinished()) {
				game.changeTurn();
			}
		}
	}
}
