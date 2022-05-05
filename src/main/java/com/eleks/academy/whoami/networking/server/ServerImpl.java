package com.eleks.academy.whoami.networking.server;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.RandomGame;
import com.eleks.academy.whoami.networking.client.ClientPlayer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ServerImpl implements Server {

	private List<String> characters = List.of("Batman", "Superman");
	private List<String> questions = List.of("Am i a human?", "Am i a character from a movie?");
	private List<String> guessess = List.of("Batman", "Superman");

	private final ServerSocket serverSocket;
	private final List<Player> clientPlayers;

	private final int players;

	private final Thread serverThread;

	public ServerImpl(int port, int players) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.players = players;
		this.clientPlayers = new ArrayList<>(players);

		Runnable waitForPlayer = () -> {
			try {
				this.waitForPlayers();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		};

		this.serverThread = new Thread(waitForPlayer, "Server thread");
	}

	@Override
	public Game startGame() throws IOException {
		RandomGame game = new RandomGame(clientPlayers, characters);
		game.initGame();
		return game;
	}

	@Override
	@PostConstruct
	public void waitForPlayer() {
		this.serverThread.start();
	}

	@Override
	@PreDestroy
	public void stop() {
		for (Player player : clientPlayers) {
			try {
				player.close();
			} catch (Exception e) {
				System.err.printf("Could not close a socket (%s)%n", e.getMessage());
			}
		}

		try {
			this.serverSocket.close();
		} catch (IOException exception) {
			System.err.printf("Cannot close a server: %s%n", exception.getMessage());
		}

		this.serverThread.interrupt();
	}

	private void waitForPlayers() throws IOException {
		System.out.println("Server starts");
		System.out.println("Waiting for a client connect....");
		for (int i = 0; i < players; i++) {
			ClientPlayer clientPlayer = new ClientPlayer(serverSocket.accept());
			clientPlayers.add(clientPlayer);
		}
		System.out.printf("Got %d players. Starting a game.%n", players);
	}

}
