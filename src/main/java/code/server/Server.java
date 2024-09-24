package code.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
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
    private static final String HISTORY_FILE = "chat_history.json";  // File to store chat history
    private static List<Message> messageHistory = new ArrayList<>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static void startServer() throws IOException {
        try {
            serverSocket = new ServerSocket(5000);
            System.out.println("Server has been launched . . .");

            // Start a new thread to listen for client connections
            Thread connectionThread = new Thread(() -> {
                while (running) {
                    try {
                        Socket clientSocket = serverSocket.accept();

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
                    case "dump":
                        dumpChatHistory();
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

    public synchronized static void broadcast(String message, String sender) {
        Message newMessage = new Message(sender, message);
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.sendMessage(message);
            }
        }
        saveMessage(newMessage);
    }

    private static void saveMessage(Message message) {
        messageHistory.add(message);
        try (FileWriter fileWriter = new FileWriter(HISTORY_FILE, false)) {
            gson.toJson(messageHistory, fileWriter);
        } catch (IOException e) {
            System.out.println("Error saving message history: " + e.getMessage());
        }
    }

    private synchronized static void dumpChatHistory() {
        try (FileReader fileReader = new FileReader(HISTORY_FILE)) {
            List<Message> history = gson.fromJson(fileReader, List.class);
            if (history == null || history.isEmpty()) {
                System.out.println("No chat history available.");
            } else {
                System.out.println("\n===== Chat History =====");
                for (Message message : history) {
                    System.out.println(message.getSender() + ": " + message.getContent());
                }
                System.out.println("========================");
            }
        } catch (IOException e) {
            System.out.println("Error reading chat history: " + e.getMessage());
        }
    }

    private synchronized static void shutdown() throws IOException {
        running = false;
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
