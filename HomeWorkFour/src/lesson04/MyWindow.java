package lesson04;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyWindow extends JFrame {

    public MyWindow() {
        //Окно приложения
        setTitle("Text chat");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(700, 300, 400, 600);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        //Панель окна чата
        JPanel chatField = new JPanel();
        chatField.setLayout(new BorderLayout());
        chatField.setPreferredSize(new Dimension(400, 1200));

        JTextArea chatFieldText = new JTextArea();
        chatFieldText.setEditable(false);

        JScrollPane chatScroll = new JScrollPane(chatFieldText);
        chatField.add(chatScroll);
        add(chatField, BorderLayout.PAGE_START);

        //Панель ввода и отправки текста
        JPanel textInputPanel = new JPanel();
        textInputPanel.setLayout(new BorderLayout());
        textInputPanel.setPreferredSize(new Dimension(400, 80));

        JTextField textInputField = new JTextField();
        JScrollPane textInputScroll = new JScrollPane(textInputField);
        textInputPanel.add(textInputScroll, BorderLayout.CENTER);

        // Класс для отправки сообщений, использую для отправки с кнопки sendText и Enter
        class SendMessageListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textInput = textInputField.getText();
                String textField = chatFieldText.getText();
                if (textField == null || textField.isEmpty()) {
                    chatFieldText.setText(textInput);
                } else {
                    chatFieldText.setText(textField + "\n" + textInput);
                }
                textInputField.setText("");
            }
        }

        textInputField.addActionListener(new SendMessageListener());

        JButton sendText = new JButton("Send message");
        textInputPanel.add(sendText, BorderLayout.LINE_END);
        add(textInputPanel, BorderLayout.PAGE_END);
        sendText.addActionListener(new SendMessageListener());



        // Творческий процесс, не смог остановиться и добавил менюшку.
        JMenuBar chatMenu = new JMenuBar();
        JMenu mNew = new JMenu("New chat");
        JMenuItem miChatNew = new JMenuItem("New");
        JMenuItem miChatExit = new JMenuItem("Exit");
        setJMenuBar(chatMenu);
        mNew.add(miChatNew);
        mNew.addSeparator();
        mNew.add(miChatExit);
        chatMenu.add(mNew);

        miChatNew.addActionListener(e -> chatFieldText.setText(""));

        miChatExit.addActionListener(e -> System.exit(0));

        JMenu mEdit = new JMenu("Edit chat");
        JMenuItem miChatEdit = new JMenuItem("Edit text");
        JMenuItem miChatBlockEdit = new JMenuItem("Block edit text");
        mEdit.add(miChatEdit);
        mEdit.addSeparator();
        mEdit.add(miChatBlockEdit);
        chatMenu.add(mEdit);

        miChatEdit.addActionListener(e -> chatFieldText.setEditable(true));

        miChatBlockEdit.addActionListener(e -> chatFieldText.setEditable(false));

        setVisible(true);
    }
}
