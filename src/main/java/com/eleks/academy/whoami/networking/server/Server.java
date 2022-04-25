package com.eleks.academy.whoami.networking.server;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public interface Server {

	Game startGame();

	Socket waitForPlayer() throws IOException;

	void addPlayer(Player player);

	void stopServer(Socket clientSocket, BufferedReader reader) throws IOException;

}
