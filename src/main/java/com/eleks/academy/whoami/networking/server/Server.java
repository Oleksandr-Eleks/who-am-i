package com.eleks.academy.whoami.networking.server;

import java.io.IOException;
import java.net.Socket;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;

public interface Server {

	Game startGame() throws IOException;
	
	void waitForPlayers() throws IOException;

	void stopServer();
	
	void stop();
}
