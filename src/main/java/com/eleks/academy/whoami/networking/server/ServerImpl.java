package com.eleks.academy.whoami.networking.server;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.impl.RandomGame;
import com.eleks.academy.whoami.core.impl.RandomPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ServerImpl implements Server {

    private List<String> characters = List.of("Batman", "Superman", "Ironman", "Spiderman", "Aquaman");
    private List<String> questions = List.of("Am i a human?", "Am i a character from a movie?", "Am i a superhero?");
    private List<String> guessess = List.of("Batman", "Superman", "Ironman", "Spiderman", "Aquaman");

    private RandomGame game = new RandomGame(characters);

    private final ServerSocket serverSocket;

    public ServerImpl(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public Game startGame() {
        System.out.println("Server starts");
        System.out.println("Waiting for a client connect....");
        return game;
    }

    @Override
    public LinkedList<Socket> waitForPlayer(Game game) throws IOException {
        game.addPlayer(new RandomPlayer("Bot", questions, guessess));
        LinkedList<Socket> serverList = new LinkedList<>();
        while (serverList.size() != 4) {
            serverList.add(serverSocket.accept());
        }
        return serverList;
    }

    @Override
    public void addPlayer(Player player) {
        game.addPlayer(player);
        System.out.println("Player: " + player.getName() + " Connected to the game!");
    }

    @Override
    public void stopServer(LinkedList<Socket> clientSocket, BufferedReader reader) throws IOException {
        clientSocket.forEach(socket -> {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        reader.close();
    }

}
