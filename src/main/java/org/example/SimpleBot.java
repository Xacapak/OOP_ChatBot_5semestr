package org.example;

import java.util.*;
import java.util.regex.*;

public class SimpleBot {

    // Шаблоны для анализа сообщений
    final Map<String, String> PATTERNS_FOR_ANALYSIS = new HashMap<String, String>() {{
        // hello
        put("хай", "hello");
        put("привет", "hello");
        put("здорово", "hello");
        put("здравствуй", "hello");
        // who
        put("кто\\s.*ты", "who");
        put("ты\\s.*кто", "who");
        // name
        put("как\\s.*зовут", "name");
        put("как\\s.*имя", "name");
        put("есть\\s.*имя", "name");
        put("какое\\s.*имя", "name");
        // howareyou
        put("как\\s.*дела", "howareyou");
        put("как\\s.*жизнь", "howareyou");
        // whatdoyoudoing
        put("зачем\\s.*тут", "whatdoyoudoing");
        put("зачем\\s.*здесь", "whatdoyoudoing");
        put("что\\s.*делаешь", "whatdoyoudoing");
        put("чем\\s.*занимаешься", "whatdoyoudoing");
        // whatdoyoulike
        put("что\\s.*нравится", "whatdoyoulike");
        put("что\\s.*любишь", "whatdoyoulike");
        // iamfeelling
        put("кажется", "iamfeelling");
        put("чувствую", "iamfeelling");
        put("испытываю", "iamfeelling");
        // yes
        put("^да", "yes");
        put("согласен", "yes");
        // whattime
        put("который\\s.*час", "whattime");
        put("сколько\\s.*время", "whattime");
        // bye
        put("прощай", "bye");
        put("увидимся", "bye");
        put("до\\s.*свидания", "bye");
    }};
    // Ответы по шаблонам
    final Map<String, String> ANSWERS_BY_PATTERNS = new HashMap<String, String>() {{ // добавить список строк
        put("hello", "Здравствуйте, рад Вас видеть.");
        put("who", "Я обычный чат-бот.");
        put("name", "Зовите меня Стив :)");
        put("howareyou", "Спасибо, что интересуетесь. У меня всё хорошо.");
        put("whatdoyoudoing", "Я пробую общаться с людьми.");
        put("whatdoyoulike", "Мне нравиться думать что я не просто программа.");
        put("iamfeelling", "Как давно это началось? Расскажите чуть подробнее.");
        put("yes", "Согласие есть продукт при полном непротивлении сторон.");
        put("bye", "До свидания. Надеюсь, ещё увидимся.");
    }};
    // Переменные для работы с датой и регулярными выражениями
    Pattern pattern; // для регулярного выражения
    Date date; // для указания даты и времени

    public SimpleBot() {
        date = new Date();
    }

    public String sayInReturn(String msg, boolean ai) {

        // Проверка на пустое сообщение
        if (msg == null || msg.trim().isEmpty()) {
            return "Пожалуйста, введите команду.";
        }

        // Если активен режим ИИ, выполняется этот блок
        if (ai) {
            // Код на основе ИИ:
            // Заглушка
            return "Обработка ИИ: Извините, я пока не научился отвечать.";
        } else {
            // Преобразуем сообщение в нижний регистр и разбиваем на слова с удалением ненужных символов
            String message = msg.toLowerCase().replaceAll("[\\s{,|.}?]+", " ").trim();
            // Обработка шаблонов
            for (Map.Entry<String, String> entry : PATTERNS_FOR_ANALYSIS.entrySet()) {
                pattern = Pattern.compile(entry.getKey()); // Компиляция паттерна
                // Проверка на соответствие шаблону
                if (pattern.matcher(message).find()) {
                    // Специальная обработка для запроса времени
                    if ("whattime".equals(entry.getValue())) {
                        return date.toString();
                    }
                    return ANSWERS_BY_PATTERNS.getOrDefault(entry.getValue(),
                            "Не понимаю вашей команды, введите другую команду.");
                    }
                }
            // Если ни один шаблон не подошел
            return "Не понимаю вашей команды, введите другую команду.";
        }
    }
}

