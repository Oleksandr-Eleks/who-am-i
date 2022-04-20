package com.eleks.academy.whoami.networking.server;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.RandomGame;
import com.eleks.academy.whoami.networking.client.ClientPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerImpl implements Server {

    public final int NUMBER_OF_CLIENTS = 2;
    private final ServerSocket serverSocket;
    private List<String> characters = List.of("Batman", "Superman", "Lark", "Splinter", "Dog");
    public RandomGame randomGame = new RandomGame(characters);

    public ServerImpl(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public Game startGame() throws IOException {
        System.out.println("Server starts");
        System.out.println("Waiting for a client connect....");
        randomGame = new RandomGame(characters);
        while (randomGame.countGamePlayers() < NUMBER_OF_CLIENTS) {
            var playerSocket = waitForPlayer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            var name = reader.readLine();
            randomGame.addPlayer(new ClientPlayer(name, playerSocket));
            System.out.println("Player: " + name + " Connected to the game!");
        }
        return randomGame;
    }

    @Override
    public Socket waitForPlayer() throws IOException {
        return serverSocket.accept();
    }

    @Override
    public void addPlayer(Player player) {
        randomGame.addPlayer(player);
    }

    @Override
    public void stopServer(Socket clientSocket, BufferedReader reader) throws IOException {
        clientSocket.close();
        reader.close();
    }
}
