package com.company;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;

    public Server(){
        super("Simple chat - server");
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        sendMessage(e.getActionCommand());
                        userText.setText("");
                    }
                }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(300,150);
        setVisible(true);
    }

    public void startRunning(){
        try{
            server = new ServerSocket(6789, 100);
            while (true){
                try{
                    waitForConnection();
                    setUpStreams();
                    whileChatting();
                }
                catch (EOFException eofExcept){
                    showMessage("\n Server ended the connection! ");
                }
                finally {
                    closeCrap();
                }
            }
        }
        catch (IOException ioExcept){
            ioExcept.printStackTrace();
        }
    }

    private void waitForConnection() throws IOException{
        showMessage(" Waiting for someone to connect . . . \n ");
        connection = server.accept();
        showMessage(" Connected to: "+connection.getInetAddress().getHostName());
    }
    private void setUpStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams are setup now! ");
    }
    private void whileChatting() throws IOException{
        String message = "You are now connected!";
        sendMessage(message);
        ableToType(true);
        do{
            try{
                message = (String) input.readObject();
                showMessage("\n"+message);
            }
            catch (ClassNotFoundException clfExcept){
                showMessage(" \n I don't know what that user send!");
            }
        }while(!message.equals("SERVER: END"));
    }
    private void closeCrap(){
        showMessage("\n Closing connections . . . \n");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }
        catch (IOException ioExcept){
            ioExcept.printStackTrace();
        }
    }
    private void sendMessage(String message){
        try{
            output.writeObject("SERVER: "+ message);
            output.flush();
            showMessage("\nSERVER: "+ message);
        }
        catch (IOException ioExcept){
            chatWindow.append("\n  Ooops can't send that message!");
        }
    }
    private void showMessage(final String text){
        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    chatWindow.append(text);
                }
            }
        );
    }
    private void ableToType(final boolean decide){
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        userText.setEditable(decide);
                    }
                }
        );
    }
}
