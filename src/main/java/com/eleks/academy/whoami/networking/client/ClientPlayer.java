package com.eleks.academy.whoami.networking.client;

import com.eleks.academy.whoami.core.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.*;

public class ClientPlayer implements Player, AutoCloseable {
    private String name = "";
    private BufferedReader reader;
    private PrintStream writer;
    private String suggestedCharacter = "";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ClientPlayer(Socket socket) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintStream(socket.getOutputStream());
    }

    @Override
    public Future<String> getName() {
        return executor.submit(this::askName);
    }

    private String askName() {
        if (name.isBlank()) {
            try {
                writer.println("Please, name yourself.");
                name = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return name;
    }

    @Override
    public Future<String> getQuestion() {
        Callable<String> askQuestion = () -> {
            String question = "";

            try {
                writer.println("Ask your question: ");
                question = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return question;
        };
        return executor.submit(askQuestion);
    }

    @Override
    public Future<String> answerQuestion(String question, String character) {
        Callable<String> answerQuestion = () -> {
            String answer = "";
            try {
                writer.println("Answer second player question: " + question + "Character is:" + character);
                answer = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return answer;
        };
        return executor.submit(answerQuestion);
    }

    @Override
    public Future<String> getGuess() {
        Callable<String> guess = () -> {
            String answer = "";

            try {
                writer.println("Write your guess: ");
                answer = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return answer;
        };
        return executor.submit(guess);
    }

    @Override
    public Future<Boolean> isReadyForGuess() {
        Callable<Boolean> isReadyForGuess = () -> {
            String answer = "";

            try {
                writer.println("Are you ready to guess? ");
                answer = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return answer.equals("Yes");
        };
        return executor.submit(isReadyForGuess);
    }

    @Override
    public Future<String> answerGuess(String guess, String character) {
        Callable<String> answerGuess = () -> {
            String answer = "";

            try {
                writer.println("Write your answer: ");
                answer = reader.readLine();
            } catch (IOException e) {

                e.printStackTrace();
            }
            return answer;
        };
        return executor.submit(answerGuess);
    }

    @Override
    public Future<String> suggestCharacter() {
        return executor.submit(this::doSuggestCharacter);
    }

    private String doSuggestCharacter() {
        if (suggestedCharacter.isBlank()) {
            try {
                return reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        return suggestedCharacter;
    }

    @Override
    public void close() {
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
