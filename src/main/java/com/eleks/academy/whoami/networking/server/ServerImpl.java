package com.eleks.academy.whoami.networking.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.FairBotPlayer;
import com.eleks.academy.whoami.core.impl.RandomGame;

public class ServerImpl implements Server {

	private List<String> characters = List.of("Batman", "Superman", "Homelander", "Tor");
	private List<String> questions = List.of("Am i a human?", "Am i from DC universe?", "Am i a character from a movie?");
	private List<String> guessess = List.of("Batman", "Superman", "Homelander", "Tor");

	private RandomGame game = new RandomGame(characters);
	static int playersCount = 0;
	private final ServerSocket serverSocket;

	public ServerImpl(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
	}
	public static boolean notEnoughPlayers() {
		return playersCount < 3;
	}
	@Override
	public Game startGame() throws IOException {
		game.addPlayer(new FairBotPlayer("Bot", questions, guessess));
		System.out.println("Server starts");
		System.out.println("Waiting for a client connect....");
		return game;
	}

	@Override
	public Socket waitForPlayer(Game game) throws IOException {
		playersCount++;
		return serverSocket.accept();
	}

	@Override
	public void addPlayer(Player player) {
		game.addPlayer(player);
		System.out.println("Player: " + player.getName() + " Connected to the game!");

	}

	@Override
	public void stopServer(Socket clientSocket, BufferedReader reader) throws IOException {
		clientSocket.close();
		reader.close();
	}

}
