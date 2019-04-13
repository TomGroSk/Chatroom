package com.company;
import java.io.EOFException;
import java.io.IOException;
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
        super("Simple chat - client");
        serverIP = host;
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
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(300,150);
        setVisible(true);
    }

    public void startRunning(){
        try{
            connectToServer();
            setUpStreams();
            whileChatting();
        }
        catch (EOFException eofExcept){
            showMessage("\n Client terminated connection");
        }
        catch (IOException ioExcept){
            ioExcept.printStackTrace();
        }
        finally {
            closeCrap();
        }
    }

    private void connectToServer() throws IOException{
        showMessage("\n Attempting connection . . .");
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("\n Connected to:" + connection.getInetAddress().getHostName());
    }

    private void setUpStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams are now good!");
    }

    private void whileChatting() throws IOException{
        ableToType(true);
        do{
            try{
                message = (String) input.readObject();
                showMessage("\n" + message);
            }
            catch (ClassNotFoundException cntExcept){
                showMessage("\n I dont know that type object!");
            }
        }while (!message.equals("CLIENT: END"));
    }

    private void closeCrap(){
        showMessage("\n Closing down . . .");
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
            output.writeObject("CLIENT: " + message);
            output.flush();
            showMessage("\nCLIENT: " + message);
        }
        catch (IOException ioExcept){
            chatWindow.append("\n Something gone wrong! :(");
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
