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
import com.eleks.academy.whoami.networking.client.ClientPlayer;

public class ServerImpl implements Server {

	private final ServerSocket serverSocket;
	private final List<Socket> clients = new ArrayList<>();
	private List<String> characters = List.of("Batman", "Superman", "Superwoman", "Robin", "Tanos");
	private Game game = new RandomGame(characters);


	public ServerImpl(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
	}

	@Override
	public Game startGame() throws IOException {
		System.out.println("Server starts\nWaiting for clients to connect...");
		return game;
	}

	@Override
	public Socket waitForPlayers(Game game) throws IOException {
		Socket player = serverSocket.accept();
		clients.add(player);
		return player;
	}

	public void identify(Socket socket) {
		try {
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			writer.println("Enter your name:");
			writer.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			var name = reader.readLine();
			// The same thing
			addPlayer(new ClientPlayer(name, socket));	//<--- weak point #2
		} catch (IOException e) {
			System.err.println("Identification of a client failed: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void addPlayer(Player player) {
		System.out.println("Player: " + player.getName() + " connected to the game! ");
		game.addPlayer(player);
	}

	@Override
	public void stopServer(Socket clientSocket, BufferedReader reader) throws IOException {
		clientSocket.close();
		reader.close();
	}

	public void stop() {
		for (Socket socket : clients) {
			try {
				socket.close();
			} catch (IOException e) {
				System.err.println(String.format("Could not close a socket (%s)", e.getMessage()));
			}
		}
	}

}
