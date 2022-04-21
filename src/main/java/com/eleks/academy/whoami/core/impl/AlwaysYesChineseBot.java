package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.data.ChineseBotGuesses;
import com.eleks.academy.whoami.data.BotQuestion;
import com.eleks.academy.whoami.services.RandomizerService;

import java.util.List;

public class AlwaysYesChineseBot implements Player {

    private List<String> availableQuestions = BotQuestion.ChineseBotQuestions();
    private List<String> availableGuesses = ChineseBotGuesses.getChineseGuess();

    @Override
    public String getName() {
        return "Jian-Yang!";
    }

    private List<String> SecondChanse(){
        String secondChanse = RandomizerService.getRandomString(availableQuestions);
        if (secondChanse.isEmpty()){
            availableQuestions.add("Did I WIN?!");
        }
        return availableQuestions;
    }
    @Override
    public String getQuestion() {
        String question = RandomizerService.getRandomString(availableQuestions);
        System.out.println("Player: " + getName() + ". Asks: " + question);
        return question;
    }

    @Override
    public String answerQuestion(String question, String character) {
        String answer = "Yes";
        System.out.println("Player: " + getName() + ". Answers: " + answer);
        return answer;
    }

    @Override
    public String getGuess() {
        String guess = RandomizerService.getRandomString(availableGuesses);
        System.out.println("Player: " + getName() + ". Guesses: 我是吗 " + guess);
        return guess;
    }

    @Override
    public String answerGuess(String guess, String character) {
        String answer = "Yes";
        System.out.println("Player: " + getName() + ". Answers: " + answer);
        return answer;
    }

    @Override
    public boolean isReadyForGuess() {
        if (availableQuestions.isEmpty()){
            availableQuestions.addAll(SecondChanse());
        }
        return availableQuestions.isEmpty();
    }
}
