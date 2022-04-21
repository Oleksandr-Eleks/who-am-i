package com.eleks.academy.whoami.networking.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

class Client {

    public static void main(String args[]) throws Exception {

        Socket clientSocket = new Socket("localhost", 888);

        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

        BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));

        String str, str1;

        System.out.println("Enter your message:");

        while (!(str = kb.readLine()).equals("exit")) {

            dos.writeBytes(str + "\n");

            str1 = br.readLine();

            System.out.println(str1);
        }

        dos.close();
        br.close();
        kb.close();
        clientSocket.close();
    }
}
