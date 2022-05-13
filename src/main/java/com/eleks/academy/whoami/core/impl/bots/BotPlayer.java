package com.eleks.academy.whoami.core.impl.bots;

import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.data.BotName;
import com.eleks.academy.whoami.data.BotQuestion;
import com.eleks.academy.whoami.data.GameCharacters;
import com.eleks.academy.whoami.service.impl.RandomizerService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class BotPlayer implements Player {
    private List<String> availableQuestions = BotQuestion.Questions();
    private List<String> availableGuesses = GameCharacters.Characters();
    private String botName = RandomizerService.getRandomString(BotName.getNames());
    @Override
    public Future<String> getName() {
        return CompletableFuture.completedFuture(this.botName);
    }

    @Override
    public Future<String> suggestCharacter() {
        String character = RandomizerService.getRandomString(GameCharacters.Characters());
        return CompletableFuture.completedFuture(character);
    }

    @Override
    public Future<String> getQuestion() {
        String question = RandomizerService.getRandomString(availableQuestions);
        System.out.println("Player: " + botName + ". Asks: " + question);
        return CompletableFuture.completedFuture(question);
    }
    @Override
    public Future<String> answerQuestion(String question, String character) {
        String answer = Math.random() > 0.5 ? "Yes" : "No";
        System.out.println("Player: " + botName + ". Answers: " + answer);
        return CompletableFuture.completedFuture(answer);
    }

    @Override
    public Future<String> getGuess() {
        String guess = RandomizerService.getRandomString(availableGuesses);
        System.out.println("Player: " + botName + ". Guesses: Am I " + guess);
        return CompletableFuture.completedFuture(guess);
    }

    @Override
    public Future<String> answerGuess(String guess, String character) {
        String answer = (character.equals(guess)) ? "Yes" : "No";
        System.out.println("Player: " + getName() + ". Answers: " + answer);
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
