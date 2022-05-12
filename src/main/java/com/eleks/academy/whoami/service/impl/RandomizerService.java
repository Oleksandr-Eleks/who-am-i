package com.eleks.academy.whoami.service.impl;

import java.util.List;

public class RandomizerService {
    public static String getRandomString(List<String> data){
        int randomPos = (int)(Math.random() * data.size());
        String name = data.get(randomPos);
        return name;
    }
    public static String removeRandomString(List<String> data){
        int randomPos = (int)(Math.random() * data.size());
        String name = data.remove(randomPos);
        return name;
    }
}