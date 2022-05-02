package com.eleks.academy.whoami.networking.server;

import java.io.IOException;

import com.eleks.academy.whoami.core.Game;

public interface Server {

    Game startGame() throws IOException;

    void waitForPlayer() throws IOException;

    void stop();

}
