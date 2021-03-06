package com.company;

import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;

public class Server extends JFrame{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;

    public static void main(String[] args) {
        Server server = new Server();
        server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        server.startRunning();
    }

    public Server(){
        super("Simple chat - server");
        userText = new JTextField();
        userText.setEditable(false);
        add(userText, BorderLayout.NORTH);
        userText.addActionListener(
                e -> {
                    System.out.println("asdasdads");
                }
        );
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(400,500);
        setVisible(true);
    }

    public void startRunning(){
        try{
            server = new ServerSocket(1111, 100);
            while (true){
                try{
                    waitForConnection();
                    setUpStreams();
                    whileChatting();
                }
                catch (EOFException eofExcept){
                    showMessage("\nServer ended the connection! ");
                }
                finally {
                    closeConnection();
                    dispose();
                }
            }
        }
        catch (IOException ioExcept){
            ioExcept.printStackTrace();
        }
    }

    private void waitForConnection() throws IOException{
        showMessage("Waiting for client . . . \n ");
        connection = server.accept();
        showMessage("Connected to: "+connection.getInetAddress().getHostName());
    }
    private void setUpStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\nStreams are setup now! ");
    }
    private void whileChatting() throws IOException{
        String message = "You are now connected!";
        sendMessage(message);
        ableToType(true);
        do{
            try{
                message = (String) input.readObject();
                showMessage("\n" + message);
            }
            catch (ClassNotFoundException clfExcept){
                showMessage(" \nI don't know what that user send!");
            }
        }while(!message.equals("CLIENT: exit"));
    }
    private void closeConnection(){
        showMessage("\nClosing connections . . . \n");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }
        catch (IOException ioExcept){
            ioExcept.printStackTrace();
            dispose();
        }
    }
    private void sendMessage(String message){
        try{
            output.writeObject("SERVER: " + message);
            output.flush();
            showMessage("\nSERVER: " + message);
        }
        catch (IOException ioExcept){
            chatWindow.append("\nOoops can't send that message!");
        }
    }
    private void showMessage(final String text){
        SwingUtilities.invokeLater(
                () -> {
                    ableToEditChatWindow(true);
                    chatWindow.append(text);
                    ableToEditChatWindow(false);
                }
        );
    }
    private void ableToType(final boolean decide){
        SwingUtilities.invokeLater(
                () -> userText.setEditable(decide)
        );
    }
    private void ableToEditChatWindow(final boolean decide){
        SwingUtilities.invokeLater(
                () -> chatWindow.setEditable(decide)
        );
    }
}