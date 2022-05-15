package com.eleks.academy.whoami.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.eleks.academy.whoami.configuration.GameControllerAdvice;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.service.impl.GameServiceImpl;


@ExtendWith(MockitoExtension.class)
class GameControllerTest {

	private final GameServiceImpl gameServiceMock = mock(GameServiceImpl.class);
	private final GameController gameContoroller = new GameController(gameServiceMock);
	private final NewGameRequest gameRequest = new NewGameRequest();
	private MockMvc mockMvc;

	@BeforeEach
	void setMockMvc() {
		mockMvc = MockMvcBuilders.standaloneSetup(gameContoroller)
				.setControllerAdvice(new GameControllerAdvice())
				.build();
		gameRequest.setMaxPlayers(3);
	}

	@Test
	void createGame() throws Exception {
		GameDetails gameDetails = new GameDetails();

		gameDetails.setId("12345");
		gameDetails.setStatus("WaitingForPlayers");

		when(gameServiceMock.createGame(eq("player"), any(NewGameRequest.class))).thenReturn(gameDetails);

		mockMvc.perform(MockMvcRequestBuilders.post("/games")
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\n" +
										"    \"maxPlayers\": 2\n" +
										"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id").value(gameDetails.getId()))
				.andExpect(jsonPath("status").value(gameDetails.getStatus()));
	}

	@Test
	void createGameFailedWithException() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/games")
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\n" +
										"    \"maxPlayers\": null\n" +
										"}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("{\"message\":\"Validation failed!\"," +
						"\"details\":[\"maxPlayers must not be null\"]}"));
	}

	@Test
	void findAvailableGames() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/games").header("X-Player", "player"))
			.andExpect(status().isOk());
		
		verify(gameServiceMock, times(1)).findAvailableGames(any(String.class));
	}

	@Test
	void findById() throws Exception {
		GameDetails gameDetails = new GameDetails();
		gameDetails.setId("12345");

		Optional<GameDetails> myOptional = Optional.of(gameDetails);

		when(gameServiceMock.findByIdAndPlayer(eq("12345"), eq("player"))).thenReturn(myOptional);

		mockMvc.perform(MockMvcRequestBuilders.get("/games/{id}", gameDetails.getId())
				.header("X-Player", "player"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").value(gameDetails.getId()));
		
		verify(gameServiceMock, times(1)).findByIdAndPlayer(eq("12345"), eq("player"));
	}

	@Test
	void findByIdFailedWithNotFound() throws Exception {
		Optional<GameDetails> myOptional = Optional.empty();

		when(gameServiceMock.findByIdAndPlayer(eq("54321"), eq("player"))).thenReturn(myOptional);

		mockMvc.perform(MockMvcRequestBuilders.get("/games/{id}", "54321")
				.header("X-Player", "player"))
				.andDo(print())
				.andExpect(status().isNotFound());
		
		verify(gameServiceMock, times(1)).findByIdAndPlayer(eq("54321"), eq("player"));
	}

	@Test
	void enrollToGame() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/games/{id}/players", "44444")
			.header("X-Player", "Enrollplayer"))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void suggestCharacter() throws Exception {
		doNothing().when(gameServiceMock).suggestCharacter(eq("1234"), eq("player"), any(CharacterSuggestion.class));

		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/games/{id}/characters", "1234")
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\n" +
										"    \"character\": \" char\"\n" +
										"}"))
				.andExpect(status().isOk());

		verify(gameServiceMock, times(1)).suggestCharacter(eq("1234"), eq("player"), any(CharacterSuggestion.class));
	}
}
