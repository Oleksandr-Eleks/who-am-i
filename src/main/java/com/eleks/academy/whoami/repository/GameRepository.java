package com.eleks.academy.whoami.repository;

import com.eleks.academy.whoami.core.impl.PersistentGame;

import java.util.List;
import java.util.Optional;

public interface GameRepository {

    List<PersistentGame> findAllAvailable();

    PersistentGame save(PersistentGame game);

    Optional<PersistentGame> findById(String id);

    List<PersistentGame> findAllGames();

}
