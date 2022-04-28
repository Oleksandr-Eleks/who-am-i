package com.eleks.academy.whoami;

import java.io.IOException;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.server.ServerImpl;

public class App {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		ServerImpl server = new ServerImpl(1919);

		try {
			Game game = server.startGame();
			game.init();
		} finally {
			server.stopServer();
			server.stop();
		}
	}
}
