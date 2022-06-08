package com.eleks.academy.whoami.api;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static java.util.Collections.singletonList;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.HttpStatus;

import com.eleks.academy.whoami.handler.ApiClient;
import com.eleks.academy.whoami.model.CharacterSuggestion;
import com.eleks.academy.whoami.model.GameDetails;
import com.eleks.academy.whoami.model.GameLight;
import com.eleks.academy.whoami.model.Message;
import com.eleks.academy.whoami.model.NewGameRequest;
import com.eleks.academy.whoami.model.PlayerState;
import com.eleks.academy.whoami.model.PlayerWithState;
import com.eleks.academy.whoami.model.QuestionAnswer;
import com.eleks.academy.whoami.model.SynchronousPlayer;
import com.eleks.academy.whoami.model.TurnDetails;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;

class GameApiTest {

	@RegisterExtension
	private static final WireMockExtension wireMockServer = WireMockExtension.newInstance()
			.options(wireMockConfig().port(8090)).build();
	
	@BeforeEach
	void prepare() {
		wireMockServer.resetAll();
	}
	
	@AfterAll
	static void shutDown() {
		wireMockServer.shutdownServer();
	}

	@Test
	void findAvailableGamesTest() throws IOException {
		try (InputStream in = getClass().getResourceAsStream("/models/game/get-available-games-response.json")) {
			String expectedResponse = new String(in.readAllBytes());
			wireMockServer
					.stubFor(WireMock.get(WireMock.urlMatching("/api/v1/games")).withHeader("X-Player",
							equalTo("Example"))
					.willReturn(WireMock.aResponse().withBody(expectedResponse).withStatus(HttpStatus.OK.value())
							.withHeader("Content-Type", "application/json")));

			GameApi gameApi = new GameApi(new ApiClient());
			
			GameLight gameLight = new GameLight();
			gameLight.setId("1234-UUID");
			gameLight.setStatus("WaitingForPlayers");
			gameLight.setPlayersInGame("3/4");

			assertThat(gameApi.findAvailableGames("Example"))
				.isEqualTo(singletonList(gameLight));
		}
	}
	
	@Test
	void createGameTest() throws IOException {
		try(InputStream in1 = getClass().getResourceAsStream("/models/game/create-game-request.json");
				InputStream in2 = getClass().getResourceAsStream("/models/game/create-game-response.json");){
			String expectedRequest = new String(in1.readAllBytes());
			String expectedResponse = new String(in2.readAllBytes());
			
			wireMockServer
					.stubFor(WireMock.post(WireMock.urlMatching("/api/v1/games"))
							.withHeader("X-Player", equalTo("Example"))
							.withHeader("Content-Type", equalTo("application/json"))
							.withRequestBody(equalToJson(expectedRequest))
					.willReturn(WireMock.aResponse().withBody(expectedResponse)
							.withStatus(HttpStatus.CREATED.value())
							.withHeader("Content-Type", "application/json")));
			
			GameApi gameApi = new GameApi(new ApiClient());
			
			NewGameRequest newGameRequest = new NewGameRequest();
			newGameRequest.maxPlayers(4);
			
			SynchronousPlayer synchronousPlayer = new SynchronousPlayer();
			synchronousPlayer.name("Example");
			synchronousPlayer.character("Batman");
			
			PlayerWithState playerWithState = new PlayerWithState();
			playerWithState.player(synchronousPlayer);
			playerWithState.answer(QuestionAnswer.NO);
			playerWithState.state(PlayerState.NOT_READY);
			
			GameDetails gameDetails = new GameDetails();
			gameDetails.id("1234-Uid");
			gameDetails.status("WaitingForPlayers");
			gameDetails.currentTurn("1");
			gameDetails.players(singletonList(playerWithState));
			
			assertThat(gameApi.createGame(newGameRequest, "Example")).isEqualTo(gameDetails);
		}
	}
	
