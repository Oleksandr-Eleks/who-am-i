package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class RandomGame implements Game {

    private Map<String, String> playersCharacter = new HashMap<>();
    private List<Player> players;
    private List<String> availableCharacters;
    private Turn currentTurn;

    private static final String YES = "YES";
    private static final String NO = "NO";

    public RandomGame(List<String> availableCharacters) {
        this.availableCharacters = new ArrayList<>(availableCharacters);
        players = new CopyOnWriteArrayList<>();
    }

    public int getCountPlayer() {
        return players.size();
    }

    @Override
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    @Override
    public boolean makeTurn() {
        Player currentGuesser = currentTurn.getGuesser();
        String name = currentGuesser.getName();
        List<String> answers = new CopyOnWriteArrayList<>();
        List<Thread> threads = new ArrayList<>();
        currentTurn.getOtherPlayers().forEach(player -> player.sendMessage("Wait question from " + name + "..."));
        if (currentGuesser.isReadyForGuess()) {
            String guess = currentGuesser.getGuess();
            currentGuesser.sendMessage("Wait answer from other players...");
            currentTurn.getOtherPlayers().forEach(player -> {
                Thread t = new Thread(() -> answers.add(player.answerGuess(name, guess, this.playersCharacter.get(name))));
                threads.add(t);
                t.start();
            });
            while (!threads.isEmpty()) {
                threads.removeIf(thread -> !thread.isAlive());
            }

            long positiveCount = answers.stream().filter(YES::equals).count();
            long negativeCount = answers.stream().filter(NO::equals).count();

            boolean win = positiveCount > negativeCount;
            if (win) {
                currentTurn.getOtherPlayers().forEach(player -> player.sendMessage(name + " is won!"));
                currentGuesser.sendMessage("You Won!");
                players.remove(currentGuesser);
                if (players.size() == 1) {
                    players.get(0).sendMessage("GAME OVER! You lost :(");
                }
            } else {
                currentGuesser.sendMessage("No");
            }
            return false;
        } else {
            String question = currentGuesser.getQuestion();
            currentGuesser.sendMessage("Wait answer from other players...");
            currentTurn.getOtherPlayers().forEach(player -> {
                Thread t = new Thread(() -> answers.add(player.answerQuestion(name, question, this.playersCharacter.get(name))));
                threads.add(t);
                t.start();
            });
            while (!threads.isEmpty()) {
                threads.removeIf(thread -> !thread.isAlive());
            }
            long positiveCount = answers.stream().filter(YES::equals).count();
            long negativeCount = answers.stream().filter(NO::equals).count();
            boolean yes = positiveCount > negativeCount;
            if (!yes) {
                currentGuesser.sendMessage("No");
            } else {
                currentGuesser.sendMessage("Yes");
            }
            return positiveCount > negativeCount;
        }
    }

    @Override
    public void assignCharacters() {
        players.forEach(player -> this.playersCharacter.put(player.getName(), this.getRandomCharacter()));

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
        int randomPos = (int) (Math.random() * this.availableCharacters.size());
        return this.availableCharacters.remove(randomPos);
    }

    @Override
    public void changeTurn() {
        this.currentTurn.changeTurn();
    }
}
