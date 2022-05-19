package com.eleks.academy.whoami.service;

import com.eleks.academy.whoami.core.SynchronousGame;
import com.eleks.academy.whoami.core.exception.GameException;
import com.eleks.academy.whoami.core.impl.PersistentGame;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.repository.GameRepository;
import com.eleks.academy.whoami.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

	@Mock
	private GameRepository gameRepository;

	@InjectMocks
	private GameServiceImpl gameService;

	private final NewGameRequest gameRequest = new NewGameRequest();

	@BeforeEach
	public void setMockMvc() {
		gameRequest.setMaxPlayers(5);
	}

	@Test
	void findAvailableGames() {
		final String player = "player";
		SynchronousGame synchronousGame = new PersistentGame(player, gameRequest.getMaxPlayers());
		Stream<SynchronousGame> gameStream = Stream.of(synchronousGame);

		when(gameRepository.findAllAvailable(player)).thenReturn(gameStream);

		var games = gameService.findAvailableGames(player);

		assertNotNull(games);
		assertThat(games).isNotEmpty();
		assertNotNull(games.get(0).getId());
		assertNotNull(games.get(0).getStatus());
		assertNotNull(games.get(0).getPlayersInGame());

		verify(gameRepository, times(1)).findAllAvailable(eq(player));
	}

	@Test
	void findAvailableGamesEmpty() {
		final String player = "player";
		Stream<SynchronousGame> gameStream = Stream.empty();

		when(gameRepository.findAllAvailable(player)).thenReturn(gameStream);

		var games = gameService.findAvailableGames(player);

		assertThat(games).isEmpty();

		verify(gameRepository, times(1)).findAllAvailable(eq(player));
	}

	@Test
	void createGame() {
		final String player = "player";
		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());

		when(gameRepository.save(any(SynchronousGame.class))).thenReturn(game);

		var createdGame = gameService.createGame(player, gameRequest);

		assertNotNull(createdGame);
		assertNotNull(createdGame.getId());
		assertNotNull(createdGame.getStatus());

		verify(gameRepository, times(1)).save(any(SynchronousGame.class));
	}

	@Test
	void enrollToGame() {
		final String player = "player";
		final String player2 = "player2";
		final String id = "12345";
		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());
		Optional<SynchronousGame> op = Optional.of(game);

		when(gameRepository.findById(id)).thenReturn(op);

		gameService.enrollToGame(id, player2);

		assertThat(game.getPlayersInGame()).isEqualTo("2/5");

		verify(gameRepository, times(1)).findById(eq(id));
	}

	@Test
	void enrollToGameFailResponseStatusException() {
		final String player = "player";
		final String player2 = "player2";
		final String id = "12345";
		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());
		Optional<SynchronousGame> op = Optional.of(game);

		when(gameRepository.findById(id))
				.thenReturn(op)
				.thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot enroll to a game"));

		gameService.enrollToGame(id, player2);

		assertThrows(ResponseStatusException.class,
				() -> {
					gameService.enrollToGame(id, player);
				},
				"Cannot enroll to a game");

		verify(gameRepository, times(2)).findById(eq(id));
	}

	@Test
	void enrollToGameFailGameException() {
		final String player = "player";
		final String id = "12345";
		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());
		Optional<SynchronousGame> op = Optional.of(game);

		when(gameRepository.findById(id)).thenReturn(op);

		assertThrows(GameException.class,
				() -> {
					gameService.enrollToGame(id, player);
				});

		verify(gameRepository, times(1)).findById(eq(id));
	}

	@Test
	void findByIdAndPlayer() {
		final String player = "player";
		final String id = "12345";
		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());
		Optional<SynchronousGame> op = Optional.of(game);

		when(gameRepository.findById(id)).thenReturn(op);

		var foundGame = gameService.findByIdAndPlayer(id, player);

		assertNotNull(foundGame);
		assertNotNull(foundGame.get().getId());
		assertNull(foundGame.get().getCurrentTurn());

		verify(gameRepository, times(1)).findById(eq(id));
	}

	@Test
	void suggestCharacter() {
		final String player = "player";
		final String id = "12345";
		CharacterSuggestion character = new CharacterSuggestion("char");
		SynchronousGame game = new PersistentGame(player, gameRequest.getMaxPlayers());
		Optional<SynchronousGame> op = Optional.of(game);

		when(gameRepository.findById(id)).thenReturn(op);

		gameService.suggestCharacter(id, player, character);

		assertNotNull(character.getCharacter());
		assertEquals(character.getCharacter(), "char");

		verify(gameRepository, times(1)).findById(eq(id));
	}

}
