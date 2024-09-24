package code.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try (Socket clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("=================================");
            System.out.println("  Connected to the chat server   ");
            System.out.println("=================================");

            // Thread to read messages from the server
            Thread readThread = new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = reader.readLine()) != null) {
                        // Format server messages distinctly
                        System.out.println("\n[SERVER] " + serverMessage);
                        System.out.print("> ");  // Indicate user can type a message
                    }
                } catch (IOException e) {
                    System.out.println("[ERROR] Error reading from server: " + e.getMessage());
                }
            });
            readThread.start();

            // Ask the client for a name
            System.out.print("Enter your name: ");
            String clientName = scanner.nextLine();
            writer.write(clientName + "\n");
            writer.flush();

            // Send messages to the server
            String userInput;
            while (true) {
                System.out.print("> ");  // Prompt for user input
                userInput = scanner.nextLine();
                if (userInput.equalsIgnoreCase("exit") || userInput.equalsIgnoreCase("quit")) {
                    break;
                }
                writer.write(userInput + "\n");
                writer.flush();
            }

            System.out.println("[INFO] You have left the chat.");

        } catch (IOException e) {
            System.out.println("[ERROR] Client error: " + e.getMessage());
        }
    }
}
