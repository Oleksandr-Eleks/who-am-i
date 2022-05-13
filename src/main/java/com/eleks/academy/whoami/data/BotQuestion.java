package com.eleks.academy.whoami.data;

import java.util.*;

public class BotQuestion {

    public static List<String> Questions() {
        List<String> answerList = Arrays.asList(
                "Am I exist in a real world?",
                "Am I alive?",
                "Am I a an animal?",
                "Am I a human?",
                "Am I a fruit?",
                "Am I a dead?",
                "Am I from fairy tales?",
                "Am I a substance?",
                "Can I breathe?",
                "Can I breathe underwater?");
        return answerList;
    }
    public static  List<String> ChineseBotQuestions(){
        List<String> questionList = Arrays.asList(
                "我还活着吗？",
                "我是人吗？",
                "我死了吗？"
        );
        return questionList;
    }
}
