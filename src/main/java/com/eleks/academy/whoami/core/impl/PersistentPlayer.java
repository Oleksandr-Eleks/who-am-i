package com.eleks.academy.whoami.core.impl;

import com.eleks.academy.whoami.enums.PlayerState;

import java.util.Objects;

public class PersistentPlayer {

    private String id;
    private String gameId;
    private String nickname;
    private String character;
    private boolean suggestStatus = false;
    private PlayerState playerState;
    private boolean enteredAnswer;
    private boolean enteredQuestion;
    private boolean guessing;
    private String playerQuestion;
    private String playerAnswer;

    private int inactiveCounter;

    public PersistentPlayer(String id, String gameId, String nickname) {
        this.id = id;
        this.gameId = gameId;
        this.nickname = nickname;
        this.inactiveCounter = 0;
        this.playerState = PlayerState.WAITING;
    }

    public int getInactiveCounter() {
        return inactiveCounter;
    }

    public void incrementInactiveCounter() {
        this.inactiveCounter++;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public boolean isSuggestStatus() {
        return suggestStatus;
    }

    public void setSuggestStatus(boolean suggestStatus) {
        this.suggestStatus = suggestStatus;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public boolean isEnteredAnswer() {
        return enteredAnswer;
    }

    public void setEnteredAnswer(boolean enteredAnswer) {
        this.enteredAnswer = enteredAnswer;
    }

    public boolean isEnteredQuestion() {
        return enteredQuestion;
    }

    public void setEnteredQuestion(boolean enteredQuestion) {
        this.enteredQuestion = enteredQuestion;
    }

    public String getPlayerQuestion() {
        return playerQuestion;
    }

    public void setPlayerQuestion(String playerQuestion) {
        this.playerQuestion = playerQuestion;
    }

    public String getPlayerAnswer() {
        return playerAnswer;
    }

    public void setPlayerAnswer(String playerAnswer) {
        this.playerAnswer = playerAnswer;
    }

    public boolean isGuessing() {
        return guessing;
    }

    public void setGuessing(boolean guessing) {
        this.guessing = guessing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PersistentPlayer that = (PersistentPlayer) o;
        return suggestStatus == that.suggestStatus && enteredAnswer == that.enteredAnswer && enteredQuestion == that.enteredQuestion && guessing == that.guessing &&
                Objects.equals(id, that.id) && Objects.equals(gameId, that.gameId) && Objects.equals(nickname, that.nickname) &&
                Objects.equals(character, that.character) && playerState == that.playerState && Objects.equals(playerQuestion, that.playerQuestion) &&
                Objects.equals(playerAnswer, that.playerAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gameId, nickname, character, suggestStatus, playerState, enteredAnswer, enteredQuestion, guessing, playerQuestion, playerAnswer);
    }

    @Override
    public String toString() {
        return "RandomPlayer{" +
                "id='" + id + '\'' +
                ", roomId='" + gameId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", character='" + character + '\'' +
                ", suggestStatus=" + suggestStatus +
                ", playerState=" + playerState +
                ", enteredAnswer=" + enteredAnswer +
                ", enteredQuestion=" + enteredQuestion +
                ", guessing=" + guessing +
                ", playerQuestion='" + playerQuestion + '\'' +
                ", playerAnswer='" + playerAnswer + '\'' +
                '}';
    }
}
