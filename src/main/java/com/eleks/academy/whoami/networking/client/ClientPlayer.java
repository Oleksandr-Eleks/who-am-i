package com.eleks.academy.whoami.networking.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import com.eleks.academy.whoami.core.Player;

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

			this.writer.println("Ask your questinon: ");
			System.out.println("Ask your questinon: ");

			question = reader.readLine();

			System.out.println("Player " + this.name + ": " + question);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return question;
	}

	@Override
	public Socket getPlayerSocket() {
		return this.socket;
	}

	@Override
	public String getGuess() {
		String answer = "";

		try {

			this.writer.println("Write your guess: ");
			System.out.println("Write your guess: ");

			answer = reader.readLine();

			System.out.println("Player " + this.name + ": " + answer);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
	}

	@Override
	public String answerQuestion(String question, String character) {
		String answer = "";

		try {

			this.writer.println("Answer" + this.name + " player question: " + question + " Character is:" + character);
			System.out.println("Answer" + this.name + " player question: " + question + " Character is:" + character);

			answer = reader.readLine();

			System.out.println("Player " + this.name + ": " + answer);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return answer;
	}

	@Override
	public boolean isReadyForGuess() {
		String answer = "";

		try {
			this.writer.println("Are you ready to guess? ");
			System.out.println("Are you ready to guess? ");

			answer = reader.readLine();

			System.out.println("Player " + this.name + ": " + answer);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return answer.equals("Yes") ? true : false;
	}

	@Override
	public String answerGuess(String guess, String character) {
		String answer = "";

		try {

			this.writer.println("Write your answer: ");
			System.out.println("Write your answer: ");

			answer = reader.readLine();

			System.out.println("Player " + this.name + ": " + answer);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return answer;
	}

}
