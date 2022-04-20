package com.eleks.academy.whoami.networking.client;

import com.eleks.academy.whoami.core.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientPlayer implements Player {

    private String name;
    private Socket socket;
    private BufferedReader reader;
    private PrintStream writer;

    public ClientPlayer(String name, Socket socket) throws IOException {
        this.name = name;
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintStream(socket.getOutputStream());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getQuestion() {
        String question = "";
        try {
            writer.println(name + ", ask your question: ");
            System.out.println(name + ", ask your question: ");
            question = reader.readLine();
            System.out.println(name + ": " + question);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return question;
    }

    @Override
    public String answerQuestion(String question, String character) {
        String answer = "";
        try {
            writer.println("Answer " + name + " player question: " + question + " Character is:" + character);
            System.out.println("Answer " + name + " player question: " + question + " Character is:" + character);
            answer = reader.readLine();
            System.out.println(name + ": " + answer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return answer;
    }

    @Override
    public String getGuess() {
        String answer = "";
        try {
            writer.println(name + ", write your guess: ");
            System.out.println(name + ", write your guess: ");
            answer = reader.readLine();
            System.out.println(name + ": " + answer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return answer;
    }

    @Override
    public boolean isReadyForGuess() {
        String answer = "";
        try {
            writer.println(name + ", are you ready to guess? ");
            System.out.println(name + ", are you ready to guess? ");
            answer = reader.readLine();
            System.out.println(name + ": " + answer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return answer.equals("Yes") ? true : false;
    }

    @Override
    public String answerGuess(String guess, String character) {
        String answer = "";
        try {
            writer.println(name + ", write your answer: ");
            System.out.println(name + ", write your answer: ");
            answer = reader.readLine();
            System.out.println(name + ": " + answer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return answer;
    }
}
