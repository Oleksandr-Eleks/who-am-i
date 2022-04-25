package com.eleks.academy.whoami.networking.server;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.core.card.Card;
import com.eleks.academy.whoami.core.card.Character;
import com.eleks.academy.whoami.core.impl.RandomGame;
import com.eleks.academy.whoami.networking.client.ConnectNewPlayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ServerImpl implements Server {

    private static final long TIME_TO_WAIT_PLAYER = 300000;
    private Card cards = new Character();
    private RandomGame game;
    private final ServerSocket serverSocket;
    private List<Socket> sockets;

    public ServerImpl(int port) throws IOException {
        cards.addCards(List.of("Batman", "Superman", "Spiderman", "Tor", "Wonder women"));
        this.serverSocket = new ServerSocket(port);
        sockets = new ArrayList<>();
    }

    @Override
    public Game startGame() throws IOException {
        game = new RandomGame(cards.getCards());
        System.out.println("Server starts");
        System.out.println("Waiting for  minimum two a client connect....");

        Thread tr = new Thread(new ConnectNewPlayer(this, game));
        tr.setName("new player");
        tr.start();
        long sleep = System.currentTimeMillis() + TIME_TO_WAIT_PLAYER;
        while (System.currentTimeMillis() < sleep) {
            if (game.getCountPlayer() >= 2) {
                break;
            }
        }
        if (game.getCountPlayer() < 2) {
            System.out.println("Sorry, we did not wait for any of the players. Game will close.");
            stopServer();
            return null;
        }
        return game;
    }

    @Override
    public Socket waitForPlayer(Game game) throws IOException {
        Socket player = serverSocket.accept();
        sockets.add(player);
        return player;
    }

    @Override
    public void addPlayer(Player player) {
        game.addPlayer(player);
        System.out.println("Player: " + player.getName() + " Connected to the game!");
    }

    @Override
    public void stopServer() throws IOException {
        sockets.forEach(socket -> {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }
}
