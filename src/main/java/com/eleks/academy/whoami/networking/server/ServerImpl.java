package com.eleks.academy.whoami.networking.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.RandomGame;

public class ServerImpl implements Server {

	private List<String> characters = List.of("Batman", "Superman", "Flash", "Human");

	private RandomGame game = new RandomGame(characters);

	private final ServerSocket serverSocket;

	public ServerImpl(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
	}

	public List<Player> getPlayers() {
		return this.game.getPlayers();
	}

	@Override
	public Game startGame() throws IOException {
		System.out.println("Server starts");
		System.out.println("Waiting for a client connect....");
		return this.game;
	}

	@Override
	public Socket waitForPlayer(Game game) throws IOException {
		return this.serverSocket.accept();
	}

	@Override
	public void addPlayer(Player player) {
		this.game.addPlayer(player);
		System.out.println("Player: " + player.getName() + " Connected to the game!");

	}

	@Override
	public void stopServer(List<Socket> clientSocket, BufferedReader reader, PrintStream writer) throws IOException {
		clientSocket.forEach(socket -> {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		reader.close();
		writer.close();
	}

}
