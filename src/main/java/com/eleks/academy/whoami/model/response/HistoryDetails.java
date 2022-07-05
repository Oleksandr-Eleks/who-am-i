package com.eleks.academy.whoami.model.response;

import com.eleks.academy.whoami.repository.HistoryChat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class HistoryDetails {
    private Map<List<String>, List<String>> historyMap = new HashMap<>();

    public HistoryDetails(HistoryChat historyChat) {
        historyMap.put(historyChat.getQuestions(), historyChat.getAnswers());
    }

    @Override
    public String toString() {
        return "History of questions and answers:\n" +
                getHistoryMap();
    }
}
