package com.eleks.academy.whoami.service_client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {
        Socket localhost = new Socket("localhost", 888);
        try (DataOutputStream dataOutputStream = new DataOutputStream(localhost.getOutputStream());
             BufferedReader br = new BufferedReader(new InputStreamReader(localhost.getInputStream()));
             BufferedReader kb = new BufferedReader(new InputStreamReader(System.in))) {
            String str, str1;
            System.out.println("Enter your name");
            while (!(str = kb.readLine()).equals("exit")) {
                dataOutputStream.writeBytes(str + "\n");
                str1 = br.readLine();
                System.out.println(str1);
            }
        }

    }
}
