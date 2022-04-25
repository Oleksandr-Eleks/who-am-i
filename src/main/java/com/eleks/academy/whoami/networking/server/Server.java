package com.eleks.academy.whoami.networking.server;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;

import java.io.IOException;
import java.net.Socket;

public interface Server {

    Game startGame() throws IOException;

    Socket waitForPlayer(Game game) throws IOException;

    void addPlayer(Player player);

    void stopServer() throws IOException;

}
