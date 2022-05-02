package com.eleks.academy.whoami;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.eleks.academy.whoami.configuration.ContextConfig;
import com.eleks.academy.whoami.configuration.ServerProperties;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.server.Server;

public class App {

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(ContextConfig.class);
		ServerProperties properties = context.getBean(ServerProperties.class);
		Server server = context.getBean(Server.class);

		Game game;
		try {
			game = server.startGame();
			game.init();
		} catch (IOException e) {
			System.err.println(String.format("Fail to start a game (%s)", e.getMessage()));
		}
		
		server.stop();
		
	}
}
