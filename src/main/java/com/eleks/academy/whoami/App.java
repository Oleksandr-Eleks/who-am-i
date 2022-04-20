package com.eleks.academy.whoami;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.server.ServerImpl;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {

        ServerImpl server = new ServerImpl(888);
        Game game = server.startGame();
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
    }
}
