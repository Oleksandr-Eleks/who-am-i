package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.List;

import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.utility.Tree;
import com.eleks.academy.whoami.utility.impl.DecisionTree;

public class StrategyPlayer implements Player{

	private String name;
	private Tree strategy;
	boolean isReadyForGuess = false;
	String lastAnswer;
	
	
	private List<String> availableGuesses = new ArrayList<>();
	private List<String> allGuesses;
	
	public StrategyPlayer(String name, Tree strategy, List<String> allGuesses) {
		this.name = name;
		this.strategy = strategy;
		this.allGuesses = allGuesses;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getQuestion() {
		String result = strategy.getData();
		if(this.lastAnswer!=null) {
		    if(this.lastAnswer.equalsIgnoreCase("no")) {
			    Tree N = this.strategy.getLeft().getLeft();
			    Tree Y = this.strategy.getLeft().getRight();
			    String data = this.strategy.getLeft().getData();
			    this.strategy = new DecisionTree(data, N, Y);
			    System.out.println(strategy.getData()+"1");
		    }
		    else if (this.lastAnswer.equalsIgnoreCase("yes")) {
			    Tree N = this.strategy.getRight().getLeft();
			    Tree Y = this.strategy.getRight().getRight();
			    String data = this.strategy.getRight().getData();
			    this.strategy = new DecisionTree(data, N, Y);
			    System.out.println(strategy.getData()+"2");
		    }	
		}
		System.out.println("Player: " + name + ". Asks: " + strategy.getData());
		lastAnswer = null;
		if(strategy.getData().contains("random")) {
			isReadyForGuess = true;
			boolean toAdd = false;
			loop: for(String s : allGuesses) {
					if(s.equalsIgnoreCase(strategy.getData())){
						toAdd = true;
					}
					else if(toAdd && s.contains("random") && !s.equals(strategy.getData())){
						break loop;
					}
					if(toAdd) {
						availableGuesses.add(s);
					}
			availableGuesses.remove(strategy.getData());
			}
		}
		return result;
	}


	@Override
	public String answerQuestion(String question, String character) {
		String answer = Math.random() < 0.5 ? "Yes" : "No";
		System.out.println("Player: " + name + ". Answers: " + answer);
		return answer;
		
	}

	@Override
	public String getGuess() {
		try {
			int randomPos = (int)(Math.random() * this.availableGuesses.size()); 
			String guess = this.availableGuesses.remove(randomPos);
			System.out.println("Player: " + name + ". Guesses: Am I " + guess);
			return guess;
		}
		catch(IllegalStateException e) {
			return null;
		}
	}

	public String getLastAnswer() {
		return lastAnswer;
	}

	public void setLastAnswer(String lastAnswer) {
		this.lastAnswer = lastAnswer;
	}
	
	@Override
	public boolean isReadyForGuess() {
		System.out.println(strategy.getData());
		System.out.println(strategy.getData().contains("random"));
		System.out.println(lastAnswer);
		return strategy.getData().contains("random")? true : isReadyForGuess;
	}

	@Override
	public String answerGuess(String guess, String character) {
		String answer = Math.random() < 0.5 ? "Yes" : "No";
		System.out.println("Player: " + name + ". Answers: " + answer);
		return answer;
	}

}
