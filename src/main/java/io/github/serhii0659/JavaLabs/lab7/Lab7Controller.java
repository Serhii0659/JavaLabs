package io.github.serhii0659.JavaLabs.lab7;

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
 * Контролер для Лабораторної роботи №7.
 * Демонструє поліморфізм та використання абстрактних класів.
 *
 * @author Serhii
 * @version 1.0
 */
public class Lab7Controller {

    @FXML
    private TextArea outputArea;

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Запускає демонстрацію поліморфізму.
     */
    @FXML
    protected void onRunDemo() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(baos);

        try {
            System.setOut(newOut);

            // === ЛОГІКА LAB 7 ===
            System.out.println("=== Лабораторна робота 7: Поліморфізм ===\n");

            // Інгредієнти
            Ingredient cheese = new Ingredient("Моцарела", 150);
            Ingredient meat = new Ingredient("Салямі", 80);
            Ingredient beet = new Ingredient("Буряк", 200);

            // 1. Робота з Soup (через посилання базового класу Dish, поліморфізм)
            System.out.println("1. Створення Soup (Dish soup = new Soup...):");
            Dish soup = new Soup("Борщ", 85.50);
            soup.addIngredient(beet);

            soup.printInfo();
            // Тут викликається реалізація prepare() з класу Soup
            soup.prepare();

            System.out.println("\n------------------------------------");

            // 2. Робота з Pizza
            System.out.println("2. Створення Pizza:");
            Pizza myPizza = new Pizza("Пепероні", 250.00, 40);
            myPizza.addIngredient(cheese);
            myPizza.addIngredient(meat);

            myPizza.printInfo();
            // Тут викликається реалізація prepare() з класу Pizza
            myPizza.prepare();
            myPizza.slice();

            // === КІНЕЦЬ ЛОГІКИ ===

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            System.out.flush();
            System.setOut(originalOut);
        }

        outputArea.setText(baos.toString());
    }

    // === Window Logic ===
    @FXML private void closeWindow(ActionEvent event) { ((Stage)((Node)event.getSource()).getScene().getWindow()).close(); }
    @FXML private void handleWindowPress(MouseEvent event) { Stage s = (Stage)((Node)event.getSource()).getScene().getWindow(); xOffset = s.getX() - event.getScreenX(); yOffset = s.getY() - event.getScreenY(); }
    @FXML private void handleWindowDrag(MouseEvent event) { Stage s = (Stage)((Node)event.getSource()).getScene().getWindow(); s.setX(event.getScreenX() + xOffset); s.setY(event.getScreenY() + yOffset); }
}

// --- КЛАСИ ДЛЯ LAB 7 ---

class Ingredient {
    private final String name;
    private final double weightGrams;
    public Ingredient(String name, double weightGrams) { this.name = name; this.weightGrams = weightGrams; }
    public void printInfo() { System.out.println(" - " + name + " (" + weightGrams + "г)"); }
}

// Абстрактний клас
abstract class Dish {
    protected final String name;
    protected final double price;
    protected final List<Ingredient> ingredients;

    public Dish(String name, double price) {
        this.name = name;
        this.price = price;
        this.ingredients = new ArrayList<>();
    }

    public void addIngredient(Ingredient ingredient) { ingredients.add(ingredient); }

    // АБСТРАКТНИЙ МЕТОД (Поліморфізм)
    public abstract void prepare();

    public void printInfo() {
        System.out.println("\n--- Інформація про страву ---");
        System.out.println("Назва: " + name);
        System.out.printf("Ціна: %.2f грн%n", price);
        System.out.println("Склад:");
        if (ingredients.isEmpty()) System.out.println(" (Інгредієнти відсутні)");
        else for (Ingredient ing : ingredients) ing.printInfo();
    }
}

class Pizza extends Dish {
    private final int sizeCm;
    public Pizza(String name, double price, int sizeCm) { super(name, price); this.sizeCm = sizeCm; }

    @Override public void printInfo() { super.printInfo(); System.out.println("Діаметр піци: " + sizeCm + " см"); }

    // Реалізація абстрактного методу
    @Override public void prepare() { System.out.println("ПРОЦЕС: Розкатуємо тісто, викладаємо начинку і печемо при 250°C."); }

    public void slice() { System.out.println("ДІЯ: Піцу '" + name + "' нарізано на 8 шматків!"); }
}

class Soup extends Dish {
    public Soup(String name, double price) { super(name, price); }

    // Реалізація абстрактного методу
    @Override public void prepare() { System.out.println("ПРОЦЕС: Варимо бульйон, додаємо овочі та кип'ятимо 40 хвилин."); }
}