package com.eleks.academy.whoami.core.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;

public class RandomGame implements Game {

    private Map<String, String> playersCharacter = new HashMap<>();
    private List<Player> players = new ArrayList<>();
    private List<String> availableCharacters;
    private Turn currentTurn;
    private List<Player> listOfWinners = new LinkedList<>();


    private final static String YES = "Yes";

    private final static String NO = "No";

    public RandomGame(List<String> availableCharacters) {
        this.availableCharacters = new ArrayList<String>(availableCharacters);
    }

    public List<Player> getListOfWinners() {
        return this.listOfWinners;
    }

    @Override
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    @Override
    public boolean makeTurn() {
        Player currentGuesser = currentTurn.getGuesser();
        Set<String> answers;
        if (currentGuesser.isReadyForGuess()) {
            String guess = currentGuesser.getGuess();
            answers = currentTurn.getOtherPlayers().stream()
                    .parallel()
                    .map(player -> player.answerGuess(guess, this.playersCharacter.get(currentGuesser.getName())))
                    .collect(Collectors.toSet());
            long positiveCount = answers.stream().filter(YES::equals).count();
            long negativeCount = answers.stream().filter(NO::equals).count();

            boolean win = positiveCount > negativeCount;

            if (win) {
                if (players.size() - 1 == currentTurn.getCurrentPlayerIndex())
                    currentTurn.setCurrentPlayerIndex(currentTurn.getCurrentPlayerIndex() - 1);
                listOfWinners.add(currentGuesser);
                players.remove(currentGuesser);

            }
            return win;

        } else {
            String question = currentGuesser.getQuestion();
            answers = currentTurn.getOtherPlayers().stream()
                    .parallel()
                    .map(player -> player.answerQuestion(question, this.playersCharacter.get(currentGuesser.getName())))
                    .collect(Collectors.toSet());
            long positiveCount = answers.stream().filter(YES::equalsIgnoreCase).count();
            long negativeCount = answers.stream().filter(NO::equalsIgnoreCase).count();

            return positiveCount > negativeCount;
        }

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
        int randomPos = (int) (Math.random() * this.availableCharacters.size());
        return this.availableCharacters.remove(randomPos);
    }

    @Override
    public void changeTurn() {
        this.currentTurn.changeTurn();
    }

}
