package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;

public class RandomGame implements Game {
	
	private Map<String, String> playersCharacter = new HashMap<>();
	private Queue<Player> players = new ConcurrentLinkedQueue<>();
	private List<String> availableCharacters;
	private Turn currentTurn;

	
	private final static String YES = "Yes";
	private final static String NO = "No";
	
	public RandomGame(List<String> availableCharacters) {
		this.availableCharacters = new ArrayList<String>(availableCharacters);
	}

	@Override
	public void addPlayer(Player player) {
		this.players.add(player);
	}

	@Override
	public boolean makeTurn() {
		if (players.stream().count() == 1){
			System.out.println("Game finished, winner is " + players.peek().getName());
			return false;
		}
		Player currentGuesser = currentTurn.getGuesser();
		Set<String> answers;
		System.out.println("Asking player " + currentGuesser.getName() + " if he is ready to guess...");
		if (currentGuesser.isReadyForGuess()) {
			System.out.println("Player " + currentGuesser.getName() + " chosen to guess...");
			String guess = currentGuesser.getGuess();
			answers = currentTurn.getOtherPlayers().stream()
					.map(a -> a.answerGuess(guess, this.playersCharacter.get(currentGuesser.getName()), currentGuesser.getName()))
					.collect(Collectors.toSet());
			HandleDisconnectedPlayers();
			long positiveCount = answers.stream().filter(a -> YES.toLowerCase().equals((a != null ? a : "").toLowerCase())).count();
			long negativeCount = answers.stream().filter(a -> NO.toLowerCase().equals((a != null ? a : "").toLowerCase())).count();
			
			boolean win = positiveCount > negativeCount;
			
			if (win) {
				players.remove(currentGuesser);
			}
			return win;
			
		} else {
			if (!currentGuesser.IsPlayerConnected()){
				System.out.println("Player " + currentGuesser.getName() + " has been kicked out due to lost network connection...");
				players.remove(currentGuesser);
				return true;
			}
			System.out.println("Player " + currentGuesser.getName() + " said he preffers to ask question...");
			String question = currentGuesser.getQuestion();
			System.out.println("Player " + currentGuesser.getName() + " asks: " + question +"?");
			answers = currentTurn.getOtherPlayers().parallelStream()
				.map(player -> player.answerQuestion(question, playersCharacter.get(currentGuesser.getName()), currentGuesser.getName()))
				.collect(Collectors.toSet());

			HandleDisconnectedPlayers();
			long positiveCount = answers.stream().filter(a -> YES.toLowerCase().equals((a != null ? a : "").toLowerCase())).count();
			long negativeCount = answers.stream().filter(a -> NO.toLowerCase().equals((a != null ? a : "").toLowerCase())).count();
			return positiveCount > negativeCount;
		}
		
	} 

	private void HandleDisconnectedPlayers() {
		var disconnectedPlayers = this.players.stream().filter(player -> !player.IsPlayerConnected());
		disconnectedPlayers.forEach(player -> {
			System.out.print("Player " + player.getName() + " has been kicked out due to lost network connection...");
			players.remove(player);
		});
	}

	@Override
	public void assignCharacters() {
		players.stream().forEach(player -> this.playersCharacter.put(player.getName(), this.getRandomCharacter()));
		
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
		int randomPos = (int)(Math.random() * this.availableCharacters.size()); 
		return this.availableCharacters.remove(randomPos);
	}

	@Override
	public void changeTurn() {
		this.currentTurn.changeTurn();
	}

}
