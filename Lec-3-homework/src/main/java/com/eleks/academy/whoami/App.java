package com.eleks.academy.whoami;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.client.ClientPlayer;
import com.eleks.academy.whoami.networking.server.ServerImpl;

public class App {

	public static void main(String[] args) throws IOException {
		BufferedReader consoleReader = new BufferedReader(
				new InputStreamReader(System.in));
		System.out.println("Enter Program Role (Server/Client)");
		String programRole = consoleReader.readLine();

		if (programRole.equalsIgnoreCase("server")) {
			System.out.println("Enter number of players");
			int numberOfPlayers = Integer.parseInt(consoleReader.readLine());
			ServerImpl server = new ServerImpl(888);

			Game game = server.startGame();
			boolean gameStatus = true;
			while (server.playersCount != numberOfPlayers) {
				var socket = server.waitForPlayer(game);

				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				var playerName = reader.readLine();

				server.addPlayer(new ClientPlayer(playerName, socket, server.questions));
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
			//new PrintStream(socket.getOutputStream()).println("Game Finidhed");
			//server.stopServer(socket, reader);
		} else {
			System.out.println("Client logic goes here");
		}
	}

}
