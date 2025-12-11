package io.github.serhii0659.JavaLabs.lab6;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Контролер для Лабораторної роботи №6.
 * Демонструє принципи спадкування (Inheritance).
 *
 * @author Serhii
 * @version 1.0
 */
public class Lab6Controller {

    @FXML
    private TextArea outputArea;

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Запускає демонстрацію роботи класів.
     * Перехоплює System.out.println і виводить текст у вікно.
     */
    @FXML
    protected void onRunDemo() {
        // Зберігаємо старий потік виводу (консоль IDE)
        PrintStream originalOut = System.out;

        // Створюємо буфер для перехоплення тексту
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(baos);

        try {
            // Перенаправляємо System.out на наш буфер
            System.setOut(newOut);

            // === ТУТ ТВІЙ ОРИГІНАЛЬНИЙ КОД З LAB 6 ===

            System.out.println("=== Лабораторна робота 6: Спадкування ===\n");

            // 1. Створення інгредієнтів
            Ingredient cheese = new Ingredient("Моцарела", 150);
            Ingredient meat = new Ingredient("Салямі", 80);

            // 2. Робота з базовим класом
            System.out.println("1. Створення базового об'єкта (Dish):");
            Dish soup = new Dish("Борщ", 85.50);
            soup.addIngredient(new Ingredient("Буряк", 200));
            soup.printInfo();

            System.out.println("\n------------------------------------");

            // 3. Робота з нащадком
            System.out.println("2. Створення об'єкта-нащадка (Pizza extends Dish):");
            Pizza myPizza = new Pizza("Пепероні", 250.00, 40);

            myPizza.addIngredient(cheese);
            myPizza.addIngredient(meat);

            // Виклик перевизначеного методу
            myPizza.printInfo();

            // Виклик унікального методу нащадка
            System.out.println("\n3. Виклик унікального методу Pizza:");
            myPizza.slice();

            // === КІНЕЦЬ ОРИГІНАЛЬНОГО КОДУ ===

        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
        } finally {
            // Важливо: повертаємо консоль на місце, щоб не зламати логування IDE
            System.out.flush();
            System.setOut(originalOut);
        }

        // Виводимо перехоплений текст у TextArea
        outputArea.setText(baos.toString());
    }

    // === Window Logic ===
    @FXML private void closeWindow(ActionEvent event) { ((Stage)((Node)event.getSource()).getScene().getWindow()).close(); }
    @FXML private void handleWindowPress(MouseEvent event) { Stage s = (Stage)((Node)event.getSource()).getScene().getWindow(); xOffset = s.getX() - event.getScreenX(); yOffset = s.getY() - event.getScreenY(); }
    @FXML private void handleWindowDrag(MouseEvent event) { Stage s = (Stage)((Node)event.getSource()).getScene().getWindow(); s.setX(event.getScreenX() + xOffset); s.setY(event.getScreenY() + yOffset); }
}

// --- ДОПОМІЖНІ КЛАСИ (З Lab 6) ---
// (public прибрано, щоб вони могли бути в одному файлі)

class Ingredient {
    private final String name;
    private final double weightGrams;

    public Ingredient() {
        this.name = "Невідомий інгредієнт";
        this.weightGrams = 0;
    }

    public Ingredient(String name, double weightGrams) {
        this.name = name;
        this.weightGrams = weightGrams;
    }

    public void printInfo() {
        System.out.println(" - " + name + " (" + weightGrams + "г)");
    }
}

class Dish {
    protected final String name;
    protected final double price;
    protected final List<Ingredient> ingredients;

    public Dish() {
        this.name = "Стандартна страва";
        this.price = 0.0;
        this.ingredients = new ArrayList<>();
    }

    public Dish(String name, double price) {
        this.name = name;
        this.price = price;
        this.ingredients = new ArrayList<>();
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void printInfo() {
        System.out.println("\n--- Інформація про страву ---");
        System.out.println("Назва: " + name);
        System.out.printf("Ціна: %.2f грн%n", price);
        System.out.println("Склад:");
        if (ingredients.isEmpty()) {
            System.out.println(" (Інгредієнти відсутні)");
        } else {
            for (Ingredient ing : ingredients) {
                ing.printInfo();
            }
        }
    }
}

class Pizza extends Dish {
    private final int sizeCm;

    public Pizza() {
        super("Маргарита", 100.0);
        this.sizeCm = 30;
    }

    public Pizza(String name, double price, int sizeCm) {
        super(name, price);
        this.sizeCm = sizeCm;
    }

    // Перевизначений метод (Override)
    @Override
    public void printInfo() {
        super.printInfo();
        System.out.println("Діаметр піци: " + sizeCm + " см");
    }

    // Унікальний метод піци
    public void slice() {
        System.out.println("Піцу '" + name + "' нарізано на 8 шматків!");
    }
}