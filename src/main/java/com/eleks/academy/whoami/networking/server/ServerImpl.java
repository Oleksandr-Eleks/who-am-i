package com.eleks.academy.whoami.networking.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.RandomGame;
import com.eleks.academy.whoami.core.impl.RandomPlayer;
import com.eleks.academy.whoami.networking.client.ClientPlayer;

public class ServerImpl implements Server {

	private final ServerSocket serverSocket;
	private Game game;
	private List<String> characters = List.of("Batman", "Superman", "Superwoman", "Robin", "Tanos");
	private List<String> guesses = characters;
	private List<String> questions = List.of("Am i a human?", "Am i a character from a movie?", "Am i a male?",
			"Am i a female?");
	
	private List<Player> players = List.of(new RandomPlayer("Test-Player #1", questions, guesses),
			new RandomPlayer("Player-Bot #2", questions, guesses),
			new RandomPlayer("Bot-Player #3", questions, guesses),
			new RandomPlayer("Top-Player #4", questions, guesses),
			new RandomPlayer("Mid-Player #5", questions, guesses));
	
	public ServerImpl(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
	}

	@Override
	public Game startGame() throws IOException {
		game = new RandomGame(characters, players);
		System.out.println("SERVER STARTS\nWAITING FOR A PLAYERS...");

		while (game.countPlayers() < 5) {
			Socket socket = waitForPlayers(game);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String playerName = bufferedReader.readLine();
			addPlayer(new ClientPlayer(playerName, socket));
		}
		return game;
	}

	@Override
	public Socket waitForPlayers(Game game) throws IOException {
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
