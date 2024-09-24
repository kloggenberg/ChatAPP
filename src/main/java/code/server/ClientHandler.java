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
            // First message is the client's name
            this.clientName = reader.readLine();

            String message;
            while ((message = reader.readLine()) != null) {
                // Broadcast message to other clients
                Server.broadcast(message, clientName);
            }
        } catch (IOException e) {
            System.out.println("Error with client: " + e.getMessage());
        } finally {
            close();
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
