package com.eleks.academy.whoami.repository.impl;

import com.eleks.academy.whoami.core.exception.GameNotFoundException;
import com.eleks.academy.whoami.core.impl.PersistentGame;
import com.eleks.academy.whoami.enums.GameStatus;
import com.eleks.academy.whoami.repository.GameRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class GameInMemoryRepository implements GameRepository {

    private final List<PersistentGame> games = new ArrayList<>();

    @Override
    public List<PersistentGame> findAllAvailable() {
        return this.games.stream()
                .filter(game -> game.getStatus().equals(GameStatus.WAITING_FOR_PLAYERS))
                .collect(Collectors.toList());
    }


    @Override
    public PersistentGame save(PersistentGame game) {
        this.games.add(game);
        return game;
    }

    @Override
    public Optional<PersistentGame> findById(String gameId) {
        return Optional.ofNullable(this.games
                .stream()
                .filter(game -> game.getGameId().equals(gameId))
                .findFirst()
                .orElseThrow(() -> new GameNotFoundException("Game not found!")));
    }

    @Override
    public List<PersistentGame> findAllGames() {
        return this.games;
    }

}
