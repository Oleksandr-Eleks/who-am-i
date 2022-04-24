package com.eleks.academy.whoami;

import java.io.IOException;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.server.ServerImpl;

public class App {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		ServerImpl server = new ServerImpl(555);

		Game game = server.startGame();

		try {
			for(int i = 0; i < 3; i++) {
				var socket = server.waitForPlayers(game);
				System.out.println("Player connected, starting a identify process.");
				Thread thr = new Thread(new Runnable() {
					@Override
					public void run() {
						server.identify(socket);
					}
				});
				thr.start();
				System.out.println(thr.getName());
			}
//			Think about some improvement
			while (game.countPlayers() != 3) {	//<--- weak point
				Thread.sleep(1000);
			}
			game.init();
		} finally {
			System.out.println("Close sockets");
			server.stop();
		}
	}
}
