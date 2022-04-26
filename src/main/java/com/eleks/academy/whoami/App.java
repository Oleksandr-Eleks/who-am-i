package com.eleks.academy.whoami;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.client.ClientPlayer;
import com.eleks.academy.whoami.networking.server.ServerImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        int players = readPlayersArg(args);

        ServerImpl server = new ServerImpl(888);
        Game game = server.startGame();
        List<Socket> playerList = new ArrayList<>(players);
        try {
            for (int i = 0; i < players; i++) {
                var socket = server.waitForPlayer();
                playerList.add(socket);
                Thread parallelClientWorker = new Thread(() -> identifyPlayer(server, socket));
                parallelClientWorker.start();
                // Start a parallel thread to process a client
            }
            System.out.println(String.format("Got %d players. Starting a game.", players));

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

    private static void identifyPlayer(ServerImpl server, Socket socket) {
        try {
            PrintWriter toClient = new PrintWriter(socket.getOutputStream());
            toClient.println("Please, name yourself.");
            toClient.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            var playerName = reader.readLine();
            synchronized (server) {
                server.addPlayer(new ClientPlayer(playerName, socket));
            }
        } catch (IOException e) {
            System.err.println("Identification of a client failed: " + e.getMessage());
        }
    }

    private static int readPlayersArg(String[] args) {
        if (args.length < 1) {
            return 2;
        } else {
            try {
                int players = Integer.parseInt(args[0]);
                if (players < 2) {
                    return 2;
                } else if (players > 5) {
                    return 5;
                } else {
                    return players;
                }
            } catch (NumberFormatException e) {
                System.err.printf("Cannot parse number of players. Assuming 2. (%s)%n", e.getMessage());
                return 2;
            }
        }
    }
}
