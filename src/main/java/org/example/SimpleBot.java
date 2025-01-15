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
        // math
        put("^[0-9\\+\\-\\*/\\.\\(\\)\\s]+$", "math");
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
        put("math", "Результат вычисления: ");
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
                    // Обработка математических выражений
                    if ("math".equals(entry.getValue())) {
                        try {
                            double result = evaluateExpression(message);
                            return ANSWERS_BY_PATTERNS.get("math") + result;
                        } catch (Exception e) {
                            return "Ошибка при вычислении выражения.";
                        }
                    }
                    return ANSWERS_BY_PATTERNS.getOrDefault(entry.getValue(),
                            "Не понимаю вашей команды, введите другую команду.");
                    }
                }
            // Если ни один шаблон не подошел
            return "Не понимаю вашей команды, введите другую команду.";
        }
    }

    // Метод для вычисления математического выражения

    /** Метод принимает строковое математическое выражение и вычисляет его,
     * обрабатывая операторы, числа и скобки с помощью двух стеков.
     * Она учитывает приоритеты операторов и выполняет операции в правильном порядке.
     */
    public double evaluateExpression(String expression) throws Exception {
        // Удаляем все пробелы из выражения
        expression = expression.replaceAll("\\s+", "");

        // Создаем два стека: один для чисел, другой для операторов
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        int i = 0;
        // Проходимся по всему выражению
        while (i < expression.length()) {
            char c = expression.charAt(i);

            // Если символ является цифрой или точкой (для десятичных чисел)
            if (Character.isDigit(c) || c == '.') {
                StringBuilder number = new StringBuilder();
                // Извлекаем полное число (включая дробную часть)
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    number.append(expression.charAt(i));
                    i++;
                }
                // Преобразуем строку в число и помещаем в стек
                numbers.push(Double.parseDouble(number.toString()));
            }
            // Если символ — открывающаяся скобка
            else if (c == '(') {
                operators.push(c); // Добавляем её в стек операторов
                i++;
            }
            // Если символ — закрывающаяся скобка
            else if (c == ')') {
                // Выполняем вычисление между скобками
                while (!operators.isEmpty() && operators.peek() != '(') {
                    calculate(numbers, operators);
                }
                operators.pop(); // Убираем '(' из стека операторов
                i++;
            }
            // Если символ — оператор (+, -, *, /)
            else if (isOperator(c)) {
                // Выполняем вычисления с учетом приоритета оператора
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    calculate(numbers, operators);
                }
                operators.push(c); // Добавляем текущий оператор в стек
                i++;
            } else {
                i++; // Продолжаем, если не символ, цифра или оператор
            }
        }

        // Завершаем все оставшиеся вычисления
        while (!operators.isEmpty()) {
            calculate(numbers, operators);
        }

        // Проверяем, что остался только один результат в стеке чисел
        if (numbers.size() != 1) {
            throw new Exception("Ошибка в выражении.");
        }
        return numbers.pop(); // Возвращаем результат
    }

    // Проверка, является ли символ оператором
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    // Определяет приоритет оператора
    private int precedence(char operator) {
        if (operator == '+' || operator == '-') {
            return 1; // Низкий приоритет
        }
        if (operator == '*' || operator == '/') {
            return 2; // Высокий приоритет
        }
        return -1; // Неизвестный оператор
    }

    /** Метод выполняет вычисление над двумя операндами (числами) с использованием оператора из стека операторов.
     * Он предназначен для вычисления значений простых математических операций, таких как сложение, вычитание, умножение и деление.
     */
    private void calculate(Stack<Double> numbers, Stack<Character> operators) throws Exception {
        // Проверка на наличие хотя бы двух чисел и оператора в стеке.
        if (numbers.size() < 2 || operators.isEmpty()) {
            throw new Exception("Ошибка в выражении.");
        }
        // Извлекаем два числа (операнда) из стека чисел.
        double b = numbers.pop();
        double a = numbers.pop();

        // Извлекаем оператор из стека операторов.
        char operator = operators.pop();

        // Переменная для хранения результата вычисления.
        double result;

        // В зависимости от оператора выполняем соответствующую операцию.
        switch (operator) {
            case '+':
                result = a + b;
                break;
            case '-':
                result = a - b;
                break;
            case '*':
                result = a * b;
                break;
            case '/':
                // Проверка на деление на ноль.
                if (b == 0) throw new Exception("Деление на ноль.");
                result = a / b;
                break;
            default:
                throw new Exception("Неизвестный оператор.");
        }
        // После вычисления, результат помещаем обратно в стек чисел.
        numbers.push(result);
    }
}

