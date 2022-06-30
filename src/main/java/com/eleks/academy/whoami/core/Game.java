package com.eleks.academy.whoami.core;

import com.eleks.academy.whoami.enums.QuestionAnswer;
import com.eleks.academy.whoami.model.request.Message;

import java.util.List;

public interface Game {
    void startGame();

    List<SynchronousPlayer> getGamePLayers();

    Turn getTurn();

    boolean isFinished();

    void askQuestion(String player, String message);

    void answerQuestion(String player, QuestionAnswer askQuestion);

    void askGuessingQuestion(String player, Message message, boolean guessStatus);

    void answerGuessingQuestion(String player, QuestionAnswer askQuestion, boolean guessStatus);
}
