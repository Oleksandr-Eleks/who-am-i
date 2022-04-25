package com.eleks.academy.whoami;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.impl.RandomPlayer;
import com.eleks.academy.whoami.networking.client.ClientPlayer;
import com.eleks.academy.whoami.networking.server.ServerImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class AppServer {

    public static void main(String[] args) throws IOException {

        ServerImpl server = new ServerImpl(888);
        Game game = server.startGame();
        game.addPlayer(new RandomPlayer("Petr1"));

        List<Socket> sockets = List.of(
                server.waitForPlayer(),
                server.waitForPlayer(),
                server.waitForPlayer(),
                server.waitForPlayer());

        List<BufferedReader> bufferedReaders = new ArrayList<>();
        for (Socket socket : sockets) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedReaders.add(bufferedReader);
            var playerName = bufferedReader.readLine();
            server.addPlayer(new ClientPlayer(playerName, socket));
        }

        game.assignCharacters();
        game.initGame();

        while (!game.isFinished()) {
            boolean turnResult = game.makeTurn();
            while (turnResult) {
                turnResult = game.makeTurn();
            }
            if (!game.isFinished()) {
                game.changeTurn();
            }
        }

        for (int i = 0; i < sockets.size(); i++) {
            server.stopServer(sockets.get(i), bufferedReaders.get(i));
        }
    }
}
