package com.company;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame {
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "", serverIP;
    private Socket connection;

    public Client(String host){

    }
}
