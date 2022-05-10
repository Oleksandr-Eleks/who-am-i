package com.eleks.academy.whoami.repository;

import com.eleks.academy.whoami.core.Game;

import java.util.Optional;
import java.util.stream.Stream;

public interface GameRepository {

	Stream<Game> findAllAvailable(String player);

	Game save(Game game);

	Optional<Game> findById(String id);

}
