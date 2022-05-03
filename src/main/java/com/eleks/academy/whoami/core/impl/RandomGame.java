package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;
import com.eleks.academy.whoami.exception.ObtainPlayerNameException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class RandomGame implements Game {

    private static final int DURATION = 2;
    private static final TimeUnit UNIT = TimeUnit.MINUTES;
    private final static String YES = "Yes";
    private final static String NO = "No";
    private final List<Player> players;
    private final List<String> availableCharacters;
    private Map<String, String> playersCharacter = new ConcurrentHashMap<>();
    private Turn currentTurn;

    public RandomGame(List<Player> players, List<String> availableCharacters) {
        this.availableCharacters = new ArrayList<String>(availableCharacters);
        this.players = new ArrayList<>(players.size());
        players.forEach(this::addPlayer);
    }

    private void addPlayer(Player player) {
        // TODO: Add test to ensure that player has not been added to the lists when failed to obtain suggestion
        Future<String> maybeCharacter = player.suggestCharacter();
        try {
            String character = maybeCharacter.get(DURATION, UNIT);
            this.players.add(player);
            this.availableCharacters.add(character);
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.err.println("Player did not suggest a charatern within %d %s".formatted(DURATION, UNIT));
        }
    }

    @Override
    public boolean makeTurn() {
        Player currentGuesser = currentTurn.getGuesser();
        Set<Future<String>> answers;
        String guessersName;
        try {
            guessersName = currentGuesser.getName().get(DURATION, UNIT);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            // TODO: Add custom runtime exception implementation
            throw new ObtainPlayerNameException("Failed to obtain a player's name");
        }
        Future<Boolean> isReadyForGuess = currentGuesser.isReadyForGuess();
        long positiveCount = 0;
        long negativeCount = 0;
        try {
            if (isReadyForGuess.get(DURATION, UNIT)) {
                String guess;
                boolean win = false;
                try {
                    guess = currentGuesser.getGuess().get(DURATION, UNIT);
                    answers = currentTurn.getOtherPlayers().stream()
                            .map(player -> player.answerGuess(guess, this.playersCharacter.get(guessersName)))
                            .collect(Collectors.toSet());
                    positiveCount = answers.stream().filter(YES::equals).count();
                    negativeCount = answers.stream().filter(NO::equals).count();

                    win = positiveCount > negativeCount;

                    if (win) {
                        players.remove(currentGuesser);
                    }

                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }

                return win;
            } else {
                Future<String> maybeQuestion = currentGuesser.getQuestion();
                String question;

                try {
                    question = maybeQuestion.get(DURATION, UNIT);
                    answers = currentTurn.getOtherPlayers().stream()
                            .map(player -> player.answerQuestion(question, this.playersCharacter.get(guessersName)))
                            .collect(Collectors.toSet());
                    positiveCount = answers.stream().filter(YES::equals).count();
                    negativeCount = answers.stream().filter(NO::equals).count();
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            e.printStackTrace();
        }

        return positiveCount > negativeCount;
    }

    private void assignCharacters() {
        players.stream().map(Player::getName).parallel().map(f -> {
            // TODO: extract into a configuration parameters
            try {
                return f.get(DURATION, UNIT);
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                // TODO: Add custom runtime exception implementation
                throw new ObtainPlayerNameException("Failed to obtain a player's name");
            } catch (TimeoutException e) {
                // TODO: Choose a name from a pool of names, i.e. Anonymous Badger etc.
                throw new RuntimeException("Player did not provide a name within %d %s".formatted(DURATION, UNIT));
            }
        }).forEach(name -> this.playersCharacter.put(name, this.getRandomCharacter()));

    }

    @Override
    public void initGame() {
        this.assignCharacters();
        this.currentTurn = new TurnImpl(this.players);
    }


    @Override
    public boolean isFinished() {
        return players.size() == 1;
    }

    private String getRandomCharacter() {
        int randomPos = (int) (Math.random() * this.availableCharacters.size());
        // TODO: Ensure player never receives own suggested character
        return this.availableCharacters.remove(randomPos);
    }

    @Override
    public void changeTurn() {
        this.currentTurn.changeTurn();
    }

    @Override
    public void play() {
        boolean gameStatus = true;

        while (gameStatus) {
            boolean turnResult = this.makeTurn();

            while (turnResult) {
                turnResult = this.makeTurn();
            }
            this.changeTurn();
            gameStatus = !this.isFinished();
        }
    }

}
