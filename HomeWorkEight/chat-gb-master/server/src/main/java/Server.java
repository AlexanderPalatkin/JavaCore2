import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<ClientHandler> clients;
    private AuthService authService = new DBAuthService();

    public AuthService getAuthService() {
        return authService;
    }

    public Server() {
        this.clients = new ArrayList<>();
        try (ServerSocket serverSocket = new ServerSocket(8189)) {// запускаем сервер
            System.out.println("Server is listening on 8189");
            while (true) {
                Socket socket = serverSocket.accept(); // ждем клиентов
                new ClientHandler(this, socket); // после того как клиент подключился создаем для него ClientHandler
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message) {
        for (ClientHandler client : clients) { // рассылаем сообщение всем подписанным клиентам
            client.sendMessage(message);
        }
    }

    public void broadcastMessageToChat(ClientHandler clientHandler, String message) { // 7 задание
        for (ClientHandler client : clients) {
            if (client == clientHandler) {
                client.sendMessage(message);
            }
            else client.sendMessage(clientHandler.getNickname() + " : " + message);
        }
    }

    public void broadcastClientsList() { // рассылаем клиентам список имен всех клиентам
        StringBuilder sb = new StringBuilder(15 * clients.size());
        sb.append("/clients ");
        for (ClientHandler o : clients) {
            sb.append(o.getNickname()).append(" ");
        }
        String out = sb.toString();
        broadcastMessage(out);
    }

    public void subscribe(ClientHandler client) { //добавляем подключившегося клиента в список
        clients.add(client);
        broadcastClientsList();
    }

    public void unsubscribe(ClientHandler client) { // удаляем клиента из списка после отключения
        clients.remove(client);
        broadcastClientsList();
    }

    public void privateMsg(ClientHandler sender, String receiverNick, String msg) { // приватное сообщение
        if (sender.getNickname().equals(receiverNick)) { //если получатель равен отправителю
            sender.sendMessage("Note for myself: " + msg);
            return;
        }
        for (ClientHandler receiver : clients) {
            if (receiver.getNickname().equals(receiverNick)) {
                receiver.sendMessage("from " + sender.getNickname() + ": " + msg);
                sender.sendMessage("for " + receiverNick + ": " + msg);
                return;
            }
        }
        sender.sendMessage("Client " + receiverNick + " is not found");
    }
}