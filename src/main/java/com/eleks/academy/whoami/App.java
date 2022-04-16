package com.eleks.academy.whoami;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.client.ClientPlayer;
import com.eleks.academy.whoami.networking.server.ServerImpl;

public class App {

    public static void main(String[] args) throws IOException {

        ServerImpl server = new ServerImpl(888);

        Game game = server.startGame();

        Socket socket = null;
        BufferedReader reader = null;
        while (ServerImpl.notEnoughPlayers()) {
            socket = server.waitForPlayer(game);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            var playerName = reader.readLine();

            server.addPlayer(new ClientPlayer(playerName, socket));
        }
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

        server.stopServer(socket, reader);
    }

}
