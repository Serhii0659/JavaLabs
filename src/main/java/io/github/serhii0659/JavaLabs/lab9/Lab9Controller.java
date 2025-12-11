package io.github.serhii0659.JavaLabs.lab9;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Контролер для Лабораторної роботи №9.
 * Розширює функціонал Lab 8, додаючи складне сортування (Comparator) та пошук (RegEx).
 *
 * @author Serhii
 * @version 1.0
 */
public class Lab9Controller {

    @FXML private TextArea outputArea;

    private double xOffset = 0;
    private double yOffset = 0;
    private Menu menu;

    /**
     * Ініціалізація.
     */
    @FXML
    public void initialize() {
        menu = new Menu();
        onShowMenu();
    }

    // === Button Handlers ===

    /** Показує меню. */
    @FXML protected void onShowMenu() {
        captureConsole(() -> menu.printMenu());
    }

    /** Сортування за ціною (зростання). */
    @FXML protected void onSortPriceAsc() {
        captureConsole(() -> {
            menu.sortByPriceAscending();
            menu.printMenu();
        });
    }

    /** Сортування за назвою (А-Я). */
    @FXML protected void onSortNameAsc() {
        captureConsole(() -> {
            menu.sortByNameAscending();
            menu.printMenu();
        });
    }

    /** Сортування за назвою (Я-А). */
    @FXML protected void onSortNameDesc() {
        captureConsole(() -> {
            menu.sortByNameDescending();
            menu.printMenu();
        });
    }

    /** Пошук за регулярним виразом. */
    @FXML protected void onRegexSearch() {
        captureConsole(() -> menu.showDishesNameEndsWithA());
    }

    /** Пошук мінімальної та максимальної ціни. */
    @FXML protected void onMinMax() {
        captureConsole(() -> menu.showMinMaxPriceInfo());
    }

    private void captureConsole(Runnable action) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(baos);
        try {
            System.setOut(newOut);
            action.run();
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

// --- КЛАСИ LAB 9 ---

abstract class Dish {
    protected String name;
    protected double price;

    public Dish(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public void printInfo() {
        System.out.printf("Страва: %-15s | Ціна: %8.2f грн%n", name, price);
    }
}

class Pizza extends Dish { public Pizza(String name, double price) { super(name, price); } }
class Soup extends Dish { public Soup(String name, double price) { super(name, price); } }

class Menu {
    private Dish[] dishes;

    public Menu() {
        dishes = new Dish[10];
        dishes[0] = new Pizza("Маргарита", 120.00);
        dishes[1] = new Soup("Борщ", 85.50);
        dishes[2] = new Pizza("Пепероні", 150.00);
        dishes[3] = new Soup("Грибний крем", 90.00);
        dishes[4] = new Pizza("Гавайська", 160.00);
        dishes[5] = new Soup("Харчо", 100.00);
        dishes[6] = new Pizza("4 Сири", 180.00);
        dishes[7] = new Soup("Солянка", 110.00);
        dishes[8] = new Pizza("BBQ", 195.00);
        dishes[9] = new Soup("Курячий", 75.00);
    }

    public void printMenu() {
        System.out.println("=== ПОТОЧНЕ МЕНЮ ===");
        for (int i = 0; i < dishes.length; i++) {
            System.out.print("ID [" + i + "] ");
            dishes[i].printInfo();
        }
    }

    // --- СОРТУВАННЯ ---

    public void sortByPriceAscending() {
        Arrays.sort(dishes, Comparator.comparingDouble(Dish::getPrice));
        System.out.println("\n[Сортування: Ціна (Зростання)]");
    }

    public void sortByNameAscending() {
        Arrays.sort(dishes, Comparator.comparing(Dish::getName));
        System.out.println("\n[Сортування: Назва (А-Я)]");
    }

    public void sortByNameDescending() {
        Arrays.sort(dishes, Comparator.comparing(Dish::getName).reversed());
        System.out.println("\n[Сортування: Назва (Я-А)]");
    }

    // --- REGEX & STATS ---

    public void showDishesNameEndsWithA() {
        String regex = ".*ка$"; // Наприклад: Солянка, Гавайська
        System.out.println("\n--- Страви, що закінчуються на 'ка' (RegEx) ---");
        boolean found = false;
        for (Dish dish : dishes) {
            if (dish.getName().matches(regex)) {
                dish.printInfo();
                found = true;
            }
        }
        if (!found) System.out.println("Нічого не знайдено.");
    }

    public void showMinMaxPriceInfo() {
        if (dishes == null || dishes.length == 0) return;

        Dish minDish = dishes[0];
        Dish maxDish = dishes[0];

        for (Dish dish : dishes) {
            if (dish.getPrice() < minDish.getPrice()) minDish = dish;
            if (dish.getPrice() > maxDish.getPrice()) maxDish = dish;
        }

        System.out.println("\n--- Статистика цін ---");
        System.out.printf("Найменша: %8.2f грн (%s)%n", minDish.getPrice(), minDish.getName());
        System.out.printf("Найбільша: %8.2f грн (%s)%n", maxDish.getPrice(), maxDish.getName());
    }
}