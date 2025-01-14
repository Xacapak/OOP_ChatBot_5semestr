package org.example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class SimpleChatBot extends JFrame implements ActionListener {

    final String TITLE_OF_PROGRAM = "AI Stiv: чат-бот.";
    final int START_LOCATION = 200;
    final int WINDOW_WIDTH = 350;
    final int WINDOW_HEIGHT = 450;

    JTextArea dialogue; // область для диалога
    JCheckBox ai;       // включить/отключить AI
    JTextField message; // поле для ввода сообщений
    SimpleBot sbot;     // класс чат-бота

    public static void main(String[] args) {
        new SimpleChatBot();
    }

    SimpleChatBot(){
        setTitle(TITLE_OF_PROGRAM);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(START_LOCATION, START_LOCATION, WINDOW_WIDTH, WINDOW_HEIGHT);

        // область для диалога
        dialogue = new JTextArea();
        dialogue.setLineWrap(true);
        JScrollPane scrollBar = new JScrollPane(dialogue);

        // панель для флажка, поля сообщения и кнопки
        JPanel bp = new JPanel();
        bp.setLayout(new BoxLayout(bp, BoxLayout.X_AXIS));
        ai = new JCheckBox("AI");
        //ai.doClick();
        message = new JTextField();
        message.addActionListener(this);
        JButton enter = new JButton("Enter");
        enter.addActionListener(this);

        // добавление всех элементов в окно
        bp.add(ai);
        bp.add(message);
        bp.add(enter);
        add(BorderLayout.CENTER, scrollBar);
        add(BorderLayout.SOUTH, bp);
        setVisible(true);
        sbot = new SimpleBot();
    }

    /**
     * Получатель событий из поля сообщения и кнопки ввода
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (message.getText().trim().length() > 0) {
            dialogue.append(message.getText() + "\n");
            dialogue.append(TITLE_OF_PROGRAM.substring(0, 9) +
                            sbot.sayInReturn(message.getText(), ai.isSelected()) + "\n");
        }
        message.setText("");
        message.requestFocusInWindow();
    }
}