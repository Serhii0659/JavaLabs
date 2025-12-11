package io.github.serhii0659.JavaLabs.lab1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Контролер для Лабораторної роботи №1.
 * Виконує обчислення за математичною формулою.
 *
 * @author Serhii
 * @version 1.0
 */
public class Lab1Controller {

    @FXML
    private TextField inputX;

    @FXML
    private TextArea outputArea;

    // Змінні для перетягування вікна
    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Основний метод обчислення.
     * Зчитує X, виконує розрахунки та виводить результат у текстове поле.
     */
    @FXML
    protected void onCalculate() {
        try {
            // Зчитування та нормалізація вводу (заміна коми на крапку)
            String rawInput = inputX.getText().replace(",", ".");
            double x = Double.parseDouble(rawInput);
            final double y = 1.2;

            // Логіка обчислення за формулою
            final double cosy = Math.cos(y);
            final double sinx = Math.sin(x);

            final double insideSqrt = 1.0 + cosy * cosy;
            final double sqrtVal = Math.sqrt(insideSqrt);

            final double numerator = 2.33 * Math.log(sqrtVal);
            final double denominator = Math.exp(y) + sinx * sinx;

            final double I = numerator / denominator;

            // Форматований вивід результату
            outputArea.setText(String.format("Вхідні дані:\n x = %.6f\n y = %.6f\n\nРезультат:\n I = %.6f", x, y, I));

        } catch (NumberFormatException e) {
            outputArea.setText("Помилка: введіть коректне число (наприклад, 0.5).");
        } catch (Exception e) {
            outputArea.setText("Критична помилка: " + e.getMessage());
        }
    }

    /**
     * Закриває поточне вікно лабораторної роботи.
     *
     * @param event подія натискання кнопки
     */
    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Фіксує координати курсора для перетягування вікна.
     *
     * @param event подія миші
     */
    @FXML
    private void handleWindowPress(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    /**
     * Переміщує вікно відповідно до руху миші.
     *
     * @param event подія миші
     */
    @FXML
    private void handleWindowDrag(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }
}