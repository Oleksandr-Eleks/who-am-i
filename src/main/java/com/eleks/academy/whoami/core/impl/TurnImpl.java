package com.eleks.academy.whoami.core.impl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.eleks.academy.whoami.core.Turn;
import com.eleks.academy.whoami.enums.PlayerState;
import com.eleks.academy.whoami.enums.QuestionAnswer;

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

        questioningPlayer.setPlayerState(PlayerState.ASK_QUESTION);

        this.questioningPlayer = this.orderedPlayers.poll();
    }

    @Override
    public PersistentPlayer getCurrentTurn() {
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

    public void setPlayersAnswers(QuestionAnswer answer) {
        playersAnswers.add(answer);
    }

    @Override
    public Turn changeTurn() {
        this.orderedPlayers.add(this.questioningPlayer);
        return new TurnImpl(this.players, this.orderedPlayers);

    }
}
