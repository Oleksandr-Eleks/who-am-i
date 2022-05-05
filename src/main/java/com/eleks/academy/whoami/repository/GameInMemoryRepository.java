package com.eleks.academy.whoami.repository;

import com.eleks.academy.whoami.core.Game;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GameInMemoryRepository implements GameRepository {

	private final List<Game> games = new ArrayList<>();

	@Override
	public List<Game> findAllAvailable() {
		return this.games.stream()
				.filter(Game::isAvailable)
				.collect(Collectors.toList());
	}

}
