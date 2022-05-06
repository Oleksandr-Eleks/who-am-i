package com.eleks.academy.whoami.networking.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.BotPlayer;
import com.eleks.academy.whoami.core.impl.RandomGame;
import com.eleks.academy.whoami.data.GameCharacters;

public class ServerImpl implements Server {
	private List<String> characters = GameCharacters.Characters();

	private static List<Socket> sockets;

	private RandomGame game = new RandomGame(characters);

	private final ServerSocket serverSocket;

	public ServerImpl(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
	}

	@Override
	public Game startGame() throws IOException {
		game.addPlayer(new BotPlayer());
		System.out.println("Server starts");
		System.out.println("Waiting for a clients to connect....");
		return game;
	}

	@Override
	public Socket waitForPlayer(Game game) throws IOException {
		return serverSocket.accept();
	}

	@Override
	public void addPlayer(Player player) {
		System.out.println("Player: " + player.getName() + " Connected to the game!");
	}

	@Override
	public void stopServer(Socket clientSocket, BufferedReader reader) throws IOException {
		clientSocket.close();
		reader.close();
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
