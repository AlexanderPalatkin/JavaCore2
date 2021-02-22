package lesson06;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        startTextClient();
    }

    public static void startTextClient() {
        try (Socket socket = new Socket("localhost", 9537);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Connected...");
            String myMessage = "";
            Thread serverReader = new Thread(() -> {
                String serverMessage = "";
                try {
                    while (!socket.isClosed()) {
                        serverMessage = in.readLine();
                        if (serverMessage != null)
                            System.out.println(serverMessage);
                    }
                } catch (SocketException e) {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            serverReader.start();
            do {
                myMessage = scanner.nextLine();
                out.println("Client: " + myMessage);
                out.flush();
            } while (!myMessage.equalsIgnoreCase("stop"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
