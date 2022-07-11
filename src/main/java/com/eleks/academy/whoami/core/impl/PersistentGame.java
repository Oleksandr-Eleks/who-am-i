package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Turn;
import com.eleks.academy.whoami.core.exception.GameStateException;
import com.eleks.academy.whoami.core.exception.PlayerNotFoundException;
import com.eleks.academy.whoami.core.exception.TurnException;
import com.eleks.academy.whoami.enums.GameStatus;
import com.eleks.academy.whoami.enums.PlayerState;
import com.eleks.academy.whoami.enums.QuestionAnswer;
import com.eleks.academy.whoami.model.request.CharacterSuggestion;
import com.eleks.academy.whoami.model.request.Message;
import com.eleks.academy.whoami.model.response.PlayerDetails;
import com.eleks.academy.whoami.model.response.TurnDetails;
import com.eleks.academy.whoami.repository.HistoryChat;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.eleks.academy.whoami.enums.Constants.PLAYER_NOT_FOUND;

public class PersistentGame {

    private String id;
    private List<PersistentPlayer> players;
    private int maxPlayers;
    private GameStatus gameStatus = GameStatus.WAITING_FOR_PLAYERS;
    private List<PersistentPlayer> winners = new LinkedList<>();
    private Turn turn;
    private HistoryChat history = new HistoryChat();


    /**
     * Creates a new game (game room) and makes a first enrolment turn by a current player
     * so that he won't have to enroll to the game he created
     *
     * @param hostPlayer player to initiate a new game
     */
    public PersistentGame(String hostPlayer, Integer maxPlayers) {
        this.id = String.format("%d-%d",
                Instant.now().toEpochMilli(),
                Double.valueOf(Math.random() * 999).intValue());
        this.players = new ArrayList<>(maxPlayers);
        this.turn = new TurnImpl(players);
        this.maxPlayers = maxPlayers;
        players.add(new PersistentPlayer(hostPlayer, id, "hostPlayer"));
    }

