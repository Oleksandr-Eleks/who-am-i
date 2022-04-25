package com.eleks.academy.whoami.impl;

import com.eleks.academy.whoami.core.Player;
import com.eleks.academy.whoami.utils.PropertyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static com.eleks.academy.whoami.constants.Constants.*;

public class RandomPlayer implements Player {

    private String name;
    private String character;

    private List<String> listQuestions;
    private Map<String, String> allQuestions;
    private Map<String, List<String>> characters;
    private Map<String, List<String>> allCharacters;
    private String numberQuestion;
    private String question;
    private String randomGuess;

    public RandomPlayer(String name) {
        this.name = "Bot-" + name;
        this.listQuestions = new ArrayList(PropertyUtils.readPropertyFile(GUESS_QUESTIONS).values());
        this.allQuestions = PropertyUtils.readPropertyFile(GUESS_QUESTIONS);
        this.characters = PropertyUtils.getCharactersWithQuestions(CHARACTERS);
        this.allCharacters = PropertyUtils.getCharactersWithQuestions(CHARACTERS);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setCharacter(String character) {
        this.character = character;
    }

    @Override
    public String getCharacter() {
        return character;
    }

    @Override
    public int getAssumptionOrClarification() {
        return characters.size() == 1 ? 1 : new Random().nextInt(2);
    }

    @Override
    public String getQuestion() {
        int randomQuestion = new Random().nextInt(listQuestions.size());
        this.question = listQuestions.get(randomQuestion);
        listQuestions.remove(randomQuestion);
        setNumberQuestion();
        System.out.println("Player: " + name + ". Asks: " + question);
        return this.question;
    }

    @Override
    public String answerQuestion(String question, String character) {
        String answer = allCharacters.get(character).stream().filter(que -> allQuestions.get(que).equals(question)).count() > 0 ? YES : NO;
        System.out.println("Player: " + name + ". Answers: " + answer);
        return answer;
    }

    private void setNumberQuestion() {
        this.numberQuestion = allQuestions.entrySet().stream().filter(questions -> questions.getValue().equals(this.question)).findFirst().get().getKey();
    }

    @Override
    public void removeCharactersWithQuestions(boolean win) {
        if (win) {
            characters = characters.entrySet().stream().filter(character -> character.getValue().contains(numberQuestion))
                    .collect(Collectors.toMap(characterKey -> characterKey.getKey(), characterValue -> characterValue.getValue()));
        } else {
            characters = characters.entrySet().stream().filter(character -> !character.getValue().contains(numberQuestion))
                    .collect(Collectors.toMap(characterKey -> characterKey.getKey(), characterValue -> characterValue.getValue()));
        }
    }

    @Override
    public String getGuess() {
        int randomCharacter = new Random().nextInt(characters.keySet().size());
        randomGuess = characters.keySet().toArray(new String[characters.keySet().size()])[randomCharacter];
        System.out.println("Player: " + name + ". Guesses: Am I " + randomGuess + " ?");
        return randomGuess;
    }

    @Override
    public String answerGuess(String guess, String character) {
        String answer = guess.equals(character) ? YES : NO;
        System.out.println("Player: " + name + ". Answers: " + answer);
        return answer;
    }

    @Override
    public void removeCharactersWithCharacter() {
        characters = characters.entrySet().stream().filter(character -> !character.getKey().equals(this.randomGuess))
                .collect(Collectors.toMap(characterKey -> characterKey.getKey(), characterValue -> characterValue.getValue()));
    }
}
