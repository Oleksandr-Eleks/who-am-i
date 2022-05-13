package com.eleks.academy.whoami.core.impl.bots;

import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.data.BotQuestion;
import com.eleks.academy.whoami.data.ChineseBotGuesses;
import com.eleks.academy.whoami.data.GameCharacters;
import com.eleks.academy.whoami.service.impl.RandomizerService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class AlwaysYesChineseBot implements Player {

    private List<String> availableQuestions = BotQuestion.ChineseBotQuestions();
    private List<String> availableGuesses = ChineseBotGuesses.getChineseGuess();
    private String name = "Jian-Yang!";

    @Override
    public Future<String> getName() {
        return CompletableFuture.completedFuture(this.name);
    }

    @Override
    public Future<String> suggestCharacter() {
        String character = RandomizerService.getRandomString(GameCharacters.Characters());
        return CompletableFuture.completedFuture(character);
    }

    @Override
    public Future<String> getQuestion() {
        String question = RandomizerService.getRandomString(availableQuestions);
        System.out.println(this.name + ". Asks: " + question);
        return CompletableFuture.completedFuture(question);
    }

    @Override
    public Future<String> answerQuestion(String question, String character) {
        String answer = "Yes";
        System.out.println(this.name + ". Answers: " + answer);
        return CompletableFuture.completedFuture(answer);
    }

    @Override
    public Future<String> getGuess() {
        String guess = RandomizerService.getRandomString(availableGuesses);
        System.out.println(this.name + ". Guesses: 我是吗 " + guess);
        return CompletableFuture.completedFuture(guess);
    }

    @Override
    public Future<String> answerGuess(String guess, String character) {
        String answer = "Yes";
        System.out.println(this.name + ". Answers: " + answer);
        return CompletableFuture.completedFuture(answer);
    }

    @Override
    public void close() {
    }

    @Override
    public Future<Boolean> isReadyForGuess() {
        return CompletableFuture.completedFuture(availableQuestions.isEmpty());
    }
}
