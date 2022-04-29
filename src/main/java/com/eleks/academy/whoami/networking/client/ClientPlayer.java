package com.eleks.academy.whoami.networking.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
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
	
	public ClientPlayer(Socket socket) throws IOException {
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new PrintStream(socket.getOutputStream());
	}

	@Override
	public Future<String> getName() {
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
	public Future<String> getCharacter() {
		return executor.submit(this::askForCharacterFromClient);
	}

	private String askForCharacterFromClient() {
		String character = "";
		try {
			writer.println("Enter your character:");
			writer.flush();
			character = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return character;
	}

	@Override
	public Future<String> getQuestion() {
		return executor.submit(this::askForQuestionFromClient);
	}

	private String askForQuestionFromClient() {
		try {
			writer.println("Ask your question:");
			writer.flush();
			return reader.readLine();
		} catch (IOException e) {
			System.err.printf("Cannot get an answer on question from a player. Assuming 2. (%s)%n", e.getMessage());
			return "getQuestionFail";
		}
	}

	@Override
	public boolean answerQuestion(String question, String playerName, String character) {
		String answer = "";

		try {
			writer.println("Answer " + playerName + " question: " + question + " (Character is: " + character + ")");
			writer.flush();
			answer = reader.readLine();
		} catch (IOException e) {
			System.err.printf("Cannot get a question from a player. Assuming 2. (%s)%n", e.getMessage());
			e.printStackTrace();
		}

		return answer.toLowerCase().contentEquals("yes");
	}

	@Override
	public boolean isReadyForGuess() {
		String answer = "";

		try {
			answer = sendAndGetMessage("Are you ready to guess? [yes|no]");
		} catch (IOException e) {
			System.err.printf("Cannot check is player ready to guess. Assuming 2. (%s)%n", e.getMessage());
			e.printStackTrace();
		}

		return answer.toLowerCase().equals("yes");
	}

	@Override
	public String getGuess() {
		String guess = "";

		try {
			guess = sendAndGetMessage("Write your guess: ");
			System.out.println(" guesses: Am I " + guess);
		} catch (IOException e) {
			System.err.printf("Cannot get a guess from a player. Assuming 2. (%s)%n", e.getMessage());
			e.printStackTrace();
		}
		return guess;
	}

	@Override
	public boolean answerGuess(String guess, String playerName, String character) {
		String answer = "";

		try {
			sendAndGetMessage(playerName + " guess -> " + guess + " (Character: " + character + ")");
		} catch (IOException e) {
			System.err.printf("Cannot get an answer on guess from a player. Assuming 2. (%s)%n", e.getMessage());

		}

		return answer.toLowerCase().contentEquals("yes");
	}

	private String sendAndGetMessage(String messageToSend) throws IOException {
		String messageToGet = "";

		writer.println(messageToSend);
		writer.flush();
		messageToGet = reader.readLine();

		return messageToGet;
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
