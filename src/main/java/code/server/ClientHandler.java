package code.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String clientName;

    public ClientHandler(Socket socket) throws IOException {
        this.clientSocket = socket;
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    }

    @Override
    public void run() {
        try {
            // Ask for client name
            writer.write("Please enter your name: ");
            writer.flush();
            this.clientName = reader.readLine();
            Server.broadcast(clientName + " has joined the chat.");

            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println(clientName + ": " + message);
                // Broadcast message to other clients
                Server.broadcast(clientName + ": " + message);
            }
        } catch (IOException e) {
            System.out.println("Error with client: " + e.getMessage());
        } finally {
            System.out.println(clientName + " has disconnected.");
            close();
            Server.broadcast(clientName + " has left the chat.");
        }
    }

    public void sendMessage(String message) {
        try {
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println("Failed to send message: " + e.getMessage());
        }
    }

    public void close() {
        try {
            reader.close();
            writer.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Failed to close client connection: " + e.getMessage());
        }
    }
}
