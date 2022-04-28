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

	private List<String> characters = List.of("Batman", "Superman");
	private List<String> questions = List.of("Am i a human?", "Am i a character from a movie?");
	private List<String> guessess = List.of("Batman", "Superman");

	private final ServerSocket serverSocket;
	private final List<Player> clientPlayers;
	
	private final int players;

	public ServerImpl(int port, int players) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.players = players;
		this.clientPlayers = new ArrayList<>(players);
	}

	@Override
	public Game startGame() throws IOException {
		RandomGame game = new RandomGame(clientPlayers ,characters);
		game.initGame();
		return game;
	}

	@Override
	@PostConstruct
	public void waitForPlayer() throws IOException {
		System.out.println("Server starts");
		System.out.println("Waiting for a client connect....");
		for(int i = 0; i < players; i++) {
			ClientPlayer clientPlayer = new ClientPlayer(serverSocket.accept());
			clientPlayers.add(clientPlayer);
		}
		System.out.println(String.format("Got %d players. Starting a game.", players));
	}

	@Override
	@PreDestroy
	public void stop() {
		for(Player player: clientPlayers) {
			try {
				player.close();
			} catch (Exception e) {
				System.err.println(String.format("Could not close a socket (%s)", e.getMessage()));
			}
		}
	}

}
