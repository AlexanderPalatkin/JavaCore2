import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private List<ClientHandler> clients;
    private AtomicInteger number = new AtomicInteger(1);

    public Server() {
        this.clients = new ArrayList<>();
        try(ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Server is listening");
            while(true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(number.getAndIncrement(), this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadCastMessage(String message) {
        for(ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    // Решение домашней работы 7.
    public void broadCastMessageToChat(ClientHandler cl, String message) {
        for(ClientHandler client : clients) {
            if(client.equals(cl)){
                client.sendMessage(message);
            }
            else client.sendMessage(client.getNickname() + " : " + message);
        }
    }

    public void broadCastClientsList() {
        StringBuilder sb = new StringBuilder(15 * clients.size());
        sb.append("/clients ");
        for(ClientHandler o : clients){
            sb.append(o.getNickname()).append(" ");
        }
        String out = sb.toString();
        broadCastMessage(out);
    }

    public void subscribe(ClientHandler client) {
        clients.add(client);
        broadCastClientsList();
    }

    public void unsubscribe(ClientHandler client) {
        clients.remove(client);
        broadCastClientsList();
    }
}
