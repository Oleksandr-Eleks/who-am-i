package com.eleks.academy.whoami.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class ContextConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
		Resource[] resources = new ClassPathResource[] {};
		pspc.setLocations(resources);
		pspc.setIgnoreUnresolvablePlaceholders(true);
		return pspc;
	}

	@Bean
	ServerProperties serverProperties(@Value("${server.port}") Integer port,
			@Value("${game.players}") Integer players) {
		return new ServerProperties(port, players);
	}
}
