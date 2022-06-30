package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.core.SynchronousGame;
import com.eleks.academy.whoami.core.Turn;
import com.eleks.academy.whoami.core.exception.AnswerQuestionException;
import com.eleks.academy.whoami.core.state.GameFinished;
import com.eleks.academy.whoami.core.state.GameState;
import com.eleks.academy.whoami.core.state.ProcessingQuestion;
import com.eleks.academy.whoami.core.state.WaitingForPlayers;
import com.eleks.academy.whoami.enums.GameStatus;
import com.eleks.academy.whoami.enums.QuestionAnswer;
import com.eleks.academy.whoami.model.request.Message;
import com.eleks.academy.whoami.model.response.PlayerWithState;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;

public class PersistentGame implements Game, SynchronousGame {

	private final String id;
	private Turn turns;
	private GameStatus gameStatus = GameStatus.WAITING_FOR_PLAYERS;
	private GameInfo gameInfo;


	private List<SynchronousPlayer> winners = new LinkedList<>();
	private Map <String, String> questions = new HashMap<>();
	private Map <String, String> answers = new HashMap<>();
	private List<SynchronousPlayer> players;
//	private Map <String, SynchronousPlayer> players;
	private ProcessingQuestion processingQuestion;
	private SynchronousPlayer currentPlayer;

	/**
	 * Creates a new game (game room) and makes a first enrolment turn by a current player
	 * so that he won't have to enroll to the game he created
	 *
	 * @param hostPlayer player to initiate a new game
	 */
	public PersistentGame(Integer maxPlayers) {
		this.id = String.format("%d-%d",
				Instant.now().toEpochMilli(),
				Double.valueOf(Math.random() * 999).intValue());
		this.players = new ArrayList<>(maxPlayers);
	}

	@Override
	public Optional<SynchronousPlayer> findPlayer(String player) {
		return this.applyIfPresent(this.turns.peek(), gameState -> gameState.findPlayer(player));
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public SynchronousPlayer enrollToGame(String player) {
		// TODO: Add player to players list
		this.players.put(player, new SynchronousPlayer(player));
		return players.get(player);
	}

	@Override
	public void startGame() {

	}

	@Override
	public List<SynchronousPlayer> getGamePLayers() {
		return null;
	}

	@Override
	public Turn getTurn() {
		return this.applyIfPresent(this.turns.peek(), GameState::getCurrentTurn);
	}

	@Override
	public void askQuestion(String player, String message) {
		// TODO: Show question
		this.processingQuestion = new ProcessingQuestion(players);
		questions.put(player, message);
		//TODO: turns
//		turns.add(processingQuestion.next());
	}

	@Override
	public void answerQuestion(String player, Answer answer) {

	}

	@Override
	public void answerQuestion(String player, QuestionAnswer answer) {
		if(answers.containsKey(player)) {
			throw new AnswerQuestionException("This player has answered already");
		}
		answers.put(player, answer.getMessage());
		if(answers.size() == players.size() - 1){
			int positiveAnswers = 0;
			for(var temp: answers.entrySet()){
			if(temp.getValue().toLowerCase(Locale.ROOT).equals("yes")){
				positiveAnswers++;
			}
		}
			if (positiveAnswers < players.size() / 2) {
				turns.remove();
				turns.add(processingQuestion.next());
			}
			for (var temp: answers.entrySet()){
				answers.remove(temp);
			}
		}
	}

	@Override
	public void askGuessingQuestion(String player, Message message, boolean guessStatus) {

	}

	@Override
	public void answerGuessingQuestion(String player, QuestionAnswer askQuestion, boolean guessStatus) {

	}

	@Override
	public SynchronousGame start() {
		return null;
	}

	@Override
	public boolean isAvailable() {
		return this.turns.peek() instanceof WaitingForPlayers;
	}

	@Override
	public String getStatus() {
		return this.applyIfPresent(this.turns.peek(), GameState::getStatus);
	}

	@Override
	public List<PlayerWithState> getPlayersInGame() {
		// TODO: Implement
		return null;
	}

	@Override
	public boolean isFinished() {
		return this.turns.isEmpty();
	}


	@Override
	public boolean makeTurn() {
		return true;
	}

	@Override
	public void changeTurn() {

	}

	@Override
	public void initGame() {

	}

	@Override
	public void play() {
		while (!(this.turns.peek() instanceof GameFinished)) {
			this.makeTurn();
		}
	}

	private <T, R> R applyIfPresent(T source, Function<T, R> mapper) {
		return this.applyIfPresent(source, mapper, null);
	}

	private <T, R> R applyIfPresent(T source, Function<T, R> mapper, R fallback) {
		return Optional.ofNullable(source)
				.map(mapper)
				.orElse(fallback);
	}
}
