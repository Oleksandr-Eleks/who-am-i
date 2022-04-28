package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class RandomGameTest {

    @Test
    void askAllPlayersForCharacterSuggestions() {
        Game game = new RandomGame(List.of("c"));
        TestPlayer p1 = new TestPlayer("P1");
        TestPlayer p2 = new TestPlayer("P2");
        game.addPlayer(p1);
        game.addPlayer(p2);

        game.assignCharacters();
        assertAll(() -> assertTrue(p1.suggested),
                () -> assertTrue(p2.suggested));
    }

    @Test
    void notAddedPlayerWhenFailedSuggestion() {
        Game game = new RandomGame(List.of("c"));
        TestPlayer p1 = new TestPlayer("P1");
        p1.character = "";
        game.addPlayer(p1);
        assertFalse(game.isPlayerContains(p1));
    }

    private static final class TestPlayer implements Player {
        private final String name;
        private boolean suggested;
        private String character;

        public TestPlayer(String name) {
            super();
            this.name = name;
        }

        @Override
        public Future<String> getName() {
            return CompletableFuture.completedFuture(name);
        }

        @Override
        public Future<String> suggestCharacter() {
            suggested = true;
            return CompletableFuture.completedFuture(character);
        }

        @Override
        public Future<String> getQuestion() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Future<String> answerQuestion(String question, String character) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Future<String> getGuess() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Future<Boolean> isReadyForGuess() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Future<String> answerGuess(String guess, String character) {
            throw new UnsupportedOperationException();
        }

    }

}
