package code.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    private static ServerSocket serverSocket;
    private static boolean running = true;
    private static Scanner scanner = new Scanner(System.in);
    private static List<ClientHandler> clients = new ArrayList<>();

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

                        // Create a new ClientHandler for the connected client
                        ClientHandler clientHandler = new ClientHandler(clientSocket);
                        synchronized (clients) {
                            clients.add(clientHandler);
                        }
                        Thread clientThread = new Thread(clientHandler);
                        clientThread.start();

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
                        shutdown();
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

    public synchronized static void broadcast(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.sendMessage(message);
            }
        }
    }

    private synchronized static void shutdown() throws IOException {
        running = false;
        broadcast("Server is shutting down.");
        for (ClientHandler client : clients) {
            client.close();
        }
        if (!serverSocket.isClosed()) {
            serverSocket.close();
        }
        System.out.println("Server has been shut down . . .");
    }

    public static void main(String[] args) throws IOException {
        startServer();
    }
}
