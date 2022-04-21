package com.eleks.academy.whoami;

import com.eleks.academy.whoami.constants.Constants;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.impl.RandomGame;
import com.eleks.academy.whoami.impl.RandomPlayer;
import com.eleks.academy.whoami.utils.PropertyUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

 public static FileInputStream getPropertyFile(String nameFile) throws FileNotFoundException {
     return new FileInputStream(Path.of("src", "main", "resources", nameFile).toFile());
 }

 public static Map<String, String> getQuestions(String nameFile){

     Properties properties = new Properties();
     Map<String, String> map = new HashMap();
     try (FileInputStream file = getPropertyFile(nameFile)) {
         properties.load(file);

         for (final String name: properties.stringPropertyNames())
             map.put(name, properties.getProperty(name));

     } catch (FileNotFoundException e) {
         e.printStackTrace();
     } catch (IOException e) {
         e.printStackTrace();
     }

     return map;
 }



 public static void main(String[] args) {


//     getQuestions("guess_questions.properties").entrySet().forEach(set -> System.out.println(set.getKey() + " " + set.getValue()));
//
//     //String-имя конкретного персонажа
//     //List<String>-список вопросов, которые описывают конкретный персонаж
//     Map<String, List<String>> getCharacters = getQuestions("characters.properties").
//             entrySet().stream().collect(Collectors.toMap(key -> key.getKey(), values -> Arrays.asList(values.getValue().split("\\s*,\\s*"))));
//
//     getCharacters.entrySet().forEach(set -> System.out.println(set.getKey() + " " + set.getValue()));
//
//     System.out.println(PropertyUtils.getCharacters());

//     System.out.println("Game Init!");
//
//     List<String> questions = new ArrayList<>();
//             PropertyUtils.getQuestions().entrySet().stream().forEach(set -> questions.add(set.getValue()));
//     List<String> guessess = PropertyUtils.getCharacters();
//
     Game game = new RandomGame();
     game.addPlayer(new RandomPlayer("Test1"));
     game.addPlayer(new RandomPlayer("Test2"));
     game.addPlayer(new RandomPlayer("Test3"));
     game.addPlayer(new RandomPlayer("Test4"));
     game.addPlayer(new RandomPlayer("Test5"));
     game.assignCharacters();
     game.initGame();
     while (!game.isFinished()) {
         boolean turnResult = game.makeTurn();
         while (turnResult) {
             turnResult = game.makeTurn();
         }
         game.changeTurn();
     }

     //System.out.println(PropertyUtils.getCharacters());


//     Map<String, String> guessQuestions = PropertyUtils.getQuestions();
//     System.out.println(guessQuestions.size());
//     System.out.println(new Random().nextInt(guessQuestions.size() + 1));
//     System.out.println(guessQuestions.remove(String.valueOf(5)));
//     System.out.println(guessQuestions.size());
//     System.out.println(new Random().nextInt(2));

    }
}
