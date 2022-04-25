package com.eleks.academy.whoami.networking.client;

import com.eleks.academy.whoami.core.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientPlayer implements Player {

	private String name;
	private String character;
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
	public void setCharacter(String character) {
		this.character = character;
	}

	@Override
	public String getCharacter() {
		return this.character;
	}

	@Override
	public int getAssumptionOrClarification() {
		int answer = -1;

		try {
			writer.println("Assumption(0) or Clarification(1)?");
			answer = Integer.valueOf(reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
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
	public String answerQuestion(String question, String character) {
		String answer = "";

		try {
			writer.println("Answer player question: " + question + " Character is: " + character);
			answer = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
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
	public String answerGuess(String guess, String character) {
		String answer = "";

		try {
			writer.println("Answer player question: Am I " + guess + "? Character is: " + character);
			answer = reader.readLine();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return answer;
	}
}
