package com.company;
import javax.swing.*;

public class Main {

        public static void main(String[] args) {
            Server server = new Server();
            server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            server.startRunning();

        }
}
