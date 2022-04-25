package com.eleks.academy.whoami.networking.client;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConnectNewPlayer implements Runnable {
    private Server server;
    private Game game;

    public ConnectNewPlayer(Server server, Game game) {
        this.server = server;
        this.game = game;
    }

    @Override
    public void run() {
        Socket socket;
        BufferedReader reader;
        while (true) {
            try {
                socket = server.waitForPlayer(game);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String name = reader.readLine();
                server.addPlayer(new ClientPlayer(name, socket));
            } catch (IOException e) {
                System.out.println(e.getMessage());
                break;
            }
        }
    }
}
