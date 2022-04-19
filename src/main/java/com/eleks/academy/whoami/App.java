package com.eleks.academy.whoami;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eleks.academy.whoami.core.*;
import com.eleks.academy.whoami.core.impl.*;
import com.eleks.academy.whoami.core.Character;
import com.eleks.academy.whoami.core.CharactersBase;

public class App implements CharactersBase{
	public static void main(String[] args) {
		System.out.println("Game Init!");
		int numberOfCharacters = 4;
		int numberOfPlayers = 2;
		List <Character> characters = new ArrayList<>();
		for (int i = 0; i < numberOfCharacters; i++) {
			characters.add(new CharacterCreator());
		}
		//List<String> questions = List.of("Am i a human?", "Am i a character from a movie?"); // interface Questions
//		List<String> guessess = List.of(batman.get(0), arestowych.get(0),scoobyDoo.get(0),garfield.get(0));

		QuestionCreator questions = new QuestionCreator();
		List<String> guessess = new ArrayList<>();
		for(Character character : characters) {
			guessess.add(character.getName());
		}
		Game game = new RandomGame(characters);
		for (int i = 0; i < numberOfPlayers; i++) {
			game.addPlayer(new RandomPlayer("Test " + i, questions.getQuestions(), guessess));
		}
		game.assignCharacters();//+
		game.initGame();
		while (!game.isFinished()) {
			boolean turnResult = game.makeTurn();
			while (turnResult) {
				turnResult = game.makeTurn();
			}
			game.changeTurn();
			System.out.println("\n");
		}

		
		
	}
	

}
