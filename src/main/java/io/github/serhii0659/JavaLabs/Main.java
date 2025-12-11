package io.github.serhii0659.JavaLabs;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Головний клас додатка JavaLabs.
 * Відповідає за завантаження JavaFX середовища та відображення головного меню.
 *
 * @author Serhii
 * @version 1.0
 */
public class Main extends Application {

    /**
     * Конструктор за замовчуванням.
     */
    public Main() {
    }

    /**
     * Точка входу в JavaFX додаток.
     * Налаштовує сцену, застосовує CSS стилі та конфігурує прозорість вікна.
     *
     * @param stage Головна сцена додатка
     * @throws IOException якщо FXML файл не знайдено
     */
    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-menu-view.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.setTitle("JavaLabs Project");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);

        stage.setMinWidth(800);
        stage.setMinHeight(500);

        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Точка входу для JVM.
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        launch();
    }
}