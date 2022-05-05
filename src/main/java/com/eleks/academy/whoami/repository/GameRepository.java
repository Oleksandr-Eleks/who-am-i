package com.eleks.academy.whoami.repository;

import com.eleks.academy.whoami.core.Game;

import java.util.List;

public interface GameRepository {

	List<Game> findAllAvailable();

}
