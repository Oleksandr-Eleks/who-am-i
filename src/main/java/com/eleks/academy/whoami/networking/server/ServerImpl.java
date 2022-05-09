package com.eleks.academy.whoami.networking.server;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.BotPlayer;
import com.eleks.academy.whoami.core.impl.RandomGame;
<<<<<<< HEAD
import com.eleks.academy.whoami.data.GameCharacters;
=======
import com.eleks.academy.whoami.networking.client.ClientPlayer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
>>>>>>> fetch/feature/lecture-8

public class ServerImpl implements Server {
	private List<String> characters = GameCharacters.Characters();

	private static List<Socket> sockets;

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
<<<<<<< HEAD
		game.addPlayer(new BotPlayer());
		System.out.println("Server starts");
		System.out.println("Waiting for a clients to connect....");
=======
		RandomGame game = new RandomGame(clientPlayers, characters);
		game.initGame();
>>>>>>> fetch/feature/lecture-8
		return game;
	}

	@Override
	@PostConstruct
	public void waitForPlayer() {
		this.serverThread.start();
	}

	@Override
<<<<<<< HEAD
	public void addPlayer(Player player) {
		System.out.println("Player: " + player.getName() + " Connected to the game!");
=======
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
>>>>>>> fetch/feature/lecture-8
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

	public void stop() {
		for (Socket s : sockets) {
			try {
				s.close();
			} catch (IOException e) {
				throw new RuntimeException();
			}
		}
	}
}
