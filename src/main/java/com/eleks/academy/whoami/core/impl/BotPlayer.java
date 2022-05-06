package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.data.*;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.services.RandomizerService;

import java.util.List;

public class BotPlayer implements Player {
    private List<String> availableQuestions = BotQuestion.Questions();
    private List<String> availableGuesses = GameCharacters.Characters();
    private String botName = RandomizerService.getRandomString(BotName.getNames());
    @Override
    public String getName() {
        return botName;
    }

    @Override
    public String getQuestion() {
        String question = RandomizerService.getRandomString(availableQuestions);
        System.out.println(botName + ". Asks: " + question + "\t");
        return question;
    }
    @Override
    public String answerQuestion(String question, String character) {
        String answer = Math.random() > 0.5 ? "Yes" : "No";
        System.out.println(botName + ". Answers: " + answer + "\t");
        return answer;
    }

    @Override
    public String getGuess() {
        String guess = RandomizerService.getRandomString(availableGuesses);
        System.out.println(botName + ". Guesses: Am I " + guess + "\t");
        return guess;
    }

    @Override
    public String answerGuess(String guess, String character) {
        String answer = (character.equals(guess)) ? "Yes" : "No";
        System.out.println(botName + ". Answers: " + answer + "\t");
        return answer;
    }

    @Override
    public boolean isReadyForGuess() {
        return availableQuestions.isEmpty();
    }

}
