package com.eleks.academy.whoami.repository;

import java.util.Optional;
import java.util.stream.Stream;

import com.eleks.academy.whoami.core.SynchronousGame;

public interface GameRepository {

	Stream<SynchronousGame> findAllAvailable(String player);

	SynchronousGame save(SynchronousGame game);

	Optional<SynchronousGame> findById(String id);

}
