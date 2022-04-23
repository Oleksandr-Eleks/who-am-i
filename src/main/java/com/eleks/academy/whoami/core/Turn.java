package com.eleks.academy.whoami.core;

import java.util.List;

public interface Turn {

    Player getGuesser();

    List<Player> getOtherPlayers();

    void changeTurn();

}
