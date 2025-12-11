package io.github.serhii0659.JavaLabs.lab2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Контролер для Лабораторної роботи №2.
 * Реалізує обчислення з перевіркою області допустимих значень (ОДЗ).
 *
 * @author Serhii
 * @version 1.0
 */
public class Lab2Controller {

    @FXML
    private TextField inputX;

    @FXML
    private TextArea outputArea;

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Виконує обчислення з перевірками (розгалуженнями).
     */
    @FXML
    protected void onCalculate() {
        try {
            String rawInput = inputX.getText().replace(",", ".");
            double x = Double.parseDouble(rawInput);
            final double y = 1.2;

            // 1. Попередні обчислення
            final double cosy = Math.cos(y);
            final double sinx = Math.sin(x);
            final double insideSqrt = 1.0 + cosy * cosy;

            // 2. Перевірка підкореневого виразу
            if (insideSqrt < 0) {
                outputArea.setText("Помилка: Значення під коренем від’ємне.\nПеревірте вхідні дані.");
                return;
            }

            final double sqrtVal = Math.sqrt(insideSqrt);

            // 3. Перевірка логарифма (аргумент має бути > 0)
            if (sqrtVal <= 0) {
                outputArea.setText("Помилка: Значення під логарифмом не додатнє (<= 0).");
                return;
            }

            final double denominator = Math.exp(y) + sinx * sinx;

            // 4. Перевірка знаменника (щоб не було ділення на нуль)
            if (Math.abs(denominator) < 1e-15) {
                outputArea.setText("Помилка: Знаменник занадто близький до нуля (ділення на нуль).");
                return;
            }

            // 5. Фінальне обчислення, якщо всі перевірки пройдено
            final double numerator = 2.33 * Math.log(sqrtVal);
            final double I = numerator / denominator;

            outputArea.setText(String.format("Вхідні дані:\n x = %.6f\n y = %.6f\n\nСтатус: Успішно\nРезультат I = %.6f", x, y, I));

        } catch (NumberFormatException e) {
            outputArea.setText("Помилка: Введіть коректне дійсне число.");
        } catch (Exception e) {
            outputArea.setText("Непередбачена помилка: " + e.getMessage());
        }
    }

    // === Стандартна логіка вікна (Закриття/Перетягування) ===

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