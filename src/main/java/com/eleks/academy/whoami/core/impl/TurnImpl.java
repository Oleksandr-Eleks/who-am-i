package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Turn;
import com.eleks.academy.whoami.core.exception.TurnException;
import com.eleks.academy.whoami.enums.PlayerState;
import com.eleks.academy.whoami.enums.QuestionAnswer;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TurnImpl implements Turn {

    private List<PersistentPlayer> players;
    private final List<QuestionAnswer> playersAnswers = new ArrayList<>();
    private PersistentPlayer questioningPlayer;
    private Queue<PersistentPlayer> orderedPlayers;

    public TurnImpl(List<PersistentPlayer> players) {
        this.players = players;

        Function<PersistentPlayer, Integer> randomAuthorOrderComparator = value ->
                Double.valueOf(Math.random() * 1000).intValue();

        this.orderedPlayers =
                this.players
                        .stream()
                        .sorted(Comparator.comparing(randomAuthorOrderComparator))
                        .collect(Collectors.toCollection(LinkedList::new));

        this.questioningPlayer = this.orderedPlayers.poll();
    }

    public TurnImpl(List<PersistentPlayer> players, Queue<PersistentPlayer> orderedPlayers) {
        this.players = players;
        this.orderedPlayers = orderedPlayers;

        if (orderedPlayers.size() == 0) {
            throw new TurnException("No players left");
        }
        this.questioningPlayer = this.orderedPlayers.poll();
        if (questioningPlayer != null) {
            questioningPlayer.setPlayerState(PlayerState.ASK_QUESTION);
        }
    }

    @Override
    public PersistentPlayer getCurrentGuesser() {
        return this.questioningPlayer;
    }

    @Override
    public List<PersistentPlayer> getOtherPlayers() {
        return orderedPlayers.stream().toList();
    }

    @Override
    public List<QuestionAnswer> getPlayersAnswers() {
        if (playersAnswers.size() == players.size() - 1) {
            playersAnswers.clear();
        }
        return playersAnswers;
    }

    @Override
    public List<PersistentPlayer> getAllPlayers() {
        return players;
    }

    public List<PersistentPlayer> getPlayers() {
        return players;
    }

    public void setPlayersAnswers(QuestionAnswer answer) {
        playersAnswers.add(answer);
    }

    @Override
    public Turn changeTurn() {
        this.questioningPlayer.setPlayerState(PlayerState.ANSWER_QUESTION);
        this.orderedPlayers.add(this.questioningPlayer);
        return new TurnImpl(this.players, this.orderedPlayers);
    }

    @Override
    public void removePLayer(String playerId) {
        Optional<PersistentPlayer> playerToRemove = this.players
                .stream()
                .filter(randomPlayer -> randomPlayer.getId().equals(playerId))
                .findFirst();

        playerToRemove.ifPresent(this.players::remove);

        playerToRemove = this.orderedPlayers
                .stream()
                .filter(randomPlayer -> randomPlayer.getId().equals(playerId))
                .findFirst();
        playerToRemove.ifPresent(this.orderedPlayers::remove);

        if (questioningPlayer.getId().equals(playerId)) {
            playerToRemove = Optional.ofNullable(this.questioningPlayer);
            if (playerToRemove.isPresent()) {
                this.questioningPlayer = this.orderedPlayers.poll();
                this.questioningPlayer.setPlayerState(PlayerState.ASK_QUESTION);
            }
        }
    }

}
