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

	private final BufferedReader reader;
	private final PrintStream writer;
	private final Socket socket;
	private String name = "";

	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	public ClientPlayer(Socket socket) throws IOException {
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new PrintStream(socket.getOutputStream());
	}

	@Override
	public Future<String> getName() {
		return executor.submit(this::askName);
	}

	private String askName() {
		try {
			writer.println("Please, name yourself.");
			name = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return name;
	}

	@Override
	public String getNameOnly() {
		return this.name;
	}

	@Override
	public Future<String> getQuestion() {
		return executor.submit(this::askQuestion);
	}

	private String askQuestion() {
		String question = "";

		try {
			writer.println("Ask your questinon: ");
			question = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return question;
	}

	@Override
	public Future<String> answerQuestion(String question, String character) {
		return executor.submit(() -> doAnswerQuestion(question, character));
	}

	private String doAnswerQuestion(String question, String character) {
		String answer = "";

		try {
			writer.println("Answer second player question: " + question + "Character is:"+ character);
			answer = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
	}

	@Override
	public Future<String> getGuess() {
		return executor.submit(this::tryGuess);
	}

	private String tryGuess() {
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
	public Future<Boolean> isReadyForGuess() {
		return executor.submit(this::pIsReadyForGuess);
	}

	private boolean pIsReadyForGuess() {
		String answer = "";

		try {
			writer.println("Are you ready to guess? ");
			answer = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return answer.equals("Yes") ? true : false;
	}

	@Override
	public Future<String> answerGuess(String guess, String character) {
		return executor.submit(() -> doAnswerGuess(guess, character));
	}
	private String doAnswerGuess(String guess, String character) {
		String answer = "";

		try {
			writer.println("Write your answer: ");
			answer = reader.readLine();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return answer;
	}

	@Override
	public Future<String> suggestCharacter() {
		return executor.submit(this::doSuggestCharacter);
	}

	private String doSuggestCharacter() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
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
