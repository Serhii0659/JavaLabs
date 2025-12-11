package io.github.serhii0659.JavaLabs.lab3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Контролер для Лабораторної роботи №3.
 * Демонструє обробку виключних ситуацій (try-catch-finally) та генерацію власних винятків.
 *
 * @author Serhii
 * @version 1.0
 */
public class Lab3Controller {

    @FXML
    private TextField inputX;

    @FXML
    private TextArea outputArea;

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Основний метод обчислення з блоком try-catch.
     */
    @FXML
    protected void onCalculate() {
        outputArea.clear(); // Очищуємо поле перед новим запуском
        outputArea.appendText("--- Початок обчислення ---\n");

        try {
            // Спроба зчитати число
            String rawInput = inputX.getText().replace(",", ".");
            // Якщо тут будуть літери, вилетить NumberFormatException
            double x = Double.parseDouble(rawInput);

            final double y = 1.2;

            // Логіка обчислення
            final double cosy = Math.cos(y);
            final double sinx = Math.sin(x);

            final double insideSqrt = 1.0 + cosy * cosy;

            // Генерація власного винятку для від'ємного кореня
            if (insideSqrt < 0) {
                throw new ArithmeticException("Значення під коренем від’ємне. Перевірте y.");
            }

            final double sqrtVal = Math.sqrt(insideSqrt);

            // Генерація власного винятку для логарифма
            if (sqrtVal <= 0) {
                throw new ArithmeticException("Значення під логарифмом не додатнє. Перевірте y.");
            }

            final double denominator = Math.exp(y) + sinx * sinx;

            // Генерація власного винятку для ділення на нуль
            if (Math.abs(denominator) < 1e-15) {
                throw new ArithmeticException("Знаменник дуже близький до нуля.");
            }

            final double numerator = 2.33 * Math.log(sqrtVal);
            final double I = numerator / denominator;

            // Успішний результат
            outputArea.appendText(String.format("УСПІХ: x = %.6f, y = %.6f -> I = %.6f%n", x, y, I));

        } catch (NumberFormatException e) {
            // Обробка помилки формату вводу
            outputArea.appendText("ПОМИЛКА: Введено некоректне число! (" + e.getMessage() + ")\n");

        } catch (ArithmeticException e) {
            // Обробка математичних помилок (наші throw new ArithmeticException)
            outputArea.appendText("МАТЕМАТИЧНА ПОМИЛКА: " + e.getMessage() + "\n");

        } catch (Exception e) {
            // Обробка будь-яких інших непередбачених помилок
            outputArea.appendText("НЕПЕРЕДБАЧЕНА ПОМИЛКА: " + e.getMessage() + "\n");

        } finally {
            // Цей блок виконується ЗАВЖДИ
            outputArea.appendText("--- Блок finally: Спроба розрахунку завершена ---\n");
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