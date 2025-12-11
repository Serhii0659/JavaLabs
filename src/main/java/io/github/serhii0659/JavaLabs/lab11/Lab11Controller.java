package io.github.serhii0659.JavaLabs.lab11;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Optional;

/**
 * Контролер для Лабораторної роботи №11.
 * Реалізує взаємодію з базою даних PostgreSQL через JDBC.
 *
 * @author Serhii
 * @version 1.1
 */
public class Lab11Controller {

    private static final String SERVER_URL = "jdbc:postgresql://localhost:13000/";
    private static final String TARGET_DB = "lab11_menu_db";
    private static final String USER = "postgres";
    private static final String PASS = "3a3U}24Fx5;L";

    @FXML private ComboBox<String> typeCombo;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private Label statusLabel;

    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;

    @FXML private TableView<DishItem> table;
    @FXML private TableColumn<DishItem, Integer> colId;
    @FXML private TableColumn<DishItem, String> colType;
    @FXML private TableColumn<DishItem, String> colName;
    @FXML private TableColumn<DishItem, Double> colPrice;

    private final ObservableList<DishItem> dataList = FXCollections.observableArrayList();
    private double xOffset = 0; private double yOffset = 0;

    /**
     * Ініціалізація контролера. Налаштовує таблицю та підключення до БД.
     */
    @FXML
    public void initialize() {
        // Налаштування колонок
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        typeCombo.getItems().addAll("Pizza", "Soup");
        typeCombo.getSelectionModel().selectFirst();
        table.setItems(dataList);

        // === ГОЛОВНА ФІШКА: СЛУХАЧ ВИБОРУ В ТАБЛИЦІ ===
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Якщо обрали рядок -> заповнюємо поля
                typeCombo.setValue(newSelection.getType());
                nameField.setText(newSelection.getName());
                priceField.setText(String.valueOf(newSelection.getPrice()));

                // Перемикаємо режим кнопок
                btnAdd.setDisable(true);
                btnUpdate.setDisable(false);
                statusLabel.setText("Режим: Редагування ID " + newSelection.getId());
            } else {
                clearFields(); // Якщо зняли виділення
            }
        });

        // Ініціалізація БД
        createDatabaseIfNotExists();
        createTableIfNotExists();
        refreshTable();
    }

    // === ЛОГІКА КНОПОК ===

    @FXML
    private void clearFields() {
        // Очищає поля і скидає вибір таблиці
        table.getSelectionModel().clearSelection();
        nameField.clear();
        priceField.clear();
        typeCombo.getSelectionModel().selectFirst();

        btnAdd.setDisable(false);
        btnUpdate.setDisable(true);
        statusLabel.setText("Режим: Створення нового");
    }

    /**
     * Додає новий запис у базу даних.
     */
    @FXML
    private void addDish() {
        String type = typeCombo.getValue();
        String name = nameField.getText();
        String priceStr = priceField.getText();

        if (name.isEmpty() || priceStr.isEmpty()) {
            showAlert("Помилка", "Заповніть назву та ціну!");
            return;
        }

        String sql = "INSERT INTO menu_items (type, name, price) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, type);
            pstmt.setString(2, name);
            pstmt.setDouble(3, Double.parseDouble(priceStr.replace(",", ".")));
            pstmt.executeUpdate();

            clearFields();
            refreshTable();
        } catch (Exception e) {
            showAlert("Помилка БД", e.getMessage());
        }
    }

    @FXML
    private void updateDish() {
        DishItem selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String sql = "UPDATE menu_items SET type = ?, name = ?, price = ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, typeCombo.getValue());
            pstmt.setString(2, nameField.getText());
            pstmt.setDouble(3, Double.parseDouble(priceField.getText().replace(",", ".")));
            pstmt.setInt(4, selected.getId());
            pstmt.executeUpdate();

            clearFields();
            refreshTable();
        } catch (Exception e) {
            showAlert("Помилка БД", e.getMessage());
        }
    }

    @FXML
    private void deleteDish() {
        DishItem selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Увага", "Вибір", "Оберіть рядок у таблиці для видалення.");
            return;
        }

        // === НОВИЙ КОД ДЛЯ ГАРНОГО ВІКНА ===
        try {
            // 1. Завантажуємо вікно підтвердження
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("confirmation-dialog.fxml"));
            javafx.scene.Parent root = loader.load();

            // 2. Налаштовуємо контролер
            ConfirmationController controller = loader.getController();
            controller.setMessage("Ви дійсно бажаєте видалити страву:\n" + selected.getName() + "?");

            // 3. Створюємо і показуємо Stage
            Stage stage = new Stage();
            stage.setScene(new javafx.scene.Scene(root));
            stage.initStyle(javafx.stage.StageStyle.TRANSPARENT); // Прозорість для круглих кутів
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL); // Блокує основне вікно
            stage.showAndWait(); // Чекаємо поки закриють

            // 4. Перевіряємо результат
            if (controller.isConfirmed()) {
                // Видаляємо з БД
                try (Connection conn = getConnection();
                     PreparedStatement pstmt = conn.prepareStatement("DELETE FROM menu_items WHERE id = ?")) {

                    pstmt.setInt(1, selected.getId());
                    pstmt.executeUpdate();

                    clearFields();
                    refreshTable();

                } catch (SQLException e) {
                    showAlert("Помилка БД", e.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Помилка", "Не вдалося відкрити вікно підтвердження: " + e.getMessage());
        }
    }

    // === СЛУЖБОВІ МЕТОДИ (БД) ===

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(SERVER_URL + TARGET_DB, USER, PASS);
    }

    @FXML
    private void refreshTable() {
        dataList.clear();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM menu_items ORDER BY id")) {
            while (rs.next()) {
                dataList.add(new DishItem(rs.getInt("id"), rs.getString("type"), rs.getString("name"), rs.getDouble("price")));
            }
        } catch (SQLException e) {
            System.err.println("Load Error: " + e.getMessage());
        }
    }

    @FXML
    private void filterDishes() {
        dataList.clear();
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM menu_items WHERE name LIKE ?")) {
            pstmt.setString(1, "%ка");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) dataList.add(new DishItem(rs.getInt("id"), rs.getString("type"), rs.getString("name"), rs.getDouble("price")));
        } catch (SQLException e) { showAlert("Помилка", e.getMessage()); }
    }

    private void createDatabaseIfNotExists() {
        try (Connection conn = DriverManager.getConnection(SERVER_URL + "postgres", USER, PASS); Statement stmt = conn.createStatement()) {
            if (!stmt.executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + TARGET_DB + "'").next()) {
                stmt.executeUpdate("CREATE DATABASE " + TARGET_DB);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void createTableIfNotExists() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS menu_items (id SERIAL PRIMARY KEY, type VARCHAR(20), name VARCHAR(100), price DOUBLE PRECISION)");
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Варіант методу для 3-х параметрів (Заголовок, Підзаголовок, Текст).
     * Виправляє помилку компіляції.
     */
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Додаємо стиль, щоб вікно не було білим (якщо стиль знайдено)
        try {
            String cssPath = "/io/github/serhii0659/JavaLabs/styles.css";
            var cssUrl = getClass().getResource(cssPath);
            if (cssUrl != null) {
                alert.getDialogPane().getStylesheets().add(cssUrl.toExternalForm());
                alert.getDialogPane().getStyleClass().add("dialog-pane");
            }
        } catch (Exception e) {
            // Ігноруємо помилки стилів, головне щоб вікно показалось
        }

        alert.showAndWait();
    }

    /**
     * Варіант методу для 2-х параметрів (Заголовок, Текст).
     * Викликає версію з 3-ма параметрами, передаючи null замість заголовка.
     */
    private void showAlert(String title, String content) {
        showAlert(title, null, content);
    }

    // === Window Logic ===
    @FXML private void closeWindow(ActionEvent event) { ((Stage)((Node)event.getSource()).getScene().getWindow()).close(); }
    @FXML private void handleWindowPress(MouseEvent event) { Stage s = (Stage)((Node)event.getSource()).getScene().getWindow(); xOffset = s.getX() - event.getScreenX(); yOffset = s.getY() - event.getScreenY(); }
    @FXML private void handleWindowDrag(MouseEvent event) { Stage s = (Stage)((Node)event.getSource()).getScene().getWindow(); s.setX(event.getScreenX() + xOffset); s.setY(event.getScreenY() + yOffset); }

    // === Model Class ===
    /**
     * Модель даних для таблиці.
     */
    public static class DishItem {
        private int id; private String type; private String name; private double price;

        /**
         * Конструктор моделі страви.
         * @param id ID запису
         * @param type Тип страви
         * @param name Назва
         * @param price Ціна
         */
        public DishItem(int id, String type, String name, double price) {
            this.id = id;
            this.type = type;
            this.name = name;
            this.price = price;
        }
        /** * Отримує ID.
         * @return ідентифікатор
         */
        public int getId() { return id; }
        /** * Отримує тип.
         * @return тип страви
         */
        public String getType() { return type; }
        /** * Отримує назву.
         * @return назва страви
         */
        public String getName() { return name; }
        /** * Отримує ціну.
         * @return ціна страви
         */
        public double getPrice() { return price; }
    }
}