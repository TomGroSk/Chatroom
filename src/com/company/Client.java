package com.company;
import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;

public class Client extends JFrame {
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "", serverIP;
    private Socket connection;

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1");    //local host - 127.0.0.1
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.startRunning();
    }

    public Client(String host){
        super("Simple chat - client");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                e -> {
                    sendMessage(e.getActionCommand());
                    userText.setText("");
                }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(400,500);
        setVisible(true);
    }

    public void startRunning(){
        try{
            connectToServer();
            setUpStreams();
            whileChatting();
        }
        catch (EOFException eofExcept){
            showMessage("\nClient terminated connection");
        }
        catch (IOException ioExcept){
            ioExcept.printStackTrace();
        }
        finally {
            closeConnection();
            dispose();
        }
    }

    private void connectToServer() throws IOException{
        showMessage("Attempting connection . . .");
        connection = new Socket(InetAddress.getByName(serverIP), 1111);
        showMessage("\nConnected to:" + connection.getInetAddress().getHostName());
    }

    private void setUpStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\nStreams are now good!");
    }

    private void whileChatting() throws IOException{
        ableToType(true);
        do{
            try{
                message = (String) input.readObject();
                showMessage("\n" + message);
            }
            catch (ClassNotFoundException cntExcept){
                showMessage("\nI don't know that type of object!");
            }
        }while (!message.equals("SERVER: exit"));
    }

    private void closeConnection(){
        showMessage("\nClosing down . . .");
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
            chatWindow.append("\nSomething's gone wrong!");
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