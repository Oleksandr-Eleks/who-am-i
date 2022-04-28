package com.eleks.academy.whoami.networking.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientPlayerIT {
    InetAddress localHost;
    int port;

    @BeforeEach
    void prepareHost() throws UnknownHostException {
        localHost = InetAddress.getLocalHost();
        port = randomPort();
    }

    @Test
    void clientReadsCharacterFromSocket() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        CountDownLatch clientReady = new CountDownLatch(1);
        try (ServerSocket server = new ServerSocket()) {
            server.bind(new InetSocketAddress(localHost, port));

            new Thread(() -> {
                try (Socket client = new Socket(localHost, port);
                     PrintWriter writer = new PrintWriter(client.getOutputStream())) {
                    clientReady.countDown();
                    Thread.sleep(6000);
                    writer.println("test character");
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();

            try (Socket client = server.accept();
                 ClientPlayer player = new ClientPlayer(client)) {

                boolean success = clientReady.await(5, TimeUnit.SECONDS);
                assertTrue(success);
                String character = player.suggestCharacter().get(5, TimeUnit.SECONDS);
                assertEquals("test character", character);
            }
        }
    }

    @Test
    void clientReadsPlayersNameFromSocket() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        CountDownLatch clientReady = new CountDownLatch(1);
        CountDownLatch nameAppeared = new CountDownLatch(1);

        try (ServerSocket server = new ServerSocket()) {
            server.bind(new InetSocketAddress(localHost, port));

            new Thread(() -> {
                try (Socket client = new Socket(localHost, port);
                     PrintWriter writer = new PrintWriter(client.getOutputStream())) {
                    Thread.sleep(6000);
                    clientReady.countDown();
                    writer.println("Player");
                    writer.flush();
                    nameAppeared.await(5, TimeUnit.SECONDS);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();

            try (Socket client = server.accept();
                 ClientPlayer player = new ClientPlayer(client)) {
                boolean success = clientReady.await(5, TimeUnit.SECONDS);
                assertTrue(success);
                String name = player.getName().get(5, TimeUnit.SECONDS);
                assertEquals("Player", name);
                nameAppeared.countDown();
            }
        }
    }

    @Test
    void notAddedPlayerWhenFailedSuggestion() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        CountDownLatch clientReady = new CountDownLatch(1);
        try (ServerSocket server = new ServerSocket()) {
            server.bind(new InetSocketAddress(localHost, port));

            new Thread(() -> {
                try (Socket client = new Socket(localHost, port)) {
                    clientReady.countDown();
                    Thread.sleep(6000);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();

            try (Socket client = server.accept();
                 ClientPlayer player = new ClientPlayer(client)) {

                clientReady.await(5, TimeUnit.SECONDS);

                String character = player.suggestCharacter().get(5, TimeUnit.SECONDS);
                assertEquals("test character", character);
            }
        }
    }

    private int randomPort() {
        return ((int) (Math.random() * (65535 - 49152)) + 49152);
    }

}
