import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private String nickname;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Server server;

    public ClientHandler(Server server, Socket socket) {
        try {
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> { // поток для клиента
                boolean continueChat = true;
                String msg = "";
                boolean isAuthorized = false;
                try {
                    long start = System.currentTimeMillis();
                    while (!isAuthorized && continueChat) { // цикл авторизации


                         if((System.currentTimeMillis() + start) > 120000){ continueChat =
                         false;
                         sendMessage("/error timeout");} else
                         if(in.available()>0) {

                        msg = in.readUTF(); // читаем сообщение от клиента
                        if (msg.startsWith("/auth")) { // если начинается с /auth, то пытаемся его авторизировать
                            String[] tokens = msg.split("\\s");
                            nickname = server.getAuthService().getNicknameByLoginAndPassword(tokens[1], tokens[2]);
                            if (nickname != null) { // если авторизация, то шлем клиенту authok и выходим из цикла
                                // авторизации
                                isAuthorized = true;
                                sendMessage("/authok");
                                server.subscribe(this);
                            } else {
                                sendMessage("/error"); // иначе пишем клиенту об ошибке (по хорошему тут можно еще
                                // текст ошибки писать, а на клиенте его ловить и печатать. Если кто то хочет, то может добавить это в чат, не будет
                                // не будет получаться -> пишите в телеграм
                            }
                        }
                        if (msg.equalsIgnoreCase("/end")) {// клиент решил выйти без авторизации s
                            continueChat = false;
                        }
                    }
                    }

                    while (continueChat) { // цикл чтения сообщений клиента
                        msg = in.readUTF();
                        if (msg.startsWith("/")) {// специальное сообщение
                            if (msg.equalsIgnoreCase("/end")) { // клиент вышел
                                continueChat = false;
                            } else if (msg.startsWith("/w")) { //приватное сообщение ДЗ7
                                String[] tokens = msg.split("\\s", 3);
                                System.out.println(tokens);
                                if (tokens.length == 3) {
                                    System.out.println(tokens);
                                    server.privateMsg(this, tokens[1], tokens[2]);
                                }
                            }
                        } else {// обычное сообщение
                            server.broadcastMessageToChat(this, msg);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    disconnect();
                }

            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);// пишем сообщение клиенту
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void disconnect() { // отключаемся
        sendMessage("/end");
        System.out.println("disconnected " + nickname);
        server.unsubscribe(this);
        try {
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}