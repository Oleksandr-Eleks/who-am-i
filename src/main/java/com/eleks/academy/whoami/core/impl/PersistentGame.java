package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.Player;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static com.eleks.academy.whoami.core.impl.GameStatus.SUGGESTING_CHARACTERS;
import static com.eleks.academy.whoami.core.impl.GameStatus.WAITING_FOR_PLAYERS;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class PersistentGame implements Game {

	private final String id;
	private final Integer maxPlayers;
	private final Map<String, Player> players = new ConcurrentHashMap<>();

	private final Map<String, List<GameCharacter>> suggestedCharacters = new ConcurrentHashMap<>();
	private final Map<String, String> playerCharacterMap = new ConcurrentHashMap<>();

	private final List<String> turns = new ArrayList<>();

	private GameStatus gameStatus;

	private Integer currentPlayerIndex;

	public PersistentGame(String hostPlayer, Integer maxPlayers) {
		this.id = String.format("%d-%d",
				Instant.now().toEpochMilli(),
				Double.valueOf(Math.random() * 999).intValue());

		this.maxPlayers = maxPlayers;

		this.players.put(hostPlayer, PersistentPlayer.of(hostPlayer));
		this.turns.add(hostPlayer);
		this.gameStatus = WAITING_FOR_PLAYERS;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public boolean isAvailable() {
		return WAITING_FOR_PLAYERS.equals(this.gameStatus);
	}

	@Override
	public boolean isReadyToStart() {
		final var statusApplicable = SUGGESTING_CHARACTERS.equals(this.gameStatus);

		final var enoughCharacters = Optional.of(this.suggestedCharacters)
				.map(Map::values)
				.stream()
				.mapToLong(Collection::size)
				.sum() >= this.players.size();

		return statusApplicable
				&& this.suggestedCharacters.size() > 1
				&& enoughCharacters;
	}

	@Override
	public boolean hasPlayer(String player) {
		return this.players.containsKey(player);
	}

	@Override
	public void addPlayer(String player) {
		this.players.put(player, PersistentPlayer.of(player));
		this.turns.add(player);

		Optional.of(this.players.size())
				.filter(playersSize -> playersSize.equals(this.maxPlayers))
				.ifPresent(then -> this.setStatus(GameStatus.SUGGESTING_CHARACTERS));
	}

	@Override
	public String getTurn() {
		return Optional.ofNullable(this.currentPlayerIndex)
				.map(this.turns::get)
				.orElse(null);
	}

	@Override
	public void startGame() {
		this.assignCharacters();

		this.setStatus(GameStatus.WAITING_FOR_QUESTION);

		this.currentPlayerIndex = 0;
	}

	@Override
	public void suggestCharacter(String player, String character) {
		List<GameCharacter> characters = this.suggestedCharacters.get(player);

		if (Objects.isNull(characters)) {
			final var newCharacters = new ArrayList<GameCharacter>();

			this.suggestedCharacters.put(player, newCharacters);

			characters = newCharacters;
		}

		characters.add(GameCharacter.of(character, player));
	}

	@Override
	public GameStatus getStatus() {
		return this.gameStatus;
	}

	@Override
	public String getPlayersInGame() {
		return String.format("%d/%d", this.players.size(), this.maxPlayers);
	}

	// TODO: Questions, answers
	// TODO: Guesses, answer
	// TODO: Game finished game
	// TODO: Drop unused methods

	@Override
	public boolean makeTurn() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void changeTurn() {

	}

	@Override
	public void initGame() {

	}

	@Override
	public void play() {

	}

	private void setStatus(GameStatus status) {
		this.gameStatus = status;
	}

	//TODO: Optimize
	private void assignCharacters() {
		final var allCharacters = this.suggestedCharacters
				.values()
				.stream()
				.flatMap(Collection::stream)
				.collect(toList());

		this.players.keySet()
				.forEach(player -> {
					final var assignedCharacter =
							allCharacters.stream()
									.filter(character -> !Objects.equals(character.getAuthor(), player))
									.collect(collectingAndThen(toList(), getRandomCharacter()));

					this.playerCharacterMap.put(player, assignedCharacter.getCharacter());

					allCharacters.remove(assignedCharacter);
				});
	}

	private Function<List<GameCharacter>, GameCharacter> getRandomCharacter() {
		return gameCharacters -> {
			int randomPos = (int) (Math.random() * gameCharacters.size());

			return gameCharacters.get(randomPos);
		};

	}
}
