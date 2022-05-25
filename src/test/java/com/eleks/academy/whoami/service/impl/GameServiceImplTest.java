package com.eleks.academy.whoami.service.impl;

import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.model.response.GameLight;
import com.eleks.academy.whoami.repository.impl.GameInMemoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

class GameServiceImplTest {
    private final GameServiceImpl gameService = new GameServiceImpl(new GameInMemoryRepository());
    private final NewGameRequest gameRequest = new NewGameRequest();
    private MockMvc mockMvc;
    @BeforeEach
    public void setMockMvc() {
        gameRequest.setMaxPlayers(5);
    }

    @Test
    void findAvailableGames() {
        gameService.createGame("player",gameRequest);
        List<GameLight> games = gameService.findAvailableGames("player");
        Assertions.assertNotEquals(0, games.size());
    }

    @Test
    void createGame() {
        GameDetails gameDetails = gameService.createGame("player", gameRequest);
         Assertions.assertNotEquals("com/eleks/academy/whoami/core/state/WaitingForPlayers.java", gameDetails.getStatus());
    }
}