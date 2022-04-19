package com.eleks.academy.whoami;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.client.ClientPlayer;
import com.eleks.academy.whoami.networking.server.ServerImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {

    public static BufferedReader reader;

    public static void main(String[] args) throws IOException {

        ServerImpl server = new ServerImpl(1100);

        Game game = server.startGame();

        var sockets = server.waitForPlayer(game);
        for (var socket : sockets) {
            System.out.println(socket);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            var playerName = reader.readLine();

            server.addPlayer(new ClientPlayer(playerName, socket));

        }
        game.assignCharacters();

        boolean gameStatus = true;

        game.initGame();

        while (gameStatus) {
            boolean turnResult = game.makeTurn();

            while (turnResult) {
                turnResult = game.makeTurn();
            }
            game.changeTurn();
            gameStatus = !game.isFinished();
        }

        server.stopServer(sockets, reader);
    }

}
