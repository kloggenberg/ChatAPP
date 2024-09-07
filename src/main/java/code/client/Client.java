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

            System.out.println("Client connected to the server.");

            // Thread to read messages from the server
            Thread readThread = new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = reader.readLine()) != null) {
                        System.out.println("Server: " + serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Error reading from server: " + e.getMessage());
                }
            });
            readThread.start();

            // Thread to send messages to the server
            String userInput;
            while (true) {
                System.out.print("Enter message to send (type 'exit' to quit): ");
                userInput = scanner.nextLine();
                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }
                writer.write(userInput + "\n");
                writer.flush();
            }

            System.out.println("Client disconnected.");

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}
