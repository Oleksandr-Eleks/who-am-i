package com.eleks.academy.whoami.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eleks.academy.whoami.core.SynchronousGame;
import com.eleks.academy.whoami.core.impl.PersistentGame;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.model.response.GameLight;
import com.eleks.academy.whoami.repository.impl.GameInMemoryRepository;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

	private final GameInMemoryRepository mockGameRepository = mock(GameInMemoryRepository.class);	
	private final GameServiceImpl gameService = new GameServiceImpl(mockGameRepository);
	private final NewGameRequest gameRequest = new NewGameRequest();
	
	@BeforeEach
	void set() {
		gameRequest.setMaxPlayers(3);
	}
	
	@Test
	void findAvailableGames() throws Exception {
		final String player = "player";
		Stream<SynchronousGame> games = Stream.empty();
		
		when(mockGameRepository.findAllAvailable(player)).thenReturn(games);
		
		List<GameLight> listOfGames = gameService.findAvailableGames(player);
		
		assertThat(listOfGames).isNotNull();
		assertThat(listOfGames).isEmpty();
		
		verify(mockGameRepository, times(1)).findAllAvailable(eq(player));
	}

	@Test
	void createGame() {
		final String player = "player";
		final var game = new PersistentGame(player, gameRequest.getMaxPlayers());
		
		when(mockGameRepository.save(game)).thenReturn(new PersistentGame(player, gameRequest.getMaxPlayers()));
		
		GameDetails createdGame = gameService.createGame(player, gameRequest);
		
		assertThat(createdGame).isNotNull();
		
		verify(mockGameRepository, times(1)).save(eq(game));
	}
}
