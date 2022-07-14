package com.eleks.academy.whoami.service;

import com.eleks.academy.whoami.core.impl.PersistentGame;
import com.eleks.academy.whoami.enums.QuestionAnswer;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.Message;
import com.eleks.academy.whoami.model.request.NewGameRequest;
import com.eleks.academy.whoami.model.response.GameDetails;
import com.eleks.academy.whoami.model.response.PlayerDetails;
import com.eleks.academy.whoami.model.response.TurnDetails;
import com.eleks.academy.whoami.repository.HistoryChat;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GameService {

    List<PersistentGame> findAvailableGames();

    GameDetails createGame(String player, NewGameRequest gameRequest);

    PlayerDetails enrollToGame(String gameId, String playerId);

    GameDetails findGameById(String id);

    void suggestCharacter(String gameId, String player, CharacterSuggestion suggestion);

    Optional<GameDetails> startGame(String gameId, String player);

    void askQuestion(String gameId, String player, String message);

    Optional<TurnDetails> findTurnInfo(String id, String player);

    void submitGuess(String id, String player, Message guess);

    void answerQuestion(String id, String player, QuestionAnswer answer);

    HistoryChat gameHistory(String gameId);

    void answerGuessingQuestion(String id, String playerId, QuestionAnswer answer);

    void leaveGame(String gameId, String playerId);

    int getAllPlayers();

    List<PersistentGame> findAllGames();

    String getCurrentQuestion(String gameId, String playerId);

    String getCurrentAnswer(String gameId, String playerId);

    void inactivePlayer(String gameId, String playerId);

}
