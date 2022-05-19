package com.eleks.academy.whoami.controller;

import com.eleks.academy.whoami.configuration.GameControllerAdvice;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

	private final GameServiceImpl gameService = mock(GameServiceImpl.class);
	private final GameController gameController = new GameController(gameService);
	private final NewGameRequest gameRequest = new NewGameRequest();
	private MockMvc mockMvc;

	@BeforeEach
	public void setMockMvc() {
		mockMvc = MockMvcBuilders.standaloneSetup(gameController)
				.setControllerAdvice(new GameControllerAdvice()).build();
		gameRequest.setMaxPlayers(5);
	}

	@Test
	void findAvailableGames() throws Exception {
		this.mockMvc.perform(
						MockMvcRequestBuilders.get("/games")
								.header("X-Player", "player"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0]").doesNotHaveJsonPath());
	}

	@Test
	void createGame() throws Exception {
		GameDetails gameDetails = new GameDetails();
		gameDetails.setId("12613126");
		gameDetails.setStatus("WaitingForPlayers");

		when(gameService.createGame(eq("player"), any(NewGameRequest.class))).thenReturn(gameDetails);

		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/games")
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\n" +
										"    \"maxPlayers\": 2\n" +
										"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id").value(gameDetails.getId()))
				.andExpect(jsonPath("status").value("WaitingForPlayers"));
	}

	@Test
	void createGameFailedWithException() throws Exception {
		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/games")
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
	void findById() throws Exception {
		String id = "1234";
		GameDetails gameDetails = new GameDetails();
		gameDetails.setId(id);
		Optional<GameDetails> op = Optional.of(gameDetails);

		when(gameService.findByIdAndPlayer(gameDetails.getId(), "player")).thenReturn(op);

		this.mockMvc.perform(
						MockMvcRequestBuilders.get("/games/{id}", gameDetails.getId())
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").value("1234"));

		verify(gameService, times(1)).findByIdAndPlayer("1234", "player");
	}

	@Test
	void findByIdFailedNotFound() throws Exception {
		String id = "1234";

		this.mockMvc.perform(
						MockMvcRequestBuilders.get("/games/{id}", id)
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		verify(gameService, times(1)).findByIdAndPlayer(id, "player");
	}

	@Test
	void enrollToGame() throws Exception {
		String id = "1234";

		doNothing().when(gameService).enrollToGame(eq(id), eq("player"));

		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/games/{id}/players", id)
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		verify(gameService, times(1)).enrollToGame(eq(id), eq("player"));
	}

	@Test
	void enrollToGameFailConnection() throws Exception {
		String id = "1234";

		doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot enroll to a game"))
				.when(gameService).enrollToGame(eq(id), eq("player"));

		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/games/{id}/players", id)
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		verify(gameService, times(1)).enrollToGame(eq(id), eq("player"));
	}

	@Test
	void suggestCharacter() throws Exception {
		String id = "1234";

		doNothing().when(gameService)
				.suggestCharacter(eq(id), eq("player"), any(CharacterSuggestion.class));

		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/games/{id}/characters", id)
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\n" +
										"\"character\": \" char\"\n" +
										"}"))
				.andExpect(status().isOk());

		verify(gameService, times(1))
				.suggestCharacter(eq(id), eq("player"), any(CharacterSuggestion.class));
	}

	@Test
	void suggestCharacterFailMoreOneCharacterFromOnePlayer() throws Exception {
		String id = "1234";

		doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot add character to a game"))
				.when(gameService).suggestCharacter(eq(id), eq("player"), any(CharacterSuggestion.class));

		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/games/{id}/characters", id)
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON)
								.content("{\n" +
										"\"character\": \" char\"\n" +
										"}"))
				.andExpect(status().isInternalServerError());

		verify(gameService, times(1))
				.suggestCharacter(eq(id), eq("player"), any(CharacterSuggestion.class));
	}

	@Test
	void startGame() throws Exception {
		String id = "1234";
		GameDetails gameDetails = new GameDetails();
		gameDetails.setId(id);
		Optional<GameDetails> op = Optional.of(gameDetails);

		when(gameService.startGame(eq("1234"), eq("player"))).thenReturn(op);

		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/games/{id}", "1234")
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").value("1234"));

		verify(gameService, times(1)).startGame(eq("1234"), eq("player"));
	}

	@Test
	void startGameFailNotFound() throws Exception {
		String id = "1234";

		this.mockMvc.perform(
						MockMvcRequestBuilders.post("/games/{id}", id)
								.header("X-Player", "player")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		verify(gameService, times(1)).startGame(eq(id), eq("player"));
	}

}
