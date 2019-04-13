package com.company;

import javax.swing.*;

public class Client_Test {
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1");    //local host - 127.0.0.1
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.startRunning();
    }
}
