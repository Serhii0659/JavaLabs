package io.github.serhii0659.JavaLabs.lab5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Контролер для Лабораторної роботи №5.
 * Демонструє принципи ООП: класи, об'єкти, конструктори, статику та перевантаження.
 *
 * @author Serhii
 * @version 1.0
 */
public class Lab5Controller {

    @FXML
    private TextField inputY;

    @FXML
    private TextArea outputArea;

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Запускає демонстраційний сценарій роботи з класом ExpressionCalculator.
     */
    @FXML
    protected void onRunDemo() {
        outputArea.clear();
        StringBuilder sb = new StringBuilder();

        try {
            // Зчитування Y
            String rawInput = inputY.getText().replace(",", ".");
            double userY = Double.parseDouble(rawInput);

            final int N = 11;
            sb.append("=== Початок Лабораторної роботи №5 ===\n");
            sb.append(String.format("Фіксований N = %d, Введений Y = %.4f%n%n", N, userY));

            final double start = -10 - 2.5 * N;
            final double end = 5 + 1.2 * N;
            final double step = 0.5 + N / 20.0;

            sb.append(String.format("Діапазон x: [%.2f; %.2f], крок: %.2f%n", start, end, step));
            sb.append("--------------------------------------------------\n");

            // 1. Створення об'єктів (демонстрація конструкторів)
            ExpressionCalculator defaultCalc = new ExpressionCalculator();
            ExpressionCalculator paramCalc = new ExpressionCalculator(0.0, userY);

            sb.append("1. Створено об'єкти:\n");
            sb.append(String.format("   - defaultCalc (конструктор без параметрів): x=%.2f, y=%.2f%n",
                    defaultCalc.getX(), defaultCalc.getY()));
            sb.append(String.format("   - paramCalc (параметризований): x=%.2f, y=%.2f%n",
                    paramCalc.getX(), paramCalc.getY()));
            sb.append(String.format("   >> Кількість створених екземплярів (static): %d%n%n",
                    ExpressionCalculator.getInstances()));

            // 2. Обчислення через методи екземпляра (Instance Methods)
            sb.append("2. Обчислення через методи екземпляра (Instance Methods):\n");
            sb.append("   (Використовується Y, збережений всередині об'єкта paramCalc)\n");

            // Виводимо лише перші 5 значень, щоб не засмічувати лог, або всі якщо їх небагато
            int count = 0;
            for (double x = start; x <= end + 1e-9; x += step) {
                paramCalc.setX(x); // Використання сеттера
                double result = paramCalc.computeInstance();

                if (count < 5 || x > end - step) { // Показати початок і кінець
                    appendResultLine(sb, x, result, paramCalc.getY());
                } else if (count == 5) {
                    sb.append("   ... (пропуск рядків) ...\n");
                }
                count++;
            }
            sb.append("\n");

            // 3. Демонстрація статичного методу
            sb.append("3. Демонстрація статичного методу (Static Method):\n");
            sb.append("   (Не залежить від полів об'єкта, аргументи передаються напряму)\n");
            double staticRes = ExpressionCalculator.computeStatic(start, userY);
            sb.append(String.format("   ExpressionCalculator.computeStatic(%.2f, %.2f) -> %s%n%n",
                    start, userY, formatResult(staticRes)));

            // 4. Демонстрація перевантаження (Overloading)
            sb.append("4. Демонстрація перевантаження методів (Overloading):\n");
            double testX = -5.0;
            sb.append(String.format("   a) computeInstance() [бере x,y з об'єкта] -> %s%n",
                    formatResult(defaultCalc.computeInstance())));
            sb.append(String.format("   b) computeInstance(%.1f) [передаємо x, беремо y з об'єкта] -> %s%n",
                    testX, formatResult(defaultCalc.computeInstance(testX))));
            sb.append(String.format("   c) computeInstance(%.1f, %.1f) [передаємо і x, і y] -> %s%n",
                    testX, userY, formatResult(defaultCalc.computeInstance(testX, userY))));

            sb.append("\n=== Кінець роботи ===");
            outputArea.setText(sb.toString());

        } catch (NumberFormatException e) {
            outputArea.setText("Помилка: Введіть коректне число Y.");
        } catch (Exception e) {
            outputArea.setText("Критична помилка: " + e.getMessage());
        }
    }

    private void appendResultLine(StringBuilder sb, double x, double result, double y) {
        sb.append(String.format("   x = %-8.4f -> I = %-10s (object y=%.2f)%n",
                x, formatResult(result), y));
    }

    private String formatResult(double result) {
        return Double.isNaN(result) ? "поза ОДЗ" : String.format("%.6f", result);
    }

    // === Window Logic ===
    @FXML private void closeWindow(ActionEvent event) { ((Stage)((Node)event.getSource()).getScene().getWindow()).close(); }
    @FXML private void handleWindowPress(MouseEvent event) { Stage s = (Stage)((Node)event.getSource()).getScene().getWindow(); xOffset = s.getX() - event.getScreenX(); yOffset = s.getY() - event.getScreenY(); }
    @FXML private void handleWindowDrag(MouseEvent event) { Stage s = (Stage)((Node)event.getSource()).getScene().getWindow(); s.setX(event.getScreenX() + xOffset); s.setY(event.getScreenY() + yOffset); }
}

/**
 * Клас для виконання обчислень виразу.
 * Містить демонстрацію полів, методів, конструкторів та статики.
 */
class ExpressionCalculator {

    // Приватні поля (Інкапсуляція)
    private double x;
    private double y;

    // Статичне поле (спільне для всіх екземплярів)
    private static int instances = 0;

    /**
     * Конструктор за замовчуванням.
     * Ініціалізує об'єкт стандартними значеннями.
     */
    public ExpressionCalculator() {
        this.x = 0.0;
        this.y = 1.2;
        instances++;
    }

    /**
     * Параметризований конструктор.
     * @param x значення аргументу x
     * @param y значення аргументу y
     */
    public ExpressionCalculator(double x, double y) {
        this.x = x;
        this.y = y;
        instances++;
    }

    // Геттери та Сеттери
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    /**
     * Статичний метод для отримання кількості створених об'єктів.
     * @return кількість екземплярів
     */
    public static int getInstances() {
        return instances;
    }

    // --- Перевантаження методів (Overloading) ---

    /**
     * Обчислює вираз, використовуючи внутрішні поля x та y.
     */
    public double computeInstance() {
        return computeStatic(this.x, this.y);
    }

    /**
     * Обчислює вираз, використовуючи переданий x та внутрішній y.
     */
    public double computeInstance(double x) {
        return computeStatic(x, this.y);
    }

    /**
     * Обчислює вираз, використовуючи передані x та y.
     */
    public double computeInstance(double x, double y) {
        return computeStatic(x, y);
    }

    /**
     * Статичний метод обчислення (основна логіка).
     * Не залежить від стану конкретного об'єкта.
     */
    public static double computeStatic(double x, double y) {
        try {
            double cosy = Math.cos(y);
            double sinx = Math.sin(x);

            double insideSqrt = 1.0 + cosy * cosy;
            if (insideSqrt < 0) return Double.NaN;

            double sqrtVal = Math.sqrt(insideSqrt);
            if (sqrtVal <= 0) return Double.NaN;

            double numerator = 2.33 * Math.log(sqrtVal);
            double denominator = Math.exp(y) + sinx * sinx;

            if (Math.abs(denominator) < 1e-15) return Double.NaN;

            return numerator / denominator;
        } catch (Exception e) {
            return Double.NaN;
        }
    }
}