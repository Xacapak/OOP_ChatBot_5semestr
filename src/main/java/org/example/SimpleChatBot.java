package org.example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Класс, представляющий графическое окно чат-бота с использованием Swing.
 * Содержит компонент для отображения диалога и обработки пользовательского ввода.
 */
class SimpleChatBot extends JFrame implements ActionListener {

    // Константы для настроек окна программы
    private static final String TITLE_OF_PROGRAM = "AI ассистент Стив";
    private static final int START_LOCATION = 200;
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 600;

    private JTextArea dialogue; // Область для вывода диалога
    private JCheckBox ai;       // Флажок для включения/выключения режима ИИ
    private JTextField message; // Поле для ввода сообщений
    private SimpleBot sbot;     // Экземпляр класса чат-бота

    // Старт программы
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimpleChatBot::new); // Использование invokeLater для создания интерфейса в потоке GUI
    }

    // Конструктор окна чат-бота
    public SimpleChatBot(){
        // Настройка основного окна
        setTitle(TITLE_OF_PROGRAM);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(START_LOCATION, START_LOCATION, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Создание компонента для отображения диалога
        dialogue = new JTextArea();
        dialogue.setLineWrap(true); // Включение автоматического переноса текста
        dialogue.setEditable(false); // Запрет на редактирование текста в области диалога
        JScrollPane scrollBar = new JScrollPane(dialogue); // Добавление полосы прокрутки

        // Создание панели для флажка (AI), поля ввода и кнопки "Enter"
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS)); // Горизонтальное расположение элементов

        ai = new JCheckBox("AI"); // Флажок для включения режима ИИ
        message = new JTextField(); // Поле для ввода текста
        message.addActionListener(this); // Обработчик событий для нажатия клавиши Enter в поле ввода
        
        JButton enterButton = new JButton("Enter"); // Кнопка отправки сообщения
        enterButton.addActionListener(this); // Обработчик нажатия на кнопку

        // Добавление элементов управления в панель
        inputPanel.add(ai);
        inputPanel.add(message);
        inputPanel.add(enterButton);

        // Добавление элементов на окно
        add(BorderLayout.CENTER, scrollBar); // Область диалога в центр
        add(BorderLayout.SOUTH, inputPanel); // Панель управления в низ окна
        setVisible(true); // Отображение окна

        sbot = new SimpleBot(); // Создание экземпляра чат-бота
    }

    /**
     * Обработчик событий для кнопки Enter и ввода текста в поле сообщения.
     * Отправляет сообщение в чат-бот и отображает ответ.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        String userMessage = message.getText().trim(); // Получаем введенное сообщение, убираем лишние пробелы

        if (userMessage.length() > 0) {
            // Добавляем введенное сообщение в область диалога
            dialogue.append("Вы: " + userMessage + "\n");

            // Получаем ответ от чат-бота
            String botResponse = sbot.sayInReturn(userMessage, ai.isSelected());
            dialogue.append("Стив ai: " + botResponse + "\n");
        }

        // Очищаем поле ввода и устанавливаем фокус на него
        message.setText("");
        message.requestFocusInWindow();
    }
}