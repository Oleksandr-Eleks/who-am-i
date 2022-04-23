package com.eleks.academy.whoami;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.client.ClientPlayer;
import com.eleks.academy.whoami.networking.server.ServerImpl;

public class App {

    /**
     * default : 3 players / it is possible to add a bot
     * or change the number of members in the ServiceImpl class variable
     */
    public static void main(String[] args) throws IOException {

        ServerImpl server = new ServerImpl(888);
        BufferedReader reader;
        List<Socket> pl = new ArrayList<>();
        List<Reader> readers = new ArrayList<>();
        Socket socket;
        try {

            Game game = server.startGame();

            socket = null;
            reader = null;
            while (ServerImpl.notEnoughPlayers()) {
                socket = server.waitForPlayer(game);
                pl.add(socket);

            }
            System.out.println("Waiting for " + pl.size() + " players to start!");
            for (int i = 0; i < pl.size(); i++) {
                reader = new BufferedReader(new InputStreamReader(pl.get(i).getInputStream()));
                var playerName = reader.readLine();
                server.addPlayer(new ClientPlayer(playerName, pl.get(i)));

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
        } finally {
            server.stop();
        }

    }

}
