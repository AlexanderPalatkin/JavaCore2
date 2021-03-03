import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientGUI extends JFrame {
    private final JPanel chatPanel = new JPanel();
    private final JPanel loginPanel = new JPanel();
    private final JTextArea textArea = new JTextArea();
    private final JTextArea clientsInformation = new JTextArea();
    private final JLabel textInputLabel = new JLabel("Your message :");
    private final JTextField textInput = new JTextField();
    private final JScrollPane js = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private final JScrollPane jsClients = new JScrollPane(clientsInformation, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private final JButton sendButton = new JButton("Send");
    // Это чат для приватных сообщений, делаем выпадающий список чтобы
    // можно было выбрать кому отправить сообщение
    private String receiver;
    private final String all = "all";
    private String[] clients = {all};
    private final JComboBox<String> selectClient = new JComboBox<>(clients);
    //при нажатии enter или кнопки submit
    private final ActionListener listener = event -> {
        String message = textInput.getText(); // получаем введенный текст
        // если input не пустой то отправляем сообщение
        if (!message.isEmpty()) {
            if (receiver != null && !receiver.equalsIgnoreCase(all)) {
                // если выбран конечный получатель, то добавляем /w и имя получателя к
                // сообщению
                message = "/w " + receiver + " " + message;
            }
            this.clientNetwork.sendMessage(message);
            textInput.setText("");
            selectClient.setSelectedItem("all");
            receiver = null;
        }
    };
    private final ClientNetwork clientNetwork = new ClientNetwork();


    // конструктор ClientGUI
    public ClientGUI() {
        /*
         * this.clientNetwork.setCallOnMsgRecieved(new Callback<String>() {
         *
         * @Override public void callback(String message){ textArea.append(message); }
         * });
         */
        setCallBacks(); // имплементируем callback методы Callback-ov
        setMainFrame(); // имплементируем основное окно
        initilizeChatPanel(); // инициализируем панель для чата
        initializeLoginJPanel(); // инициализируем панель для авторизации
        this.clientNetwork.connect(); // запускаем подключение к серверу
        this.setVisible(true);
    }

    private void setMainFrame() {
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(new FlowLayout());
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                clientNetwork.sendMessage("/end"); // при закрытии окна отправляем серверу сообщение об отключении
                super.windowClosing(event);
            }
        });
    }

    private void initilizeChatPanel() {
        textArea.setEditable(false);
        clientsInformation.setEditable(false);
        chatPanel.setBackground(Color.white);
        chatPanel.setPreferredSize(new Dimension(490, 490));
        js.setPreferredSize(new Dimension(450, 350));
        jsClients.setPreferredSize(new Dimension(450, 35));
        chatPanel.add(jsClients);
        chatPanel.add(js);
        textInput.setPreferredSize(new Dimension(150, 25));
        chatPanel.add(textInputLabel);
        chatPanel.add(textInput);
        textInput.addActionListener(listener);
        sendButton.addActionListener(listener);
        selectClient.addActionListener(e -> { //здесь мы записываем в список всех клиентов, чтобы можно было выбрать
            // кому послать сообщение
            receiver = selectClient.getSelectedItem().toString();
        });
        JLabel toWho = new JLabel("to");
        chatPanel.add(toWho);
        chatPanel.add(selectClient);
        chatPanel.add(sendButton);
        chatPanel.setVisible(false);
        this.add(chatPanel);
    }

    private void initializeLoginJPanel() {
        loginPanel.setBackground(Color.white);
        loginPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        loginPanel.setPreferredSize(new Dimension(300, 150));
        loginPanel.setBorder(BorderFactory.createTitledBorder("Authorization"));
        JTextField login = new JTextField();
        JLabel loginLabel = new JLabel("Your connection identifier : ");
        JLabel passwordLabel = new JLabel("Your password : ");
        JPasswordField password = new JPasswordField();
        login.setPreferredSize(new Dimension(100, 25));
        password.setPreferredSize(new Dimension(100, 25));
        loginPanel.add(loginLabel);
        loginPanel.add(login);
        loginPanel.add(passwordLabel);
        loginPanel.add(password);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(280, 35));
        buttonPanel.setBackground(Color.WHITE);
        JButton button = new JButton("Submit");
        buttonPanel.add(button);
        button.addActionListener(e -> { // при нажатии на кнопку submit серверу запрос на авторизацию
            clientNetwork.sendMessage("/auth " + login.getText() + " " + String.valueOf(password.getPassword()));
            login.setText("");// спрашиваем данные логина и пароля
            password.setText("");
        });
        loginPanel.add(buttonPanel);
        loginPanel.setVisible(true);
        this.add(loginPanel);
    }

    // имплиментируем нужные нам Callback-i
    // t.e. говорим ClientNetwork, какое поведение мы ждем от него в какой ситуации
    private void setCallBacks() {
        //при получении сообщения от сервера добавляем его в textArea
        this.clientNetwork.setCallOnMsgRecieved(message -> textArea.append(message + "\n"));
        // при получении нового списка клиентов
        this.clientNetwork.setCallOnChangeClientList(clientsList -> {
            clientsInformation.setText(clientsList);// печатаем всех клиентов в окошке клиентов
            clients = (all + " "+ clientsList).split("\\s"); // создаем таблицу из присутствующих клиентов + варинт "все"
        // для combobox
            selectClient.setModel(new DefaultComboBoxModel(clients));// передаем данные combobox
        });
        // при успешной авторизации мы прячем loginPanel и делаем видимой chatPanel
        this.clientNetwork.setCallOnAuth(s -> {
            loginPanel.setVisible(false);
            chatPanel.setVisible(true);
        });
        // при сообщении об ошибке показываем pop-up
        this.clientNetwork.setCallOnError(message -> JOptionPane.showMessageDialog(null, message, "We have a problem",
                JOptionPane.ERROR_MESSAGE));
    }

}