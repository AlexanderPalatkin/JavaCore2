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

    public ClientHandler(Integer number, Server server, Socket socket) {
        try {
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.nickname = "Client" + number;
            new Thread(() -> {
                server.subscribe(this);
                boolean goOn = true;
                String msg = null;
                while (goOn) {
                    try {
                        msg = in.readUTF();
                        if (msg.startsWith("/")) {
                            if (msg.equalsIgnoreCase("/end")) {
                                goOn = false;
                                disconnect();
                            }
                        } else server.broadCastMessageToChat(this, msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void disconnect() {
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
