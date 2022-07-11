package com.eleks.academy.whoami.service.impl;

import com.eleks.academy.whoami.core.exception.GameNotFoundException;
import com.eleks.academy.whoami.core.exception.GameStateException;
import com.eleks.academy.whoami.core.impl.PersistentGame;
import com.eleks.academy.whoami.core.impl.PersistentPlayer;
import com.eleks.academy.whoami.enums.GameStatus;
import com.eleks.academy.whoami.enums.QuestionAnswer;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.Message;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.model.response.HistoryDetails;
import com.eleks.academy.whoami.model.response.PlayerDetails;
import com.eleks.academy.whoami.model.response.TurnDetails;
import com.eleks.academy.whoami.repository.GameRepository;
import com.eleks.academy.whoami.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.eleks.academy.whoami.enums.Constants.ROOM_NOT_FOUND_BY_ID;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    @Override
    public List<PersistentGame> findAvailableGames() {
        return this.gameRepository.findAllAvailable();
    }

    @Override
    public GameDetails findGameById(String id) {
        return new GameDetails(checkGameExistence(id));
    }

    @Override
    public Optional<TurnDetails> findTurnInfo(String id, String player) {
        PersistentGame game = checkGameExistence(id);
        return Optional.of(game.getTurnDetails());
    }

    @Override
    public GameDetails createGame(String playerId, NewGameRequest gameRequest) {
        List<PersistentGame> availableGames = findAvailableGames();
        if (availableGames.isEmpty()) {
            PersistentGame game = new PersistentGame(playerId, gameRequest.getMaxPlayers());
            return new GameDetails(gameRepository.save(game));
        } else {
            PersistentGame game = checkGameExistence(availableGames.get(0).getGameId());
            if (game.getStatus().equals(GameStatus.WAITING_FOR_PLAYERS)) {
                game.enrollToGame(playerId);
            } else {
                throw new GameStateException("You cannot enroll to this game! All player slots are taken");
            }
            return new GameDetails(game);
        }
    }

    @Override
    public PlayerDetails enrollToGame(String gameId, String playerId) {
        PersistentGame game = checkGameExistence(gameId);
        if (game.getStatus().equals(GameStatus.WAITING_FOR_PLAYERS)) {
            return game.enrollToGame(playerId);
        }
        throw new GameStateException("You cannot enroll to this game! All player slots are taken");
    }

    @Override
    public void suggestCharacter(String gameId, String player, CharacterSuggestion suggestion) {
        PersistentGame game = checkGameExistence(gameId);
        if (game.getStatus().equals(GameStatus.SUGGEST_CHARACTER)) {
            game.suggestCharacter(player, suggestion);
        } else {
            throw new GameStateException("You cannot suggest the character! Current game state is: " + game.getStatus());
        }
    }

    @Override
    public Optional<GameDetails> startGame(String gameId, String player) {
        PersistentGame game = checkGameExistence(gameId);
        switch (game.getStatus()) {

            case GAME_IN_PROGRESS -> throw new GameStateException("Game already in progress! Find another one to play!");

            case READY_TO_PLAY -> {
                game.startGame();
                return Optional.of(new GameDetails(game));
            }

            case SUGGEST_CHARACTER -> throw new GameStateException("Game can not be started! Players suggesting characters! " +
                    "Waiting for other players to contribute their characters" +
                    "Players left: " +
                    game.getPLayers()
                            .stream()
                            .filter(randomPlayer -> !randomPlayer.isSuggestStatus())
                            .map(PersistentPlayer::getNickname)
                            .collect(Collectors.toList()));

            case WAITING_FOR_PLAYERS -> throw new GameStateException("Game can not be started!" +
                    " Waiting for additional players! " +
                    "Current players number: " + game.getPLayers().size() + game.getMaxPlayers());
            default -> throw new GameStateException("Unrecognized state!");
        }
    }

    @Override
    public void askQuestion(String gameId, String player, String message) {
        PersistentGame game = checkGameExistence(gameId);
        if (game.getStatus().equals(GameStatus.GAME_IN_PROGRESS)) {
            game.askQuestion(player, message);
        }
    }

    @Override
    public void answerQuestion(String gameId, String player, QuestionAnswer answer) {
        PersistentGame game = checkGameExistence(gameId);
        if (game.getStatus().equals(GameStatus.GAME_IN_PROGRESS)) {
            game.answerQuestion(player, answer);
        }
    }

    @Override
    public void submitGuess(String gameId, String player, Message guess) {
        PersistentGame game = checkGameExistence(gameId);
        if (game.getStatus().equals(GameStatus.GAME_IN_PROGRESS)) {
            game.askGuessingQuestion(player, guess);
        }
    }


    @Override
    public void answerGuessingQuestion(String gameId, String playerId, QuestionAnswer answerQuess) {
        PersistentGame game = checkGameExistence(gameId);
        if (game.getStatus().equals(GameStatus.GAME_IN_PROGRESS)) {
            game.answerGuessingQuestion(playerId, answerQuess);
        }
    }

    private PersistentGame checkGameExistence(String gameId) {
        if (gameRepository.findById(gameId).isPresent()) {
            return gameRepository.findById(gameId).get();
        }
        throw new GameNotFoundException(String.format(ROOM_NOT_FOUND_BY_ID, gameId));
    }

    @Override
    public String gameHistory(String gameId) {
        PersistentGame game = checkGameExistence(gameId);
        return (new HistoryDetails(game.getHistory())).toString();
    }

    @Override
    public void leaveGame(String gameId, String playerId) {
        PersistentGame game = checkGameExistence(gameId);
        game.deletePlayer(playerId);
    }

    @Override
    public List<PersistentGame> findAllGames() {
        return this.gameRepository.findAllGames();
    }

    @Override
    public int getAllPlayers() {
        List<PersistentGame> allGames = findAllGames();
        int allPlayers = 0;
        if (!allGames.isEmpty()) {
            for (var game : allGames) {
                allPlayers += game.getPLayers().size();
            }
        }
        return allPlayers;
    }

}
