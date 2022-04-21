package com.eleks.academy.whoami.data;

import java.util.Arrays;
import java.util.List;

public class ChineseBotGuesses {

    public static List<String> getChineseGuess() {
        final List<String> guess = Arrays.asList(
                "问你？ 我可以留在美国吗？",
                "问你？ 你会付钱给我吗？"
        );
        return guess;
    }
}
