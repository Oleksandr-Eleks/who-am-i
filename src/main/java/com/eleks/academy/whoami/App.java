package com.eleks.academy.whoami;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.client.ClientPlayer;
import com.eleks.academy.whoami.networking.server.ServerImpl;

public class App {
    
    public final static int CLIENTS_COUNT = 2;

	public static void main(String[] args) throws IOException {

		ServerImpl server = new ServerImpl(888);

		Game game = server.startGame();
		
		List<Socket> clientSockets = new ArrayList<Socket>(CLIENTS_COUNT);
        List<BufferedReader> clientBufferedReaders = new ArrayList<BufferedReader>(CLIENTS_COUNT);
		int start = 0;
		do { 
		    start++;
		    var socket = server.waitForPlayer(game);
		    clientSockets.add(socket);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        clientBufferedReaders.add(reader);
	        var playerName = reader.readLine();

	        server.addPlayer(new ClientPlayer(playerName, socket));
		} 
		while (start < CLIENTS_COUNT);
		
		boolean gameStatus = true;

		game.assignCharacters();

		game.initGame();

		while (gameStatus) {
			boolean turnResult = game.makeTurn();

			while (turnResult) {
				turnResult = game.makeTurn();
			}
			game.changeTurn();
			gameStatus = !game.isFinished();
		}

		
		for (int i = 0; i < clientSockets.size(); i++) {
		    server.stopServer(clientSockets.get(i), clientBufferedReaders.get(i));
		}
	}

}
