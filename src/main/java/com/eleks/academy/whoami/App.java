package com.eleks.academy.whoami;

import java.io.IOException;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.server.ServerImpl;

public class App {

	public static void main(String[] args) throws IOException {

		ServerImpl server = new ServerImpl(888);

		Game game = server.startGame();

		game.init();
	}
}
