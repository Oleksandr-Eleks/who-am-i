package com.eleks.academy.whoami.networking.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.eleks.academy.whoami.core.Player;

public class ClientPlayer implements Player {

	private String name;
	private Socket socket;
	private BufferedReader reader;
	private PrintStream writer;

	public Boolean IsPlayerConnected = true;

	public ClientPlayer(String name, Socket socket, List<String> availableQuestions)
			throws IOException {
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
			writer.println("It's yout turn to ask... What'd you ask? ");
			question = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return question;
	}

	@Override
	public String answerQuestion(String question, String character, String askingPlayerName) {
		String playerAnswer = "";
		try {
			writer.println(
					"Answer " + askingPlayerName + "'s player question: " + question + "Character is:" + character);
			playerAnswer = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (playerAnswer == null) {
			this.IsPlayerConnected = false;
			return playerAnswer;
		}

		System.out.println("Player: " + name + ". Answers: " + playerAnswer);

		return playerAnswer;
	}

	@Override
	public String getGuess() {
		String guess = "Unknown";
		try {
			writer.println("Enter your guess question");
			guess = reader.readLine();
		} catch (IOException e) {

		}

		if (guess == null) {
			this.IsPlayerConnected = false;
			return guess;
		}

		writer.println("Player " + name + ". Guesses: Am I " + guess);
		System.out.println("Player " + name + ". Guesses: Am I " + guess);
		return guess;
	}

	@Override
	public boolean isReadyForGuess() {
		String userPrompt = "";
		try {
			writer.println("Are yot ready to guess?");
			userPrompt = reader.readLine();
		} catch (IOException e) {
			return false;
		}

		if (userPrompt == null) {
			this.IsPlayerConnected = false;
			return false;
		}

		return userPrompt.equalsIgnoreCase("yes");
	}

	@Override
	public String answerGuess(String guess, String character, String askingPlayerName) {
		String playersGuess = "";
		if (guess == null) {
			return guess;
		}

		try {
			writer.println("Player " + askingPlayerName + " asks: Am I " + guess + "? He is " + character
					+ ". Write your answer: ");
			playersGuess = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (playersGuess == null) {
			this.IsPlayerConnected = false;
			return playersGuess;
		}

		System.out.println("Player " + name + " answers: " + playersGuess);
		return playersGuess;
	}

	@Override
	public Boolean IsPlayerConnected() {
		return IsPlayerConnected;
	}

}
