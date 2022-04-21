package com.eleks.academy.whoami;

import java.util.ArrayList;
import java.util.List;


import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.impl.RandomGame;
import com.eleks.academy.whoami.core.impl.RandomPlayer;
import com.eleks.academy.whoami.core.impl.StrategyGame;
import com.eleks.academy.whoami.core.impl.StrategyPlayer;
import com.eleks.academy.whoami.utility.impl.DecisionTree;


public class App {
	public static void main(String[] args) {
		System.out.println("Game Init!");
		
		DecisionTree decisionTree = new DecisionTree("Am I a human");
		decisionTree.setLeft("Am I an animal?");
		decisionTree.getLeft().setLeft("randomRobots");
		decisionTree.getLeft().setRight("randomCartoonCharacters");
		
		decisionTree.setRight("Am I real?");
		decisionTree.getRight().setLeft("Am I a book character?");
		decisionTree.getRight().getLeft().setRight("randomBookCharacters");
		decisionTree.getRight().getLeft().setLeft("randomTvCharacters");
		decisionTree.getRight().setRight("Am I famous?");
		decisionTree.getRight().getRight().setLeft("randomPeople");
		decisionTree.getRight().getRight().setRight("Am I a politician?");
		decisionTree.getRight().getRight().getRight().setRight("randomPoliticians");
		decisionTree.getRight().getRight().getRight().setLeft("randomSuperstars");
		
		List<String> robots = List.of("randomRobots", "c3pio", "rd2d");
		List<String> cartoonCharacters = List.of("randomCartoonCharacters", "Tom", "Jerry");
		List<String> bookCharacters = List.of("randomBookCharacters", "Piter Pen", 
				                          "Ivasyk Telesyk", "Danny Tagart");
		List<String> tvCharacters = List.of("randomTvCharacters", "Dana Scully", "Stella Gibson");
		List<String> superstars = List.of("randomSuperstars", "Lana del Rey", "Mitski");
		List<String> politicians = List.of("randomPoliticians", "Joe Biden jr.", "Petro Poroshenko");
		List<String> people = List.of("randomPeople", "NATO soldier", "night club dancer");
		
		List<String> strategyGuesses = new ArrayList<>();
		strategyGuesses.addAll(robots);
		strategyGuesses.addAll(cartoonCharacters);
		strategyGuesses.addAll(bookCharacters);
		strategyGuesses.addAll(tvCharacters);
		strategyGuesses.addAll(superstars);
		strategyGuesses.addAll(politicians);	
		strategyGuesses.addAll(people);
		
		
		List<String> characters = List.of("Batman", "Superman", "Ivasyk Telesyk", "Lana del Rey");
		List<String> questions = List.of("Am i a human?", "Am i a character from a movie?");
		List<String> guessess = List.of("Batman", "Superman", "Ivasyk Telesyk", "Lana del Rey");
		
		Game game = new StrategyGame(characters);
		game.addPlayer(new RandomPlayer("Test1", questions, guessess));
		game.addPlayer(new StrategyPlayer("Test2", decisionTree, strategyGuesses));
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
