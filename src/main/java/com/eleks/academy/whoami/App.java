package com.eleks.academy.whoami;

import java.util.List;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.impl.RandomGame;
import com.eleks.academy.whoami.core.impl.RandomPlayer;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Game Init!");
		
		List<String> characters = List.of("Batman", "Superman", "Apple-Man", "Bloodshot", "Kolobok", "Red Hood" );
		List<String> questions = List.of("Am i a human?", "Am i a character from a movie?", "Am I from a fairytale?", "Am I from this planet?");
		List<String> guessess = List.of("Batman", "Superman", "Apple-Man", "Bloodshot", "Kolobok","Red Hood" );
		
		Game game = new RandomGame(characters);
		game.addPlayer(new RandomPlayer("Test1", questions, guessess));
		game.addPlayer(new RandomPlayer("Test2", questions, guessess));
		game.assignCharacters();
		game.initGame();
		while (!game.isFinished()) {
			boolean turnResult = game.makeTurn();
			while (turnResult) {
				turnResult = game.makeTurn();
			}
			game.changeTurn();
		}
		
		
		
	}
	

}
