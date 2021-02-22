package lesson06;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        startTextServer();
    }

    public static void startTextServer() {
        try (ServerSocket serverSocket = new ServerSocket(9537)) {
            System.out.println("Server waiting...");
            try (Socket socket = serverSocket.accept();
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream());
                 Scanner scanner = new Scanner(System.in)
            ) {
                System.out.println("Client connected...");
                String servMessage = "";
                Thread clientReader = new Thread(() -> {
                    String clientMessage = "";
                    try {
                        while (!socket.isClosed()) {
                            clientMessage = in.readLine();
                            if (clientMessage != null)
                                System.out.println(clientMessage);
                        }
                    } catch (SocketException e) {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                clientReader.start();
                do {
                    servMessage = scanner.nextLine();
                    out.println("Server: " + servMessage);
                    out.flush();
                } while (!servMessage.equalsIgnoreCase("stop"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
