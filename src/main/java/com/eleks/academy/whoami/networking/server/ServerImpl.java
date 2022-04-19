package com.eleks.academy.whoami.networking.server;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.card.Card;
import com.eleks.academy.whoami.core.card.Character;
import com.eleks.academy.whoami.core.impl.RandomGame;
import com.eleks.academy.whoami.networking.client.ClientPlayer;
import com.eleks.academy.whoami.networking.client.ConnectNewPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


public class ServerImpl implements Server {

    private Card cards = new Character();
    private RandomGame game;
    private final ServerSocket serverSocket;

    public ServerImpl(int port) throws IOException {
        cards.addCards(List.of("Batman", "Superman", "Spiderman", "Tor", "Wonder women"));
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public Game startGame() throws IOException {
        game = new RandomGame(cards.getCards());
        System.out.println("Server starts");
        System.out.println("Waiting for  minimum two a client connect....");
        while (game.getCountPlayer() < 2) {
            Socket socket = waitForPlayer(game);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String name = reader.readLine();
            addPlayer(new ClientPlayer(name, socket));
        }
        ConnectNewPlayer connectNewPlayer = new ConnectNewPlayer(this, game);
        Thread tr = new Thread(connectNewPlayer);
        tr.start();
        return game;
    }

    @Override
    public Socket waitForPlayer(Game game) throws IOException {
        return serverSocket.accept();
    }

    @Override
    public void addPlayer(Player player) {
        game.addPlayer(player);
        System.out.println("Player: " + player.getName() + " Connected to the game!");
    }

    @Override
    public void stopServer(Socket clientSocket, BufferedReader reader) throws IOException {
        serverSocket.close();
    }
}
