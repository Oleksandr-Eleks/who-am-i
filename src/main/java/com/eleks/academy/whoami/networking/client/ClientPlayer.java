package com.eleks.academy.whoami.networking.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.eleks.academy.whoami.core.Player;

public class ClientPlayer implements Player, AutoCloseable {

	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	private final BufferedReader reader;
	private final PrintStream writer;
	private final Socket socket;
	private String name;
	private String question;
	private String guess;

	public ClientPlayer(Socket socket) throws IOException {
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new PrintStream(socket.getOutputStream());
	}

	@Override
	public Future<String> askName() {
		return executor.submit(this::askForNameFromClient);
	}

	private String askForNameFromClient() {
		try {
			writer.println("Enter your name:");
			writer.flush();
			name = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Future<String> askCharacter() {
		return executor.submit(this::askForCharacterFromClient);
	}

	private String askForCharacterFromClient() {
		String character = "";
		try {
			clearBuffer();
			writer.println("Enter your character:");
			writer.flush();
			character = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return character;
	}

	@Override
	public Future<String> askQuestion() {
		return executor.submit(this::askForQuestionFromClient);
	}

	private String askForQuestionFromClient() {
		try {
			clearBuffer();
			writer.println("Ask your question:");
			writer.flush();
			question = reader.readLine();
			System.out.println("[" + name + "] asks: Am I " + question);
		} catch (IOException e) {
			System.err.printf("Cannot get an answer on question from a player. Assuming 2. (%s)%n", e.getMessage());
		}
		return question;
	}

	@Override
	public String getQuestion() {
		return question;
	}

	@Override
	public Future<String> askGuess() {
		return executor.submit(this::askForGuessFromClient);
	}

	private String askForGuessFromClient() {
		try {
			clearBuffer();
			writer.println("Write your guess: ");
			writer.flush();
			guess = reader.readLine();
			System.out.println("[" + name + "] guesses: Am I " + guess + " ?");
		} catch (IOException e) {
			System.err.printf("Cannot get a guess from a player. Assuming 2. (%s)%n", e.getMessage());
		}
		return guess;
	}

	@Override
	public String getGuess() {
		return guess;
	}

	@Override
	public Future<String> answerQuestion(String playerName, String question, String character) {
		String answer = "";
		try {
			clearBuffer();
			writer.println("Answer [yes|no] to " + playerName + " question: " + question + " (Character: " + character + ")");
			writer.flush();
			answer = reader.readLine().toLowerCase();
		} catch (IOException e) {
			System.err.printf("Cannot get a question from a player. Assuming 2. (%s)%n", e.getMessage());
			e.printStackTrace();
		}
		return CompletableFuture.completedFuture(answer);
	}

	@Override
	public Future<String> answerGuess(String playerName, String guess, String character) {
		String answer = "";
		try {
			clearBuffer();
			writer.println("Answer [yes|no] to " + playerName + " guess: " + guess + " (Character: " + character + ")");
			writer.flush();
			answer = reader.readLine().toLowerCase();
		} catch (IOException e) {
			System.err.printf("Cannot get an answer on guess from a player. Assuming 2. (%s)%n", e.getMessage());
		}
		return CompletableFuture.completedFuture(answer);
	}

	@Override
	public Future<String> isReadyForGuess() {
		String answer = "";
		try {
			clearBuffer();
			writer.println("Are you ready to guess? [yes|no]");
			writer.flush();
			answer = reader.readLine().toLowerCase();
		} catch (IOException e) {
			System.err.printf("Cannot check is player ready to guess. Assuming 2. (%s)%n", e.getMessage());
		}
		return CompletableFuture.completedFuture(answer);
	}

	private void clearBuffer() throws IOException {
		while (reader.ready()) {
			reader.readLine();
		}

	}

	@Override
	public void close() {
		executor.shutdown();
		try {
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		close(reader);
		close(writer);
		close(socket);
	}

	private void close(AutoCloseable closeable) {
		try {
			closeable.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
