package com.eleks.academy.whoami.repository.impl;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.repository.GameRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Repository
public class GameInMemoryRepository implements GameRepository {

	private final Map<String, Game> games = new ConcurrentHashMap<>();

	@Override
	public Stream<Game> findAllAvailable(String player) {
		Predicate<Game> freeToJoin = Game::isAvailable;

		Predicate<Game> playersGame = game ->
				game.hasPlayer(player);

		return this.games.values()
				.stream()
				.filter(freeToJoin.or(playersGame));
	}

	@Override
	public Game save(Game game) {
		this.games.put(game.getId(), game);

		return game;
	}

	@Override
	public Optional<Game> findById(String id) {
		return Optional.ofNullable(this.games.get(id));
	}

}
