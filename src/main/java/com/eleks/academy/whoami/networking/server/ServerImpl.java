package com.eleks.academy.whoami.networking.server;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.RandomGame;
import com.eleks.academy.whoami.core.impl.RandomPlayer;
import com.eleks.academy.whoami.networking.client.ClientPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerImpl implements Server {

    private final ServerSocket serverSocket;
    private List<String> characters = List.of("Batman", "Superman", "Lark", "Splinter", "Dog");
    public RandomGame randomGame = new RandomGame(characters);
    private final List<Socket> openSockets = new ArrayList<>();

    public ServerImpl(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public Game startGame() throws IOException {
        System.out.println("Server starts");
        System.out.println("Waiting for a client connect....");
        return randomGame;
    }

    @Override
    public Socket waitForPlayer() throws IOException {
        Socket player = serverSocket.accept();
        openSockets.add(player);
        return player;
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

    public void stop() {
        for (Socket s : openSockets) {
            try {
                s.close();
            } catch (IOException e) {
                System.err.println(String.format("Could not close a socket (%s)", e.getMessage()));
            }
        }
    }
}
