package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    static final int PORT = 2222;

    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        TransactionManager transactionManager = new TransactionManager();
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();

        }
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            // new thread for a client
            new ClientHandler(socket, transactionManager).start();
        }
    }
}