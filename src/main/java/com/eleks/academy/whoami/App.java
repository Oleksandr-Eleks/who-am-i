package com.eleks.academy.whoami;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.eleks.academy.whoami.configuration.ContextConfig;
import com.eleks.academy.whoami.configuration.ServerProperties;
import com.eleks.academy.whoami.core.Game;
import com.eleks.academy.whoami.networking.client.ClientPlayer;
import com.eleks.academy.whoami.networking.server.Server;
import com.eleks.academy.whoami.networking.server.ServerImpl;

public class App {

	public static void main(String[] args) throws IOException {
		ApplicationContext context = new AnnotationConfigApplicationContext(ContextConfig.class);
		ServerProperties properties = context.getBean(ServerProperties.class);
		Server server = context.getBean(Server.class);

		Game game = server.startGame();
		game.play();
	}

}
