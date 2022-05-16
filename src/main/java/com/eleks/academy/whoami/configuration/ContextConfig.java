package com.eleks.academy.whoami.configuration;

import com.eleks.academy.whoami.networking.server.Server;
import com.eleks.academy.whoami.networking.server.ServerImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@EnableConfigurationProperties(ServerProperties.class)
public class ContextConfig {

    @Bean
    Server server(ServerProperties serverProperties) throws IOException {
        return new ServerImpl(serverProperties.port(), serverProperties.players());
    }

}