package org.example;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;

/**
 * Класс, представляющий графическое окно чат-бота с использованием Swing.
 * Содержит компонент для отображения диалога и обработки пользовательского ввода.
 */
class SimpleChatBot extends JFrame implements ActionListener {

    // Константы для настроек окна программы
    private static final String TITLE_OF_PROGRAM = "AI ассистент Стив";
    private static final int START_LOCATION = 200;
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 800;
    private static final String HISTORY_FILE = "chat_history.txt";

    private JTextArea dialogue; // Область для вывода диалога
    private JCheckBox ai;       // Флажок для включения/выключения режима ИИ
    private JTextField message; // Поле для ввода сообщений
    private SimpleBot sbot;     // Экземпляр класса чат-бота
    private ArrayList<String> history; // для хранения истории сообщений

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
        setLocationRelativeTo(null);

        setIconImage(new ImageIcon(getClass().getResource("/images/icon_ai.png")).getImage());

        // Создание компонента для отображения диалога
        dialogue = new JTextArea();
        dialogue.setLineWrap(true); // Включение автоматического переноса текста
        dialogue.setEditable(false); // Запрет на редактирование текста в области диалога

        // Кастомный цвет и шрифт для диалога
        dialogue.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
        dialogue.setBackground(new Color(30, 30, 30));
        dialogue.setForeground(new Color(0, 255, 0));

        JScrollPane scrollBar = new JScrollPane(dialogue); // Добавление полосы прокрутки

        // Создание панели для флажка (AI), поля ввода и кнопки "Enter"
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS)); // Горизонтальное расположение элементов
        inputPanel.setBackground(new Color(50, 50, 50));

        ai = new JCheckBox("AI"); // Флажок для включения режима ИИ
        ai.setBackground(new Color(50, 50, 50));
        ai.setForeground(Color.WHITE);
        ai.setFont(new Font("Bookman Old Style", Font.BOLD, 12));

        message = new JTextField(); // Поле для ввода текста
        message.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
        message.setBackground(new Color(20, 20, 20));
        message.setForeground(Color.WHITE);
        message.setCaretColor(Color.GREEN);
        message.addActionListener(this); // Обработчик событий для нажатия клавиши Enter в поле ввода
        
        JButton enterButton = new JButton("Enter"); // Кнопка отправки сообщения
        enterButton.setFont(new Font("Bookman Old Style", Font.BOLD, 14));
        enterButton.setBackground(new Color(40, 40, 40));
        enterButton.setForeground(Color.WHITE);
        enterButton.addActionListener(this); // Обработчик нажатия на кнопку

        // Добавление элементов управления в панель
        inputPanel.add(ai);
        inputPanel.add(message);
        inputPanel.add(enterButton);

        // Добавление элементов на окно
        add(BorderLayout.CENTER, scrollBar); // Область диалога в центр
        add(BorderLayout.SOUTH, inputPanel); // Панель управления в низ окна

        history = new ArrayList<>(); //список для хранения истории сообщений
        /** Вызов метода загрузки истории.
         *  Метод читает ранее сохраненные диалоги из файла и добавляет
         *  их в диалоге.
         */
        loadHistory();

        setVisible(true); // Отображение окна

        sbot = new SimpleBot(); // Создание экземпляра чат-бота

        // Добавление shutdown hook для записи истории в файл при завершении программы
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveHistory));
    }

    /**
     * Обработчик событий для кнопки Enter и ввода текста в поле сообщения.
     * Отправляет сообщение в чат-бот и отображает ответ.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        String userMessage = message.getText().trim(); // Получаем введенное сообщение, убираем лишние пробелы

        // Проверяем, не является ли сообщение пользователя пустым.
        if (userMessage.length() > 0) {
            String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date()); // Получаем текущую временную метку в формате "часы:минуты:секунды".
            // Формирует строку, которая представляет сообщение пользователя.
            // Включает временную метку, слово "Вы" и текст сообщения.
            String userEntry = String.format("[%s] Вы: %s", timestamp, userMessage);
            dialogue.append(userEntry + "\n");
            history.add(userEntry); // Сохранение сообщения пользователя в список "history", чтобы позже можно было его сохранить в файл.

            // Передаем сообщение пользователя боту и получаем ответ бота.
            String botResponse = sbot.sayInReturn(userMessage, ai.isSelected()); // Параметр `ai.isSelected()` определяет, активен ли режим AI.
            String botEntry = String.format("[%s] Стив ai: %s", timestamp, botResponse);
            dialogue.append(botEntry + "\n");
            history.add(botEntry);
        }

        // Очищаем поле ввода и устанавливаем фокус на него
        message.setText("");
        message.requestFocusInWindow();
    }

    // Метод сохранения истории сообщений в файл.
    private void saveHistory() {
        // Используем конструкцию try-with-resources, чтобы автоматически закрывать поток записи в файл.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE))) {
            // Проходим по каждой строке из списка "history".
            for (String line : history) {
                writer.write(line); // Записываем каждую строку в файл.
                writer.newLine(); // Переходим на новую строчку в файле.
            }
        } catch (IOException e) {
            // Если происходит ошибка ввода-вывода (например, файл недоступен), выводим информацию об ошибке.
            e.printStackTrace();
        }
    }

    // Метод загрузки истории сообщений из файла.
    private void loadHistory() {
        // Используем конструкцию try-with-resources, чтобы автоматически закрывать поток чтения из файла.
        try (BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE))) {
            String line; // Переменная для хранения строк, считанных из файла.
            while ((line = reader.readLine()) != null) { // Читаем файл построчно, пока не достигнем конца.
                dialogue.append(line + "\n"); // Добавляем каждую строку в текст диалога
                history.add(line); // Сохраняем каждую строку в список "history"
            }
        } catch (IOException e) {
            // Если возникает ошибка ввода-вывода (например, файл отсутствует), выводим информацию об ошибке.
            e.printStackTrace();
        }
    }
}