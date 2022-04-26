package com.eleks.academy.whoami.networking.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.RandomGame;
import com.eleks.academy.whoami.core.impl.RandomPlayer;
import com.eleks.academy.whoami.networking.client.ClientPlayer;

public class ServerImpl implements Server {

	private List<String> characters = List.of("Batman", "Superman", "Dinosaur", "Cinderella");
	private List<String> questions = List.of("Am i a human?", "Am i a character from a movie?", "Am I a princess?", "Do I exist?", "Am I an animal?");
	private List<String> guessess = List.of("Batman", "Superman", "Dinosaur", "Cinderella");
	private List<Socket> clients = new ArrayList<>();
	private RandomGame game = new RandomGame(characters);

	private final ServerSocket serverSocket;

	public ServerImpl(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
	}

	@Override
	public Game startGame() throws IOException {
		game.addPlayer(new RandomPlayer("Me", questions, guessess));
		System.out.println("Server starts");
		System.out.println("Waiting for a client connect....");
		while (game.countPlayers() < 2) {
			Socket socket = waitForPlayer(game);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String playerName = bufferedReader.readLine();
			addPlayer(new ClientPlayer(playerName, socket));
		}
		return game;
	}

	public void identify(Socket socket) {
		try {
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			writer.println("Nickname:");
			writer.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			var nickname = reader.readLine();
			addPlayer(new ClientPlayer(nickname, socket));
		} catch (IOException e) {
			e.printStackTrace();
		}}

	@Override
	public Socket waitForPlayer(Game game) throws IOException {
		Socket player = serverSocket.accept();
		clients.add(player);
		return player;
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
