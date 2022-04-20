package com.eleks.academy.whoami.networking.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.RandomGame;

public interface Server {
	
	Game startGame() throws IOException;
	
	Socket waitForPlayer() throws IOException;
	
	void addPlayer(Player player);
	
	void stopServer(Socket clientSocket, BufferedReader reader) throws IOException;
	
}
