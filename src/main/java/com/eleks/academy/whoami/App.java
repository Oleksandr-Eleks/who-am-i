package com.eleks.academy.whoami;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.client.ClientPlayer;
import com.eleks.academy.whoami.networking.server.ServerImpl;

public class App {

    /**
     * default : 3 players / it is possible to add a bot(random or who know about your role)
     * or change the number of members in the ServiceImpl class variable
     */
    public static void main(String[] args) throws IOException {
        int players = readPlayersArgs(args);
        ServerImpl server = new ServerImpl(888);
        List<Socket> pl = new ArrayList<>(players);
        System.out.println("Waiting for " + players + " players to start!");
        try {
            Game game = server.startGame();
            for (int i = 0; i < players; i++) {
                var socket = server.waitForPlayer(game);
                pl.add(socket);
                Thread thread = new Thread(() -> identifyPlayers(server, socket));
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
            System.out.println("Game is ended. First successful guesser is : " +
                    "" + game.getListOfWinners().get(0).getName());
        } finally {
            server.stop();
        }

    }

    private static int readPlayersArgs(String[] args) {
        if (args.length < 1) {
            return 3;
        } else {
            try {
                return Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Cannot parse number of players. Assume 2.");
                return 3;
            }
        }
    }

    public static void identifyPlayers(ServerImpl server, Socket socket) {
        try {
            PrintWriter toClient = new PrintWriter(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            toClient.flush();
            var playerName = reader.readLine();
            System.out.println("Player: " + playerName + " Connected to the game!");
            synchronized (server) {
                server.addPlayer(new ClientPlayer(playerName, socket));
            }
        } catch (IOException e) {
            System.err.println("Identification of a client failed " + e.getMessage());
        }
    }

}
