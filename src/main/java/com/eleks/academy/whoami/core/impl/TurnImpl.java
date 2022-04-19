package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.Turn;

import java.util.List;

public class TurnImpl implements Turn {

    private List<Player> players;
    private int currentPlayerIndex = 0;

    public TurnImpl(List<Player> players) {
        this.players = players;
    }

    @Override
    public Player getGuesser() {
        return this.players.get(currentPlayerIndex);
    }

    @Override
    public List<Player> getOtherPlayers() {
        return this.players.stream()
                .filter(player -> !player.getName().equals(getGuesser().getName())).toList();
    }

    @Override
    public void changeTurn() {
        this.currentPlayerIndex = currentPlayerIndex + 1 >= players.size() ? 0 : currentPlayerIndex + 1;
    }


}
