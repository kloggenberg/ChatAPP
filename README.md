# Chat Application

A simple chat application using Java sockets, allowing clients to communicate with each other or in group chats. This project consists of a server and client implementation.

## Overview

- **Server**: Manages multiple client connections and handles message broadcasting.
- **Client**: Connects to the server, sends, and receives messages.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 11 or higher.
- An IDE or text editor of your choice.

### Running the Server

1. Open a terminal and navigate to the `code/server` directory.
2. Compile the server code:
    ```bash
    javac -d bin src/code/server/Server.java src/code/server/ClientHandler.java
    ```
3. Run the server:
    ```bash
    java -cp bin code.server.Server
    ```
4. The server will start listening on port 5000 for client connections.

### Running the Client

1. Open another terminal and navigate to the `code/client` directory.
2. Compile the client code:
    ```bash
    javac -d bin src/code/client/Client.java
    ```
3. Run the client:
    ```bash
    java -cp bin code.client.Client
    ```
4. The client will connect to the server, and you can start sending messages.

## Usage

### Server Commands

- **`exit`, `quit`, `stop`**: Shuts down the server and closes all client connections.

### Client Commands

- **Send Messages**: Type a message and press Enter to send it to the server.
- **Exit**: Type `exit` to disconnect from the server.
