package com.eleks.academy.whoami;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.client.ClientPlayer;
import com.eleks.academy.whoami.networking.server.ServerImpl;

public class App {

	public static void main(String[] args) throws IOException {

		final int players = 4;

		List<Socket> playersSockets = new ArrayList<>();

		BufferedReader reader = null;

		PrintStream writer = null;

		boolean gameStatus = true;

		ServerImpl server = new ServerImpl(8888);

		Game game = server.startGame();

		for (int i = 0; i < players; i++) {
			playersSockets.add(server.waitForPlayer(game));

			reader = new BufferedReader(new InputStreamReader(playersSockets.get(i).getInputStream()));

			writer = new PrintStream(playersSockets.get(i).getOutputStream());

			var playerName = reader.readLine();

			server.addPlayer(new ClientPlayer(playerName, playersSockets.get(i)));
			if (i < players - 1) {
				writer.println("Waiting players");
			}
			
		}

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

		server.stopServer(playersSockets, reader);

		writer.close();
	}

}
