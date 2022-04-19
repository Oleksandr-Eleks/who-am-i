package com.eleks.academy.whoami.networking.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;

public interface Server {

    Game startGame() throws IOException;

    LinkedList<Socket> waitForPlayer(Game game) throws IOException;

    void addPlayer(Player player);

    void stopServer(LinkedList<Socket> clientSocket, BufferedReader reader) throws IOException;

}
