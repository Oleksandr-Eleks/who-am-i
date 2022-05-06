package com.eleks.academy.whoami;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.client.ClientPlayer;
import com.eleks.academy.whoami.networking.server.ServerImpl;

public class App {

	public static void main(String[] args) throws IOException {

		ServerImpl server = new ServerImpl(888);

		Game game = server.startGame();

		int maxPlayers = 3;

		List<Socket> playerList = new ArrayList<>(maxPlayers);
		List<BufferedReader> clientBR = new ArrayList<BufferedReader>(maxPlayers);

		try {
			for (int i = 0; i < maxPlayers; i++) {
				Socket socket = server.waitForPlayer(game);
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				clientBR.add(br);
				String name = br.readLine();
				game.addPlayer(new ClientPlayer(name, socket));
				playerList.add(socket);
			}
			System.out.println(String.format("%d Players connected", maxPlayers));

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
		} catch (Exception e){
			e.printStackTrace();
			server.stop();
		}
	}
}
