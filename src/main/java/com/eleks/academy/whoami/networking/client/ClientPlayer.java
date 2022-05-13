package com.eleks.academy.whoami.networking.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.*;

import com.eleks.academy.whoami.core.Player;

public class ClientPlayer implements Player, AutoCloseable {

	private final BufferedReader reader;
	private final PrintStream writer;
	private final Socket socket;

	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	public ClientPlayer(Socket socket) throws IOException {
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new PrintStream(socket.getOutputStream());
	}

	@Override
	public Future<String> getName() {
		String name = "";

		try {
			writer.println("Please, name yourself.");
			name = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return CompletableFuture.completedFuture(name);
	}

	@Override
	public Future<String> getQuestion() {
		String question = "";

		try {
			writer.println("Ask your questinon: ");
			question = reader.readLine();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return CompletableFuture.completedFuture(question);
	}

	@Override
	public Future<String> answerQuestion(String question, String character) {
		String answer = "";

		try {
			writer.printf("Answer second player question: %s Character is: %s", question, character);
			answer = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return CompletableFuture.completedFuture(answer);
	}

	@Override
	public Future<String> getGuess() {
		String answer = "";

		try {
			writer.println("Write your guess: ");
			answer = reader.readLine();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return CompletableFuture.completedFuture(answer);
	}

	@Override
	public Future<Boolean> isReadyForGuess() {
		String answer = "";

		try {
			writer.println("Are you ready to guess? ");
			answer = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return CompletableFuture.completedFuture(answer.equals("Yes") ? true : false);
	}

	@Override
	public Future<String> answerGuess(String guess, String character) {
		String answer = "";

		try {
			writer.println("Write your answer: ");
			answer = reader.readLine();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return CompletableFuture.completedFuture(answer);
	}

	@Override
	public Future<String> suggestCharacter() {
		String character = "";
		try {
			writer.println("Write a character name for a next player: ");
			character = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return CompletableFuture.completedFuture(character);
	}

	@Override
	public void close() {
		executor.shutdown();
		try {
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		close(writer);
		close(reader);
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
