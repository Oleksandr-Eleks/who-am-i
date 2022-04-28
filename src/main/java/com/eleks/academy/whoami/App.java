package com.eleks.academy.whoami;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.eleks.academy.whoami.configuration.ContextConfig;
import com.eleks.academy.whoami.configuration.ServerProperties;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.client.ClientPlayer;
import com.eleks.academy.whoami.networking.server.ServerImpl;

public class App {

	public static void main(String[] args) throws IOException {
		ApplicationContext context = new AnnotationConfigApplicationContext(ContextConfig.class);
		ServerProperties properties = context.getBean(ServerProperties.class);
		
		int players = readPlayersArg(args);

		ServerImpl server = new ServerImpl(properties.getPort());

		Game game = server.startGame();

		List<ClientPlayer> playerList = new ArrayList<>(players);
		try {
			for (int i = 0; i < players; i++) {
				var socket = server.waitForPlayer(game);
				ClientPlayer player = new ClientPlayer(socket);
				playerList.add(player);
				server.addPlayer(player);
			}
			System.out.println(String.format("Got %d players. Starting a game.", players));

			boolean gameStatus = true;
			game.assignCharacters();
			game.initGame();
			while (gameStatus) {
				boolean turnResult = game.makeTurn();

				while (turnResult) {
					turnResult = game.makeTurn();
				}
				game.changeTurn();
				gameStatus = !game.isFinished();
			}
		} finally {
			server.stop();
			for (ClientPlayer clientPlayer : playerList) {
				try {
					clientPlayer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static int readPlayersArg(String[] args) {
		if (args.length < 1) {
			return 2;
		} else {
			try {
				int players = Integer.parseInt(args[0]);
				if (players < 2) {
					return 2;
				} else if (players > 5) {
					return 5;
				} else {
					return players;
				}
			} catch (NumberFormatException e) {
				System.err.printf("Cannot parse number of players. Assuming 2. (%s)%n", e.getMessage());
				return 2;
			}
		}
	}

}
