package com.eleks.academy.whoami.repository;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class HistoryChat {
    private List<String> questions = new ArrayList<>();
    private List<String> answers = new ArrayList<>();

    public void setQuestions(String question) {
        this.questions.add(question);
    }

    public void setAnswers(String answer) {
        this.answers.add(answer);
    }

    public List<String> getQuestions() {
        return questions;
    }

    public List<String> getAnswers() {
        return answers;
    }
}
