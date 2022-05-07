package com.eleks.academy.whoami.core.impl;

import java.util.ArrayList;
import java.util.List;

import com.eleks.academy.whoami.core.Player;

public class SmarterPlayer implements Player {

    private String name;
	private List<String> availableQuestions;
	private List<String> availableGuesses;
	
	public SmarterPlayer(String name, List<String> availableQuestions, List<String> availableGuesses) {
		this.name = name;
		this.availableQuestions = new ArrayList<String>(availableQuestions);
		this.availableGuesses = new ArrayList<String>(availableGuesses);
	}
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getQuestion() {
        var questionIndex = GetRandomValue(0, availableQuestions.size()-1);
        var question = availableQuestions.get(questionIndex);
        availableQuestions.remove(questionIndex);
        System.out.println("Player: " + name + ". Asks: " + question);
        return question;
    }

    @Override
    public String answerQuestion(String question, String character) {
        System.out.println("Player: " + name + ". Answers: NO" );
        return "No";
    }

    @Override
    public String getGuess() {
        int randomPos = (int)(Math.random() * this.availableGuesses.size()); 
		String guess = this.availableGuesses.remove(randomPos);
		System.out.println("Player: " + name + ". Guesses: Am I " + guess);
		return guess;
    }

    @Override
    public boolean isReadyForGuess() {
        return availableQuestions.isEmpty();
    }

    @Override
    public String answerGuess(String guess, String character) {
        System.out.println("Player: " + name + ". Answers: NO");
        return "No";
    }

    private int GetRandomValue(int from, int to){
        var rand = Math.random();
        var result = (int)(from + (1-rand)*(to-from));
        return result;
    }
    
}
