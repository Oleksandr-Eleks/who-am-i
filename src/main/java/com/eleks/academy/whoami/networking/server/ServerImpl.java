package com.eleks.academy.whoami.networking.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.RandomGame;

public class ServerImpl implements Server {

	private ServerSocket serverSocket;
	private final List<Socket> clients = new ArrayList<>();
	private List<String> characters = List.of("Batman", "Superman", "Superwoman", "Robin", "Tanos");
	private Game game;
	final int playersNumb = 2;

	public ServerImpl(int port) {
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Game startGame() {
		game = new RandomGame();
		
		return game;
	}

	@Override
	public Socket waitForPlayers(Game game) throws IOException {
		Socket player = serverSocket.accept();
		clients.add(player);
		return player;
	}

	@Override
	public void addPlayer(Player player) {
		System.out.println("Player: " + player.getName() + " connected to the game! ");
		game.addPlayer(player);
	}

	@Override
	public void stopServer() {
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (Exception e) {
			System.err.println("server.close failed: " + e.getMessage());
		}
	}
	
	@Override
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
