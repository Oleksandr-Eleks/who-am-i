package com.eleks.academy.whoami.utils;

import com.eleks.academy.whoami.constants.Constants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class PropertyUtils {

    private static FileInputStream getPropertyFile(String nameFile) throws FileNotFoundException {
        return new FileInputStream(Path.of("src", "main", "resources", nameFile).toFile());
    }

    public static Map<String, String> readPropertyFile(String nameFile) {

        Properties properties = new Properties();
        Map<String, String> mapQuestions = new HashMap();
        try (FileInputStream file = getPropertyFile(nameFile)) {
            properties.load(file);

            for (String name : properties.stringPropertyNames())
                mapQuestions.put(name, properties.getProperty(name));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mapQuestions;
    }

    public static List<String> getCharacters() {

        Properties properties = new Properties();
        List<String> listCharacters = new ArrayList<>();
        try (FileInputStream file = getPropertyFile(Constants.CHARACTERS)) {
            properties.load(file);

            for (String name : properties.stringPropertyNames())
                listCharacters.add(name.replace('.', ' '));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listCharacters;


    }

    public static Map<String, List<String>> getCharactersWithQuestions(String nameFile) {
        return readPropertyFile(nameFile).entrySet().stream().collect(Collectors.toMap(key -> key.getKey().replace('.', ' '), values -> Arrays.asList(values.getValue().split("\\s*,\\s*"))));
    }

}
