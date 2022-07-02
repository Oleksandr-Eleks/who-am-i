package com.eleks.academy.whoami.repository;

import com.eleks.academy.whoami.core.impl.PersistentGame;

import java.util.List;

public interface GameRepository {

    List<PersistentGame> findAllAvailable();

    PersistentGame save(PersistentGame game);

    PersistentGame findById(String id);

}
