package com.eleks.academy.whoami.networking.server;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.RandomGame;
import com.eleks.academy.whoami.core.impl.RandomPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ServerImpl implements Server {

	private List<String> characters = List.of("Batman", "Superman");
	private List<String> questions = List.of("Am i a human?", "Am i a character from a movie?");
	private static final int DURATION = 2;
	private static final TimeUnit UNIT = TimeUnit.SECONDS;
	private final ServerSocket serverSocket;

	private RandomGame game = new RandomGame(characters);
	private final List<Socket> openSockets = new ArrayList<>();
	private List<String> guesses = List.of("Batman", "Superman");

	public ServerImpl(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
	}

	@Override
	public Game startGame() throws IOException {
		game.addPlayer(new RandomPlayer("Bot", characters, questions, guesses));
		System.out.println("Server starts");
		System.out.println("Waiting for a client connect....");
		return game;
	}

	@Override
	public Socket waitForPlayer(Game game) throws IOException {
		Socket player = serverSocket.accept();
		openSockets.add(player);
		return player;
	}

	@Override
	public void addPlayer(Player player) {
		String name = "";
		try {
			name = player.getName().get(DURATION, UNIT);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} catch (TimeoutException e) {
			System.err.printf("Player did not answer within %d %s%n", DURATION, UNIT);
		}
		if (!name.isBlank()) {
			game.addPlayer(player);
			System.out.println("Player: " + name + " Connected to the game!");
		}
	}

	@Override
	public void stopServer(Socket clientSocket, BufferedReader reader) throws IOException {
		clientSocket.close();
		reader.close();
	}

	public void stop() {
		for (Socket s : openSockets) {
			try {
				s.close();
			} catch (IOException e) {
				System.err.printf("Could not close a socket (%s)%n", e.getMessage());
			}
		}
	}

}