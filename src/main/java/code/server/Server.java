package code.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static ServerSocket serverSocket;
    private static boolean running = true;
    private static Scanner scanner = new Scanner(System.in);

    private static void startServer() throws IOException {
        try {
            serverSocket = new ServerSocket(5000);
            System.out.println("Server has been launched . . .");

            // Start a new thread to listen for client connections
            Thread connectionThread = new Thread(() -> {
                while (running) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("New client connected at: " + clientSocket.getInetAddress().getHostAddress());
                        // Here you can handle the client connection (e.g., pass it to a ClientHandler)
                    } catch (IOException e) {
                        if (running) {
                            System.out.println("Error accepting client connection: " + e.getMessage());
                        }
                    }
                }
            });
            connectionThread.start();

            // Keep the main thread open to listen for user input
            while (running) {
                System.out.println("Please enter a command:");
                String userInput = scanner.nextLine();

                switch (userInput.toLowerCase()) {
                    case "exit":
                    case "quit":
                    case "stop":
                        running = false;
                        serverSocket.close();
                        break;
                    default:
                        System.out.println("Invalid command");
                }
            }

        } catch (IOException e) {
            // Handle exceptions
            System.out.println("Server error: " + e.getMessage());
        } finally {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("Server has been shut down . . .");
        }
    }

    public static void main(String[] args) throws IOException {
        startServer();
    }
}
