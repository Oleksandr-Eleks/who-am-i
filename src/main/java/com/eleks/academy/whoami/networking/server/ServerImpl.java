package com.eleks.academy.whoami.networking.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.FairBotPlayer;
import com.eleks.academy.whoami.core.impl.RandomGame;

public class ServerImpl implements Server {

    private List<String> characters = List.of("Batman", "Superman", "Homelander", "Tor");
    private List<String> questions = List.of("Am i a human?", "Am i from DC universe?", "Am i a character from a movie?");
    private List<String> guessess = List.of("Batman", "Superman", "Homelander", "Tor");
    private List<Socket> openSockets = new ArrayList<>();
    private RandomGame game = new RandomGame(characters);
    static int playersCount = 0;
    static int livePlayers = 2;
    private final ServerSocket serverSocket;

    public ServerImpl(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public static boolean notEnoughPlayers() {
        return playersCount < livePlayers;
    }

    @Override
    public Game startGame() throws IOException {
        System.out.println("Server starts");
        System.out.println("Waiting for a client connect....");
        return game;
    }

    @Override
    public Socket waitForPlayer(Game game) throws IOException {
        playersCount++;
        Socket player = serverSocket.accept();
        openSockets.add(player);
        return player;
    }

    @Override
    public void addPlayer(Player player) {
        game.addPlayer(player);


    }

    @Override
    public void stopServer(Socket clientSocket, BufferedReader reader) throws IOException {
        clientSocket.close();
        reader.close();
    }

    public void stop() {
        for (Socket s : openSockets) {
            try {
                s.close();
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }
}
