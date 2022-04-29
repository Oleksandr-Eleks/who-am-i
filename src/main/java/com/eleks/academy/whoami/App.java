package com.eleks.academy.whoami;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.eleks.academy.whoami.configuration.ContextConfig;
import com.eleks.academy.whoami.configuration.ServerProperties;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.server.Server;

public class App {

	public static void main(String[] args) throws IOException, InterruptedException {
		ApplicationContext context = new AnnotationConfigApplicationContext(ContextConfig.class);
		ServerProperties properies = context.getBean(ServerProperties.class);
		Server server = context.getBean(Server.class);
		
		server.waitForPlayers();
		
		try {
			Game game = server.startGame();
			game.init();
		} finally {
			server.stopServer();
			server.stop();
		}
	}
}
