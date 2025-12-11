package io.github.serhii0659.JavaLabs.lab10;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Контролер для Лабораторної роботи №10.
 * Демонструє читання текстових файлів та серіалізацію об'єктів у бінарні файли.
 *
 * @author Serhii
 * @version 1.0
 */
public class Lab10Controller {

    @FXML private TextArea outputArea;
    private double xOffset = 0; private double yOffset = 0;

    private Menu menu;
    private static final String TEXT_FILE = "menu_input.txt";
    private static final String BINARY_FILE = "menu_sorted.dat";

    /**
     * Ініціалізація контролера. Створює новий об'єкт меню.
     */
    @FXML
    public void initialize() {
        menu = new Menu();
    }

    // === Button Handlers ===

    /**
     * Створює тестовий текстовий файл з даними.
     */
    @FXML protected void onCreateDummy() {
        captureConsole(() -> {
            String content = "Pizza;Маргарита;120.00\n" +
                    "Soup;Борщ;85.50\n" +
                    "Pizza;Пепероні;150.00\n" +
                    "Soup;Грибний крем;90.00\n" +
                    "Pizza;Гавайська;160.00\n";
            try {
                Files.write(Paths.get(TEXT_FILE), content.getBytes(StandardCharsets.UTF_8));
                System.out.println("Файл '" + TEXT_FILE + "' успішно створено!");
                System.out.println("Зміст:\n" + content);
            } catch (IOException e) {
                System.out.println("Помилка створення файлу: " + e.getMessage());
            }
        });
    }

    /**
     * Зчитує дані з текстового файлу.
     */
    @FXML protected void onReadText() {
        captureConsole(() -> {
            menu.importFromTextFile(TEXT_FILE);
            menu.printMenu("Зчитано з текстового файлу");
        });
    }

    /**
     * Сортує дані в меню за назвою.
     */
    @FXML protected void onSort() {
        captureConsole(() -> {
            menu.sortByNameAscending();
            menu.printMenu("Відсортовано в пам'яті");
        });
    }

    /**
     * Зберігає поточний стан меню у бінарний файл (серіалізація).
     */
    @FXML protected void onSaveBinary() {
        captureConsole(() -> menu.exportToBinaryFile(BINARY_FILE));
    }

    /**
     * Завантажує дані з бінарного файлу (десеріалізація).
     */
    @FXML protected void onLoadBinary() {
        captureConsole(() -> menu.importFromBinaryFileAndPrint(BINARY_FILE));
    }

    // === Utility ===
    private void captureConsole(Runnable action) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(baos, true, StandardCharsets.UTF_8));
            action.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.setOut(originalOut);
        }
        outputArea.setText(baos.toString(StandardCharsets.UTF_8));
    }

    // === Window Logic ===
    /** * Закриває вікно.
     * @param event подія
     */
    @FXML private void closeWindow(ActionEvent event) { ((Stage)((Node)event.getSource()).getScene().getWindow()).close(); }
    /** * Фіксує позицію.
     * @param event подія
     */
    @FXML private void handleWindowPress(MouseEvent event) { Stage s = (Stage)((Node)event.getSource()).getScene().getWindow(); xOffset = s.getX() - event.getScreenX(); yOffset = s.getY() - event.getScreenY(); }
    /** * Перетягує вікно.
     * @param event подія
     */
    @FXML private void handleWindowDrag(MouseEvent event) { Stage s = (Stage)((Node)event.getSource()).getScene().getWindow(); s.setX(event.getScreenX() + xOffset); s.setY(event.getScreenY() + yOffset); }
}

// --- CLASSES FOR LAB 10 ---

abstract class Dish implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String name;
    protected double price;

    public Dish(String name, double price) {
        this.name = name;
        this.price = price;
    }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public void printInfo() { System.out.printf("Страва: %-15s | Ціна: %8.2f грн%n", name, price); }
}

class Pizza extends Dish { public Pizza(String name, double price) { super(name, price); } }
class Soup extends Dish { public Soup(String name, double price) { super(name, price); } }

class Menu {
    private Dish[] dishes;

    public Menu() { dishes = new Dish[0]; }

    public void importFromTextFile(String filename) {
        List<Dish> loaded = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("Файл не знайдено!");
            return;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String type = parts[0].trim();
                    String name = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim().replace(',', '.'));
                    if (type.equalsIgnoreCase("Pizza")) loaded.add(new Pizza(name, price));
                    else if (type.equalsIgnoreCase("Soup")) loaded.add(new Soup(name, price));
                }
            }
            dishes = loaded.toArray(new Dish[0]);
            System.out.println("Дані зчитано успішно (" + dishes.length + " записів).");
        } catch (Exception e) {
            System.out.println("Помилка читання: " + e.getMessage());
        }
    }

    public void exportToBinaryFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(dishes);
            System.out.println("Об'єкти успішно записано у файл: " + filename);
        } catch (IOException e) {
            System.out.println("Помилка запису: " + e.getMessage());
        }
    }

    public void importFromBinaryFileAndPrint(String filename) {
        System.out.println("\n=== ЗЧИТУВАННЯ З БІНАРНОГО ФАЙЛУ ===");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Dish[] loadedDishes = (Dish[]) ois.readObject();
            for (Dish dish : loadedDishes) {
                System.out.print("[Bin] ");
                dish.printInfo();
            }
        } catch (Exception e) {
            System.out.println("Помилка читання бінарного файлу: " + e.getMessage());
        }
    }

    public void sortByNameAscending() {
        if (dishes == null) return;
        Arrays.sort(dishes, Comparator.comparing(Dish::getName));
        System.out.println("Сортування виконано.");
    }

    public void printMenu(String header) {
        System.out.println("\n--- " + header + " ---");
        if (dishes == null || dishes.length == 0) System.out.println("Меню пусте.");
        else for (Dish d : dishes) d.printInfo();
    }
}