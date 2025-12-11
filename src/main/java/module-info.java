/**
 * Головний модуль курсового проекту JavaLabs.
 * Містить конфігурацію залежностей, відкриті пакети для JavaFX та експортовані модулі.
 */
module io.github.serhii0659.JavaLabs {
    // Основні модулі JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // База даних (для Lab 11)
    requires java.sql;
    requires org.postgresql.jdbc;

    // UI бібліотеки
    requires atlantafx.base;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    // Відкриваємо головний пакет (для MainMenuController)
    opens io.github.serhii0659.JavaLabs to javafx.fxml;

    // Відкриваємо пакет Lab 1 (для Lab1Controller)
    opens io.github.serhii0659.JavaLabs.lab1 to javafx.fxml;
    opens io.github.serhii0659.JavaLabs.lab2 to javafx.fxml;
    opens io.github.serhii0659.JavaLabs.lab3 to javafx.fxml;
    opens io.github.serhii0659.JavaLabs.lab4 to javafx.fxml;
    opens io.github.serhii0659.JavaLabs.lab5 to javafx.fxml;
    opens io.github.serhii0659.JavaLabs.lab6 to javafx.fxml;
    opens io.github.serhii0659.JavaLabs.lab7 to javafx.fxml;
    opens io.github.serhii0659.JavaLabs.lab8 to javafx.fxml;
    opens io.github.serhii0659.JavaLabs.lab9 to javafx.fxml;
    opens io.github.serhii0659.JavaLabs.lab10 to javafx.fxml;
    opens io.github.serhii0659.JavaLabs.lab11 to javafx.base, javafx.fxml;
    opens io.github.serhii0659.JavaLabs.bonus to javafx.fxml;

    // === ЕКСПОРТ (щоб Java бачила класи) ===
    exports io.github.serhii0659.JavaLabs;
}