    public String getGameId() {
        return this.id;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public List<PersistentPlayer> getPLayers() {
        return players;
    }

    public GameStatus getStatus() {
        return gameStatus;
    }


    public List<PersistentPlayer> getWinnerList() {
        return winners;
    }

    /***
     *
     * @return data about current turn (current player, and other players)
     */
    public TurnDetails getTurnDetails() {
        return new TurnDetails(turn.getCurrentGuesser(), turn.getOtherPlayers());
    }

    /***
     *
     * @return PersistentPlayer whose turn is now
     */
    public PersistentPlayer getCurrentTurn() {
        return turn.getCurrentGuesser();
    }

    public PlayerDetails enrollToGame(String playerId) {
        PersistentPlayer player;
        if (players.stream().noneMatch((randomPlayer -> randomPlayer.getId().equals(playerId)))) {
            player = new PersistentPlayer(playerId, this.id, "Player-" + (players.size() + 1));
            players.add(player);
            if (players.size() == maxPlayers) {
                this.turn = new TurnImpl(players);
                gameStatus = GameStatus.SUGGEST_CHARACTER;
            }
            return PlayerDetails.of(player);
        } else {
            throw new GameStateException("Player already enrolled in this game!");
        }
    }

    public void suggestCharacter(String playerId, CharacterSuggestion suggestion) {
        var player = players
                .stream()
                .filter(randomPlayer -> randomPlayer.getId().equals(playerId))
                .findFirst().orElseThrow(() -> new PlayerNotFoundException("Player not found!"));
        if (!player.isSuggestStatus()) {
            player.setCharacter(suggestion.getCharacter());
            player.setNickname(suggestion.getNickname());
            player.setSuggestStatus(true);
        } else {
            throw new GameStateException("You already suggest the character");
        }
        if (players.stream().filter(PersistentPlayer::isSuggestStatus).count() == maxPlayers) {
            gameStatus = GameStatus.READY_TO_PLAY;
        }
    }

    public void startGame() {
        if (players.stream().filter(PersistentPlayer::isSuggestStatus).count() == maxPlayers) {
            assignCharacters();
            var currentPlayer = turn.getCurrentGuesser();
            currentPlayer.setPlayerState(PlayerState.ASK_QUESTION);
            players.stream()
                    .filter(randomPlayer -> !randomPlayer.getId().equals(currentPlayer.getId()))
                    .forEach(randomPlayer -> randomPlayer.setPlayerState(PlayerState.ANSWER_QUESTION));
            gameStatus = GameStatus.GAME_IN_PROGRESS;
        }
    }


    public void askQuestion(String playerId, String message) {
        // TODO: Show question
        var askingPlayer = players
                .stream()
                .filter(randomPlayer -> randomPlayer.getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundException(String.format(PLAYER_NOT_FOUND, playerId)));

        cleanPlayersValues(players);

        if (askingPlayer.getPlayerState().equals(PlayerState.ASK_QUESTION)) {
            askingPlayer.setPlayerQuestion(message);
            askingPlayer.setEnteredQuestion(true);

            addQuestionToHistory(askingPlayer.getNickname(), message);

        } else {
            throw new TurnException("Not your turn! Current turn has player: " + getCurrentTurn().getNickname());
        }
    }

    public void answerQuestion(String playerId, QuestionAnswer questionAnswer) {
        //TODO: show on screen questions and answers from history
        var askingPlayer = turn.getCurrentGuesser();
        var answeringPlayer = players
                .stream()
                .filter(randomPlayer -> randomPlayer.getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundException(String.format(PLAYER_NOT_FOUND, playerId)));

        if (askingPlayer.equals(answeringPlayer)) {
            throw new TurnException("Not your turn for answering");
        }

        var playersAnswers = turn.getPlayersAnswers();

        if (answeringPlayer.getPlayerState().equals(PlayerState.ANSWER_QUESTION)) {
            playersAnswers.add(questionAnswer);
            answeringPlayer.setEnteredAnswer(true);
            answeringPlayer.setPlayerAnswer(String.valueOf(questionAnswer));

            addAnswerToHistory(answeringPlayer.getNickname(), questionAnswer.toString());
        }

        if (playersAnswers.size() == players.size() - 1) {
            var positiveAnswers = playersAnswers
                    .stream()
                    .filter(answer -> answer.equals(QuestionAnswer.NOT_SURE) || answer.equals(QuestionAnswer.YES))
                    .collect(Collectors.toList());

            var negativeAnswers = playersAnswers
                    .stream()
                    .filter(answer -> answer.equals(QuestionAnswer.NO))
                    .collect(Collectors.toList());

            if (positiveAnswers.size() < negativeAnswers.size()) {
                this.turn = this.turn.changeTurn();
            }
        }
    }

    public void askGuessingQuestion(String playerId, Message guess) {
        var askingPlayer = players
                .stream()
                .filter(randomPlayer -> randomPlayer.getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundException(String.format(PLAYER_NOT_FOUND, playerId)));

        cleanPlayersValues(players);

        if (askingPlayer.getPlayerState().equals(PlayerState.ASK_QUESTION)) {
            askingPlayer.setPlayerState(PlayerState.GUESSING);
            askingPlayer.setPlayerQuestion(guess.getMessage());
            askingPlayer.setEnteredQuestion(true);
            askingPlayer.setGuessing(true);

            addQuestionToHistory(askingPlayer.getNickname(), guess.getMessage());

        } else {
            throw new TurnException("Not your turn! Current turn has player: " + getCurrentTurn().getNickname());
        }
    }

    public void answerGuessingQuestion(String playerId, QuestionAnswer askQuestion) {
        var askingPlayer = turn.getCurrentGuesser();
        var answeringPlayer = players
                .stream()
                .filter(randomPlayer -> randomPlayer.getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundException(String.format(PLAYER_NOT_FOUND, playerId)));

        if (askingPlayer.equals(answeringPlayer)) {
            throw new TurnException("Not your turn for answering");
        }

        var playersAnswers = turn.getPlayersAnswers();

        if (answeringPlayer.getPlayerState().equals(PlayerState.ANSWER_QUESTION)) {
            playersAnswers.add(askQuestion);
            answeringPlayer.setEnteredAnswer(true);
            answeringPlayer.setPlayerAnswer(String.valueOf(askQuestion));

            addAnswerToHistory(answeringPlayer.getNickname(), askQuestion.toString());
        }

        if (playersAnswers.size() == this.players.size() - 1) {
            var positiveAnswers = playersAnswers
                    .stream()
                    .filter(answer -> answer.equals(QuestionAnswer.YES))
                    .collect(Collectors.toList());

            var negativeAnswers = playersAnswers
                    .stream()
                    .filter(answer -> answer.equals(QuestionAnswer.NOT_SURE) || answer.equals(QuestionAnswer.NO))
                    .collect(Collectors.toList());

            if (positiveAnswers.size() > negativeAnswers.size()) {
                this.winners.add(askingPlayer);
                deletePlayer(playerId);
            } else {
                this.turn = this.turn.changeTurn();
            }
        }

    }

    public void deletePlayer(String playerId) {
        Optional<PersistentPlayer> playerToRemove = this.players
                .stream()
                .filter(randomPlayer -> randomPlayer.getId().equals(playerId))
                .findFirst();

        playerToRemove.ifPresent(this.players::remove);
        this.turn.removePLayer(playerId);
    }


    private void assignCharacters() {
        var availableCharacters = players.stream()
                .map(PersistentPlayer::getCharacter)
                .collect(Collectors.toList());

        final var random = new Random();

        for (int i = availableCharacters.size() - 1; i >= 1; i--) {
            Collections.swap(availableCharacters, i, random.nextInt(i + 1));
        }

        AtomicInteger a = new AtomicInteger();
        players.forEach(randomPlayer -> randomPlayer.setCharacter(availableCharacters.get(a.getAndIncrement())));
    }

    private void cleanPlayersValues(List<PersistentPlayer> players) {
        players.forEach(randomPlayer -> {
            randomPlayer.setEnteredAnswer(false);
            randomPlayer.setEnteredQuestion(false);
            randomPlayer.setPlayerQuestion(null);
            randomPlayer.setPlayerAnswer(null);
        });
    }

    private void addQuestionToHistory(String nickName, String question) {
        StringBuffer sb = new StringBuffer("\n" + nickName + ". Question : " + question);
        history.setQuestions(sb.toString());
    }

    private void addAnswerToHistory(String nickName, String answer) {
        StringBuffer sb = new StringBuffer(nickName + ". Answer : " + answer);
        history.setQuestions(sb.toString());
    }

    public HistoryChat getHistory() {
        return history;
    }
}
