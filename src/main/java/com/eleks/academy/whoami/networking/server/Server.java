package com.eleks.academy.whoami.networking.server;

import com.eleks.academy.whoami.core.Game;

import java.io.IOException;

public interface Server {
    Game startGame() throws IOException;

    void waitForPlayer();

    void stop();
}
