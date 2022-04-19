package com.eleks.academy.whoami.networking.client;

import com.eleks.academy.whoami.core.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientPlayer implements Player {

    private final String name;
    private final BufferedReader reader;
    private final PrintStream writer;

    public ClientPlayer(String name, Socket socket) throws IOException {
        this.name = name;
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
            writer.println("Ask your question: ");
            question = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return question;
    }

    @Override
    public String answerQuestion(String name, String question, String character) {
        String answer = "";

        try {
            writer.println(name + " ask a question: " + question + " (His character is " + character + ") Yes/No");
            do {
                answer = reader.readLine();
            }
            while (isNotCorrectAnswer(answer));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return answer.toUpperCase();
    }

    @Override
    public String getGuess() {
        String answer = "";
        try {
            writer.println("Write your guess: ");
            answer = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    @Override
    public boolean isReadyForGuess() {
        String answer = "";

        try {
            writer.println("Are you ready to guess? ");
            do {
                answer = reader.readLine();
            }
            while (isNotCorrectAnswer(answer));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer.equalsIgnoreCase("yes");
    }

    @Override
    public String answerGuess(String name, String guess, String character) {
        String answer = "";

        try {
            writer.println(name + " answer: I think it is " + guess + ". That is true?   Yes/No");
            do {
                answer = reader.readLine();
            }
            while (isNotCorrectAnswer(answer));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer.toUpperCase();
    }

    @Override
    public void sendMessage(String message) {
        writer.println(message);
    }

    private boolean isNotCorrectAnswer(String answer) {
        if (!answer.equalsIgnoreCase("yes") && !answer.equalsIgnoreCase("no")) {
            sendMessage("\"" + answer + "\"" + " is not correct. Correct: Yes/No");
            return true;
        }
        return false;
    }
}
