package com.eleks.academy.whoami.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
public class HistoryChat {
    @Getter
    private final Map<HistoryQuestion, HistoryQuestion> questions = new HashMap<>();
    private HistoryQuestion currentQuestion;

    public void addQuestion(String question, String player) {
        final var historyQuestion = new HistoryQuestion(player, question);
        this.questions.put(historyQuestion, historyQuestion);
        this.currentQuestion = historyQuestion;
    }

    public void addAnswer(String answer, String player) {
        Optional.ofNullable(this.questions.get(this.currentQuestion))
                .ifPresent(question -> question.addAnswer(answer, player));
    }
}

@Data
@NoArgsConstructor
class HistoryQuestion {
    private String player;
    private String question;
    private List<HistoryAnswer> answers = new ArrayList<>();

    public HistoryQuestion(String player, String question) {
        this.player = player;
        this.question = question;
    }

    public void addAnswer(String answer, String player) {
        this.answers.add(new HistoryAnswer(player, answer));
    }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class HistoryAnswer {
    private String player;
    private String answer;

}
