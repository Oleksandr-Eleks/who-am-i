package com.eleks.academy.whoami.networking.client;

import com.eleks.academy.whoami.core.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.*;

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
	public String getName() {
		// TODO: save name for future
		try {
			return executor.submit(this::askName).get();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	private String askName() {
		String name = "";

		try {
			writer.println("Please, name yourself.");
			name = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return name;
	}

	public String doGetQuestion() {
		String question = "";

		try {
			writer.println("Ask your questinon: ");
			question = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return question;
	}

	public String doAnswerQuestion(String question, String character) {
		String answer = "";

		try {
			writer.println("Answer second player question: " + question + "Character is:" + character);
			answer = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return answer;
	}

	public String doGetGuess() {
		String answer = "";


		try {
			writer.println("Write your guess: ");
			answer = reader.readLine();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return answer;
	}

	public Boolean doReadyForGuess() {
		String answer = "";

		try {
			writer.println("Are you ready to guess? ");
			answer = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return answer.equals("Yes");
	}

	public String doAnswerGuess(String guess, String character) {
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

	@Override
	public Future<String> getQuestion() {
		return executor.submit(this::doGetQuestion);
	}

	@Override
	public Future<String> answerQuestion(String question, String character) {
		return executor.submit(() -> this.doAnswerQuestion(question, character));
	}

	@Override
	public Future<String> getGuess() {
		return executor.submit(this::doGetGuess);
	}

	@Override
	public Future<Boolean> isReadyForGuess() {
		return executor.submit(this::doReadyForGuess);
	}

	@Override
	public Future<String> answerGuess(String guess, String character) {
		return executor.submit(() -> this.doAnswerGuess(guess, character));
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
