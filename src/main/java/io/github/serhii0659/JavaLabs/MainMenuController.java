package io.github.serhii0659.JavaLabs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

/**
 * Контролер головного меню додатка.
 * Відповідає за навігацію між лабораторними роботами та керування головним вікном.
 *
 * @author Serhii
 * @version 1.0
 */
public class MainMenuController {

    @FXML
    private GridPane labsGrid;

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Конструктор за замовчуванням.
     */
    public MainMenuController() {
    }

    /**
     * Метод ініціалізації контролера. Викликається автоматично після завантаження FXML.
     */
    @FXML
    public void initialize() {
        System.out.println("Головне меню ініціалізовано.");
    }

    // === ЛАБОРАТОРНІ РОБОТИ ===

    /** Відкриває вікно Лабораторної роботи №1. */
    @FXML public void openLab1() { openLabWindow("lab1/lab1-view.fxml", "Lab 1: Math Formulas"); }

    /** Відкриває вікно Лабораторної роботи №2. */
    @FXML public void openLab2() { openLabWindow("lab2/lab2-view.fxml", "Lab 2: Розгалуження"); }

    /** Відкриває вікно Лабораторної роботи №3. */
    @FXML public void openLab3() { openLabWindow("lab3/lab3-view.fxml", "Lab 3: Обробка винятків"); }

    /** Відкриває вікно Лабораторної роботи №4. */
    @FXML public void openLab4() { openLabWindow("lab4/lab4-view.fxml", "Lab 4: Циклічні алгоритми"); }

    /** Відкриває вікно Лабораторної роботи №5. */
    @FXML public void openLab5() { openLabWindow("lab5/lab5-view.fxml", "Lab 5: Класи та об'єкти"); }

    /** Відкриває вікно Лабораторної роботи №6. */
    @FXML public void openLab6() { openLabWindow("lab6/lab6-view.fxml", "Lab 6: Спадкування"); }

    /** Відкриває вікно Лабораторної роботи №7. */
    @FXML public void openLab7() { openLabWindow("lab7/lab7-view.fxml", "Lab 7: Поліморфізм"); }

    /** Відкриває вікно Лабораторної роботи №8. */
    @FXML public void openLab8() { openLabWindow("lab8/lab8-view.fxml", "Lab 8: Агрегація"); }

    /** Відкриває вікно Лабораторної роботи №9. */
    @FXML public void openLab9() { openLabWindow("lab9/lab9-view.fxml", "Lab 9: Сортування та RegEx"); }

    /** Відкриває вікно Лабораторної роботи №10. */
    @FXML public void openLab10() { openLabWindow("lab10/lab10-view.fxml", "Lab 10: Робота з файлами"); }

    /** Відкриває вікно Лабораторної роботи №11. */
    @FXML public void openLab11() { openLabWindow("lab11/lab11-view.fxml", "Lab 11: База даних"); }

    /** Відкриває вікно Бонусу. */
    @FXML public void openBonus() { openLabWindow("bonus/bonus-view.fxml", "Bonus: Графіки"); }

    private void openLabWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            URL cssUrl = getClass().getResource("styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Помилка", "Не вдалося відкрити лабораторну\n" + e.getMessage());
        }
    }

    // === КЕРУВАННЯ ВІКНОМ ===

    /**
     * Закриває додаток.
     * @param event подія натискання кнопки
     */
    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    /**
     * Згортає вікно додатка.
     * @param event подія натискання кнопки
     */
    @FXML
    private void minimizeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * Обробка натискання миші для перетягування.
     * @param event подія миші
     */
    @FXML
    private void handleWindowPress(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    /**
     * Обробка перетягування вікна.
     * @param event подія миші
     */
    @FXML
    private void handleWindowDrag(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        try {
            URL cssUrl = getClass().getResource("styles.css");
            if (cssUrl != null) {
                alert.getDialogPane().getStylesheets().add(cssUrl.toExternalForm());
                alert.getDialogPane().getStyleClass().add("dialog-pane");
            }
        } catch (Exception ignored) {}
        alert.showAndWait();
    }
}