package code.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private static PrintWriter logWriter = new PrintWriter(System.out, true); // For logging messages (optional)

    public ClientHandler(Socket socket) throws IOException {
        this.clientSocket = socket;
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = reader.readLine()) != null) {
                System.out.println("Message from client: " + message);
                // Broadcast message to other clients or process it
                Server.broadcast(message);
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