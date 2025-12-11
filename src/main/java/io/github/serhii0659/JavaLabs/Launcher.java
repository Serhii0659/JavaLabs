package io.github.serhii0659.JavaLabs;

/**
 * Клас-обгортка для запуску додатка.
 * Використовується для коректного запуску JavaFX з Fat JAR.
 */
public class Launcher {

    /**
     * Конструктор за замовчуванням.
     */
    public Launcher() {
    }

    /**
     * Точка входу в програму через Launcher.
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        Main.main(args);
    }
}