package com.eleks.academy.whoami.networking.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
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
    void init() throws UnknownHostException {
        localHost = InetAddress.getLocalHost();
        port = randomPort();
    }

    @Test
    void clientReadsCharacterFromSocket() throws IOException, InterruptedException {
        CountDownLatch clientReady = new CountDownLatch(1);
        CountDownLatch timeout = new CountDownLatch(2);

        try (ServerSocket server = new ServerSocket()) {
            server.bind(new InetSocketAddress(localHost, port));

            Thread t1 = new Thread(() -> {
                try (Socket client = new Socket(localHost, port);
                     PrintWriter writer = new PrintWriter(client.getOutputStream())) {
                    writer.println("test character");
                    writer.flush();
                    clientReady.countDown();
                    timeout.countDown();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            });
            t1.start();

            Thread t2 = new Thread(() -> {
                try (Socket client = server.accept();
                     ClientPlayer player = new ClientPlayer(client)) {
                    // TODO: refactor test to always fail after 5 seconds
                    boolean success = clientReady.await(5, TimeUnit.SECONDS);
                    assertTrue(success);
                    String character = player.suggestCharacter().get(5, TimeUnit.SECONDS);
                    assertEquals("test character", character);
                    timeout.countDown();
                } catch (IOException | TimeoutException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            });
            t2.start();
            boolean timeOver = timeout.await(5, TimeUnit.SECONDS);
            if (!timeOver) {
                t1.interrupt();
                t2.interrupt();
            }
            assertTrue(timeOver);
        }
    }

    @Test
    void clientReadsPlayersNameFromSocket() throws IOException, InterruptedException {
        CountDownLatch clientReady = new CountDownLatch(1);
        CountDownLatch nameAppeared = new CountDownLatch(1);
        CountDownLatch timeout = new CountDownLatch(2);

        try (ServerSocket server = new ServerSocket()) {
            server.bind(new InetSocketAddress(localHost, port));

            Thread t1 = new Thread(() -> {
                try (Socket client = new Socket(localHost, port);
                     PrintWriter writer = new PrintWriter(client.getOutputStream())) {
                    clientReady.countDown();
                    writer.println("Player");
                    writer.flush();
                    nameAppeared.await(5, TimeUnit.SECONDS);
                    timeout.countDown();
                } catch (IOException | InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            });
            t1.start();
            Thread t2 = new Thread(() -> {
                try (Socket client = server.accept();
                     ClientPlayer player = new ClientPlayer(client)) {
                    // TODO: refactor test to always fail after 5 seconds
                    boolean success = clientReady.await(5, TimeUnit.SECONDS);
                    assertTrue(success);
                    String character = player.getName().get(5, TimeUnit.SECONDS);
                    assertEquals("Player", character);
                    nameAppeared.countDown();
                    timeout.countDown();
                } catch (IOException | TimeoutException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            });
            t2.start();
            boolean timeOver = timeout.await(5, TimeUnit.SECONDS);
            if (!timeOver) {
                t1.interrupt();
                t2.interrupt();
            }
            assertTrue(timeOver);
        }
    }

    private int randomPort() {
        return ((int) (Math.random() * (65535 - 49152)) + 49152);
    }

}