	@Test
	void findByIdTest() throws IOException {
		try (InputStream in = getClass().getResourceAsStream("/models/game/get-game-by-id-response.json")) {
			String expectedResponse = new String(in.readAllBytes());
			wireMockServer
					.stubFor(WireMock.get(WireMock.urlMatching("/api/v1/games/1234-Uid")).withHeader("X-Player",
							equalTo("Example"))
					.willReturn(WireMock.aResponse().withBody(expectedResponse).withStatus(HttpStatus.OK.value())
							.withHeader("Content-Type", "application/json")));

			GameApi gameApi = new GameApi(new ApiClient());
			
			SynchronousPlayer synchronousPlayer = new SynchronousPlayer();
			synchronousPlayer.name("Example");
			synchronousPlayer.character("Batman");
			
			PlayerWithState playerWithState = new PlayerWithState();
			playerWithState.player(synchronousPlayer);
			playerWithState.answer(QuestionAnswer.NO);
			playerWithState.state(PlayerState.NOT_READY);
			
			GameDetails gameDetails = new GameDetails();
			gameDetails.id("1234-Uid");
			gameDetails.status("WaitingForPlayers");
			gameDetails.currentTurn("1");
			gameDetails.players(singletonList(playerWithState));

			assertThat(gameApi.findById("1234-Uid", "Example"))
				.isEqualTo(gameDetails);
		}
	}
	
	@Test
	void startGameTest() throws IOException {
		try(InputStream in1 = getClass().getResourceAsStream("/models/game/start-game-response.json")){
			String expectedResponse = new String(in1.readAllBytes());
			
			wireMockServer
					.stubFor(WireMock.post(WireMock.urlMatching("/api/v1/games/1234-Uid/"))
							.withHeader("X-Player", equalTo("Example"))
					.willReturn(WireMock.aResponse().withBody(expectedResponse)
							.withStatus(HttpStatus.OK.value())
							.withHeader("Content-Type", "application/json")));
			
			GameApi gameApi = new GameApi(new ApiClient());
			
			SynchronousPlayer synchronousPlayer = new SynchronousPlayer();
			synchronousPlayer.name("Example");
			synchronousPlayer.character("Batman");
			
			PlayerWithState playerWithState = new PlayerWithState();
			playerWithState.player(synchronousPlayer);
			playerWithState.answer(QuestionAnswer.NO);
			playerWithState.state(PlayerState.NOT_READY);
			
			GameDetails gameDetails = new GameDetails();
			gameDetails.id("1234-Uid");
			gameDetails.status("WaitingForPlayers");
			gameDetails.currentTurn("1");
			gameDetails.players(singletonList(playerWithState));
			
			assertThat(gameApi.startGame("1234-Uid", "Example")).isEqualTo(gameDetails);
		}
	}
	
	@Test
	void enrollToGameTest() throws IOException {
		try(InputStream in1 = getClass().getResourceAsStream("/models/game/enroll-player-response.json")){
			String expectedResponse = new String(in1.readAllBytes());
			
			wireMockServer
					.stubFor(WireMock.post(WireMock.urlMatching("/api/v1/games/1234-Uid/players"))
							.withHeader("X-Player", equalTo("Example"))
					.willReturn(WireMock.aResponse().withBody(expectedResponse)
							.withStatus(HttpStatus.OK.value())
							.withHeader("Content-Type", "application/json")));
			
			GameApi gameApi = new GameApi(new ApiClient());
			
			SynchronousPlayer synchronousPlayer = new SynchronousPlayer();
			synchronousPlayer.name("Example");
			synchronousPlayer.character("Batman");
			
			assertThat(gameApi.enrollToGame("1234-Uid", "Example")).isEqualTo(synchronousPlayer);
		}
	}
	
	@Test
	void suggestCharacterTest() throws IOException {
		try(InputStream in1 = getClass().getResourceAsStream("/models/game/suggest-character-request.json")){
			String expectedRequest = new String(in1.readAllBytes());
			
			wireMockServer
					.stubFor(WireMock.post(WireMock.urlMatching("/api/v1/games/1234-Uid/characters"))
							.withHeader("X-Player", equalTo("Example"))
							.withRequestBody(equalToJson(expectedRequest))
					.willReturn(WireMock.aResponse().withStatus(HttpStatus.OK.value())));
			
			GameApi gameApi = new GameApi(new ApiClient());
			
			CharacterSuggestion characterSuggestion = new CharacterSuggestion();
			characterSuggestion.character("Batman");
			
			gameApi.suggestCharacter(characterSuggestion, "1234-Uid", "Example");
		}
	}
	
