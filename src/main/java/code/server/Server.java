package code.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {
    private static ServerSocket serverSocket;
    private static  boolean running = true;
    private static Scanner scanner = new Scanner(System.in);

    private static void startServer() throws IOException {
        while (running) {
            serverSocket = new ServerSocket(5000);
            System.out.println("Server has been launched . . .");
        }
        System.out.println("Server has been shutting down . . .");
    }

    public static void main(String[] args) throws IOException {
        startServer();

        System.out.println("Please enter a command:");
        String userInput = scanner.nextLine();

        switch (userInput) {
            case "exit":
            case "quit":
            case "stop":
                running = false;
            default:
                System.out.println("Invalid command");
        }
    }
}
