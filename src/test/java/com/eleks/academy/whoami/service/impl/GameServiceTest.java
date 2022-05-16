package com.eleks.academy.whoami.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eleks.academy.whoami.core.SynchronousGame;
import com.eleks.academy.whoami.model.response.GameLight;
import com.eleks.academy.whoami.repository.impl.GameInMemoryRepository;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

	private final GameInMemoryRepository mockGameRepository = mock(GameInMemoryRepository.class);	
	private final GameServiceImpl gameService = new GameServiceImpl(mockGameRepository);

	@Test
	void findAvailableGames() throws Exception {
		final String player = "player";
		final Supplier<Stream<SynchronousGame>> games = () -> Stream.empty();
		doReturn(games.get()).when(mockGameRepository).findAllAvailable(player);
		
		final List<GameLight> expected = gameService.findAvailableGames(player);
		assertThat(expected).isEqualTo(games.get().map(GameLight::of).toList());
		assertThat(expected).isEmpty();
		
		verify(mockGameRepository, times(1)).findAllAvailable(eq(player));
	}

	@Test
	void createGame() {
		
	}
}
