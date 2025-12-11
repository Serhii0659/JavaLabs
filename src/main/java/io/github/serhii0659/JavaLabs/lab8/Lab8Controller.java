package io.github.serhii0659.JavaLabs.lab8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Контролер для Лабораторної роботи №8.
 * Демонструє агрегацію (Menu містить Dish[]), сортування та пошук у масиві.
 *
 * @author Serhii
 * @version 1.0
 */
public class Lab8Controller {

    @FXML private TextArea outputArea;
    @FXML private TextField filterPriceField;
    @FXML private TextField editIdField;
    @FXML private TextField editPriceField;

    private double xOffset = 0;
    private double yOffset = 0;

    // Об'єкт-агрегатор (зберігає стан між натисканнями кнопок)
    private Menu menu;

    /**
     * Ініціалізація контролера.
     */
    @FXML
    public void initialize() {
        // Ініціалізуємо меню при старті вікна
        menu = new Menu();
        onShowMenu(); // Одразу показуємо меню
    }

    // === Button Handlers ===

    /**
     * Відображає все меню.
     */
    @FXML
    protected void onShowMenu() {
        captureConsole(() -> menu.printMenu());
    }

    /**
     * Сортує за зростанням ціни.
     */
    @FXML
    protected void onSortAsc() {
        captureConsole(() -> {
            menu.sortAscending();
            menu.printMenu();
        });
    }

    /**
     * Сортує за спаданням ціни.
     */
    @FXML
    protected void onSortDesc() {
        captureConsole(() -> {
            menu.sortDescending();
            menu.printMenu();
        });
    }

    /**
     * Фільтрує страви за ціною.
     */
    @FXML
    protected void onFilter() {
        captureConsole(() -> {
            try {
                String text = filterPriceField.getText().replace(",", ".");
                if (text.isEmpty()) {
                    System.out.println("Помилка: Введіть ціну для пошуку.");
                    return;
                }
                double limit = Double.parseDouble(text);
                menu.showDishesCheaperThan(limit);
            } catch (NumberFormatException e) {
                System.out.println("Помилка: Некоректний формат ціни.");
            }
        });
    }

    /**
     * Змінює ціну страви за ID.
     */
    @FXML
    protected void onModify() {
        captureConsole(() -> {
            try {
                String idText = editIdField.getText();
                String priceText = editPriceField.getText().replace(",", ".");

                if (idText.isEmpty() || priceText.isEmpty()) {
                    System.out.println("Помилка: Заповніть поля ID та Нова ціна.");
                    return;
                }

                int index = Integer.parseInt(idText);
                double newPrice = Double.parseDouble(priceText);

                menu.modifyDishPrice(index, newPrice);
                menu.printMenu(); // Показуємо оновлене меню

            } catch (NumberFormatException e) {
                System.out.println("Помилка: Некоректний формат чисел.");
            }
        });
    }

    /**
     * Утиліта для перехоплення System.out у TextArea.
     * @param action дія, яку треба виконати (лямбда)
     */
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

        // Додаємо текст замість перезапису, щоб бачити історію, або setText для очищення
        // Тут використовуємо setText, щоб було як в консолі (очищення екрану)
        outputArea.setText(baos.toString());
    }

    // === Window Logic ===
    @FXML private void closeWindow(ActionEvent event) { ((Stage)((Node)event.getSource()).getScene().getWindow()).close(); }
    @FXML private void handleWindowPress(MouseEvent event) { Stage s = (Stage)((Node)event.getSource()).getScene().getWindow(); xOffset = s.getX() - event.getScreenX(); yOffset = s.getY() - event.getScreenY(); }
    @FXML private void handleWindowDrag(MouseEvent event) { Stage s = (Stage)((Node)event.getSource()).getScene().getWindow(); s.setX(event.getScreenX() + xOffset); s.setY(event.getScreenY() + yOffset); }
}

// --- КЛАСИ З LAB 8 ---

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

class Pizza extends Dish {
    public Pizza(String name, double price) { super(name, price); }
}

class Soup extends Dish {
    public Soup(String name, double price) { super(name, price); }
}

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

    public void modifyDishPrice(int index, double newPrice) {
        if (index >= 0 && index < dishes.length) {
            double oldPrice = dishes[index].getPrice();
            dishes[index].setPrice(newPrice);
            System.out.printf("Ціну для '%s' змінено: %.2f -> %.2f грн%n",
                    dishes[index].getName(), oldPrice, newPrice);
        } else {
            System.out.println("Помилка: Невірний індекс страви.");
        }
    }

    public void showDishesCheaperThan(double limit) {
        System.out.println("\n--- Бюджетні страви (до " + limit + " грн) ---");
        boolean found = false;
        for (Dish dish : dishes) {
            if (dish.getPrice() <= limit) {
                dish.printInfo();
                found = true;
            }
        }
        if (!found) {
            System.out.println("Страви в цій ціновій категорії відсутні.");
        }
    }

    public void sortAscending() {
        Arrays.sort(dishes, Comparator.comparingDouble(Dish::getPrice));
        System.out.println("\n[Сортування виконано: від дешевих до дорогих]");
    }

    public void sortDescending() {
        Arrays.sort(dishes, (d1, d2) -> Double.compare(d2.getPrice(), d1.getPrice()));
        System.out.println("\n[Сортування виконано: від дорогих до дешевих]");
    }
}