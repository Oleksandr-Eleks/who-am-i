package com.eleks.academy.whoami;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.client.ClientPlayer;
import com.eleks.academy.whoami.networking.server.ServerImpl;

public class App {

	public static void main(String[] args) throws IOException {

		ServerImpl server = new ServerImpl(888);

		Game game = server.startGame();

		var socket = server.waitForPlayer(game);

		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		boolean gameStatus = true;

		var playerName = reader.readLine();
		var playerName2 = reader.readLine();

		server.addPlayer(new ClientPlayer(playerName, socket));
		server.addPlayer(new ClientPlayer(playerName2, socket));

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

		server.stopServer(socket, reader);
	}

}
