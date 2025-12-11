package io.github.serhii0659.JavaLabs.lab4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Контролер для Лабораторної роботи №4.
 * Демонструє роботу циклічних алгоритмів (for, do-while) для табулювання функції.
 *
 * @author Serhii
 * @version 1.0
 */
public class Lab4Controller {

    @FXML
    private TextField inputY;

    @FXML
    private TextArea outputArea;

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Основний метод розрахунку.
     * Обчислює межі діапазону та крок, після чого запускає цикли.
     */
    @FXML
    protected void onCalculate() {
        outputArea.clear();

        try {
            // Зчитування Y
            String rawInput = inputY.getText().replace(",", ".");
            double y = Double.parseDouble(rawInput);

            // Константи з завдання
            final int N = 11;
            final double start = -10 - 2.5 * N;
            final double end = 5 + 1.2 * N;
            final double step = 0.5 + N / 20.0;

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Параметри: N=%d, Y=%.4f%n", N, y));
            sb.append(String.format("Діапазон x: [%.2f; %.2f], Крок: %.2f%n%n", start, end, step));

            // === 1. Цикл FOR ===
            sb.append("=== Результати (цикл FOR) ===\n");
            sb.append(String.format("%-10s | %-20s%n", "X", "Результат I"));
            sb.append("--------------------------------\n");

            for (double x = start; x <= end; x += step) {
                double result = computeI(x, y);
                appendResultLine(sb, x, result);
            }

            sb.append("\n");

            // === 2. Цикл DO-WHILE ===
            sb.append("=== Результати (цикл DO-WHILE) ===\n");
            sb.append(String.format("%-10s | %-20s%n", "X", "Результат I"));
            sb.append("--------------------------------\n");

            double x = start;
            do {
                double result = computeI(x, y);
                appendResultLine(sb, x, result);
                x += step;
            } while (x <= end);

            outputArea.setText(sb.toString());

        } catch (NumberFormatException e) {
            outputArea.setText("Помилка: Введіть коректне число Y.");
        } catch (Exception e) {
            outputArea.setText("Критична помилка: " + e.getMessage());
        }
    }

    /**
     * Допоміжний метод для додавання рядка в таблицю.
     */
    private void appendResultLine(StringBuilder sb, double x, double result) {
        if (Double.isNaN(result)) {
            sb.append(String.format("%-10.4f | %-20s%n", x, "поза ОДЗ"));
        } else {
            sb.append(String.format("%-10.4f | %-20.6f%n", x, result));
        }
    }

    /**
     * Метод обчислення функції I(x, y).
     * Повертає Double.NaN, якщо значення не входить в ОДЗ.
     *
     * @param x аргумент x
     * @param y аргумент y
     * @return значення функції або NaN
     */
    private double computeI(double x, double y) {
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

    // === Стандартна логіка вікна ===

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleWindowPress(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    @FXML
    private void handleWindowDrag(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }
}