package com.eleks.academy.whoami.repository;

import com.eleks.academy.whoami.enums.QuestionAnswer;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class HistoryChat {
    private Map<String, String> allQuestions;
    private Map<String, QuestionAnswer> allAnswers;

    public Map<String, String> getAllQuestions() {
        return allQuestions;
    }

    public void setAllQuestions(String nickName, String question) {
        allQuestions.put(nickName, question);
    }

    public Map<String, QuestionAnswer> getAllAnswers() {
        return allAnswers;
    }

    public void setAllAnswers(String nickName, QuestionAnswer answer) {
        allAnswers.put(nickName, answer);
    }
}
