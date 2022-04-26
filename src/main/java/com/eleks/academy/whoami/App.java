package com.eleks.academy.whoami;

import java.io.IOException;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.server.ServerImpl;

public class App {

	public static void main(String[] args) throws IOException {

		ServerImpl server = new ServerImpl(888);

		Game game = server.startGame();

		final int amountOfPlayers = 4;

		try {
			for(int i = 0; i < amountOfPlayers; i++) {
				var socket = server.waitForPlayer(game);
				System.out.println("Player is connected!");
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						server.identify(socket);
					}
				});
				thread.start();
				System.out.println(thread.getName());

	}

} catch (IOException e) {
			e.printStackTrace();
		}
	}
}