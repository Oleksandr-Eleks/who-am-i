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
	private String question;
	private String character;

	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	public ClientPlayer(Socket socket) throws IOException {
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new PrintStream(socket.getOutputStream());
	}

	@Override
	public Future<String> getName() {
		// TODO: save name for future
		return executor.submit(this::askName);
	}

	@Override
	public Future<String> getQuestion() {
		return executor.submit(this::askQuestion);
	}

	@Override
	public Future<String> answerQuestion(String question, String character) {
		this.question = question;
		this.character = character;
		return executor.submit(this::doAnswerQuestion);
	}

	@Override
	public Future<String> getGuess() {
		return executor.submit(this::askGuess);
	}

	@Override
	public Future<Boolean> isReadyForGuess() {
		return executor.submit(this::askIsReadyForGuess);
	}

	@Override
	public Future<String> answerGuess(String guess, String character) {
		return executor.submit(this::doAnswerGuess);
	}

	@Override
	public Future<String> suggestCharacter() {
		return executor.submit(this::doSuggestCharacter);
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

	private String doSuggestCharacter() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	private void close(AutoCloseable closeable) {
		try {
			closeable.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String askGuess() {
		String answer = "";

		try {
			writer.println("Write your guess: ");
			answer = reader.readLine();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return answer;
	}

	private String askName() {
		try {
			writer.println("Please, name yourself.");
			this.name = reader.readLine();
			if (this.name.equals("")) {
				this.name = "Player " + Math.random() * 10;
			}
			writer.println("Your name: " + this.name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.name;
	}

	private String askQuestion() {
		String question = "";

		try {
			writer.println("Ask your question: ");
			question = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return question;
	}

	private String doAnswerQuestion() {
		String answer = "";
		try {
			writer.println("Answer second player question: " + this.question + "Character is:" + this.character);
			answer = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return answer;
	}

	private String doAnswerGuess() {
		String answer = "";

		try {
			writer.println("Write your answer: ");
			answer = reader.readLine();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return answer;
	}

	private boolean askIsReadyForGuess() {
		String answer = "";

		try {
			writer.println("Are you ready to guess? ");
			answer = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return answer.equals("Yes");
	}

}