	@Test
	void findTurnInfoTest() throws IOException {
		try (InputStream in = getClass().getResourceAsStream("/models/game/get-current-turn-response.json")) {
			String expectedResponse = new String(in.readAllBytes());
			wireMockServer
					.stubFor(WireMock.get(WireMock.urlMatching("/api/v1/games/1234-Uid/turn")).withHeader("X-Player",
							equalTo("Example"))
					.willReturn(WireMock.aResponse().withBody(expectedResponse).withStatus(HttpStatus.OK.value())
							.withHeader("Content-Type", "application/json")));

			GameApi gameApi = new GameApi(new ApiClient());
			
			SynchronousPlayer synchronousPlayer = new SynchronousPlayer();
			synchronousPlayer.name("Example");
			synchronousPlayer.character("Batman");
			
			PlayerWithState playerWithState = new PlayerWithState();
			playerWithState.player(synchronousPlayer);
			playerWithState.answer(QuestionAnswer.NO);
			playerWithState.state(PlayerState.NOT_READY);
			
			TurnDetails turnDetails = new TurnDetails();
			turnDetails.players(singletonList(playerWithState));
			turnDetails.currentPlayer(synchronousPlayer);

			assertThat(gameApi.findTurnInfo("1234-Uid", "Example"))
				.isEqualTo(turnDetails);
		}
	}
	
	@Test
	void askQuestionTest() throws IOException {
		try(InputStream in1 = getClass().getResourceAsStream("/models/game/ask-question-request.json")){
			String expectedRequest = new String(in1.readAllBytes());
			
			wireMockServer
					.stubFor(WireMock.post(WireMock.urlMatching("/api/v1/games/1234-Uid/questions"))
							.withHeader("X-Player", equalTo("Example"))
							.withRequestBody(equalToJson(expectedRequest))
					.willReturn(WireMock.aResponse().withStatus(HttpStatus.OK.value())));
			
			GameApi gameApi = new GameApi(new ApiClient());
			
			Message message = new Message();
			message.message("Question");
			
			gameApi.askQuestion(message, "1234-Uid", "Example");
		}
	}
	
	@Test
	void guessTest() throws IOException {
		try(InputStream in1 = getClass().getResourceAsStream("/models/game/guess-request.json")){
			String expectedRequest = new String(in1.readAllBytes());
			
			wireMockServer
					.stubFor(WireMock.post(WireMock.urlMatching("/api/v1/games/1234-Uid/guess"))
							.withHeader("X-Player", equalTo("Example"))
							.withRequestBody(equalToJson(expectedRequest))
					.willReturn(WireMock.aResponse().withStatus(HttpStatus.OK.value())));
			
			GameApi gameApi = new GameApi(new ApiClient());
			
			Message message = new Message();
			message.message("Question");
			
			gameApi.submitGuess(message, "1234-Uid", "Example");
		}
	}
	
	@Test
	void answerTest() throws IOException {
		try(InputStream in1 = getClass().getResourceAsStream("/models/game/answer-request.json")){
			String expectedRequest = new String(in1.readAllBytes());
			
			wireMockServer
					.stubFor(WireMock.post(WireMock.urlMatching("/api/v1/games/1234-Uid/answer"))
							.withHeader("X-Player", equalTo("Example"))
							.withRequestBody(equalToJson(expectedRequest))
					.willReturn(WireMock.aResponse().withStatus(HttpStatus.OK.value())));
			
			GameApi gameApi = new GameApi(new ApiClient());
			
			Message message = new Message();
			message.message("Question");
			
			gameApi.answerQuestion(message, "1234-Uid", "Example");
		}
	}
	
	

}
