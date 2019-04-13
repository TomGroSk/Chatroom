package com.company;
import javax.swing.*;

public class Main {

        public static void main(String[] args) {
//            Server server = new Server();
//            server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            server.startRunning();


            Client client = new Client("127.0.0.1");
            client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.startRunning();
        }
}
