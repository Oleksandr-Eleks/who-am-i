package com.eleks.academy.whoami.networking.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.RandomGame;
import com.eleks.academy.whoami.networking.client.ClientPlayer;

public class ServerImpl implements Server {

	private ServerSocket serverSocket;
	private final List<Player> clientPlayers;
	private int players;

	public ServerImpl(int port, int players) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.players = players;
		this.clientPlayers = new ArrayList<>(players);
	}

	@Override

	public Game startGame() {
		Game game = new RandomGame(clientPlayers);
		return game;
	}

	@Override
	@PostConstruct
	public void waitForPlayers() throws IOException {
		System.out.println("Server starts\nWaiting for a clients to connect....");
		do {
			ClientPlayer clientPlayer = new ClientPlayer(serverSocket.accept());
			clientPlayers.add(clientPlayer);
			System.out.println("Client connected...");
		} while(clientPlayers.size() != players);
		System.out.println(String.format("Got %d players. Starting a game.", players));
	}

	@PreDestroy
	public void stop() {
		System.out.println("Stop server...");
		for (Player player : clientPlayers) {
			try {
				player.close();
			} catch (Exception e) {
				System.err.println(String.format("Could not close a socket (%s)", e.getMessage()));
			}
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			System.err.println(String.format("Could not close a Serversocket (%s)", e.getMessage()));
		}
	}

}
