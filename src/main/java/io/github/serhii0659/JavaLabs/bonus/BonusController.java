package io.github.serhii0659.JavaLabs.bonus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.*;

/**
 * Контролер для бонусу.
 * Будує графік функції на основі даних, збережених у базі даних PostgreSQL.
 */
public class BonusController {
    private static final String DB_URL = "jdbc:postgresql://localhost:13000/lab11_menu_db"; // Використовуємо ту ж БД
    private static final String USER = "postgres";
    private static final String PASS = "3a3U}24Fx5;L";

    @FXML private LineChart<Number, Number> lineChart;
    private double xOffset = 0; private double yOffset = 0;

    /**
     * Ініціалізація. Створює таблицю в БД, якщо її немає.
     */
    @FXML
    public void initialize() {
        createTableIfNotExists();
    }

    /**
     * Генерує дані (функція sin(x)) і записує їх у базу даних.
     * Створює мінімум 20 точок.
     */
    @FXML
    protected void onGenerateData() {
        String insertSQL = "INSERT INTO graph_points (x, y) VALUES (?, ?)";
        String clearSQL = "TRUNCATE TABLE graph_points"; // Очищаємо старі дані

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            // Спочатку очищаємо таблицю
            stmt.execute(clearSQL);

            // Генеруємо точки від -10 до 10
            for (double x = -10.0; x <= 10.0; x += 0.5) {
                double y = Math.sin(x); // Функція y = sin(x)

                pstmt.setDouble(1, x);
                pstmt.setDouble(2, y);
                pstmt.addBatch(); // Додаємо в пакет
            }

            pstmt.executeBatch(); // Виконуємо вставку всіх точок разом
            showAlert("Успіх", "У базу даних записано 41 точку (функція Sin).");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Помилка БД", e.getMessage());
        }
    }

    /**
     * Зчитує точки з БД і малює графік.
     */
    @FXML
    protected void onLoadGraph() {
        lineChart.getData().clear(); // Очищаємо старий графік

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Дані з PostgreSQL");

        String sql = "SELECT x, y FROM graph_points ORDER BY x ASC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int count = 0;
            while (rs.next()) {
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                series.getData().add(new XYChart.Data<>(x, y));
                count++;
            }

            if (count == 0) {
                showAlert("Інфо", "Таблиця в БД порожня. Спочатку згенеруйте дані.");
            } else {
                lineChart.getData().add(series);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Помилка БД", e.getMessage());
        }
    }

    /**
     * Очищає візуальний графік (але не БД).
     */
    @FXML
    protected void onClearGraph() {
        lineChart.getData().clear();
    }

    // === СЛУЖБОВІ МЕТОДИ ===

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS graph_points (" +
                "id SERIAL PRIMARY KEY, " +
                "x DOUBLE PRECISION NOT NULL, " +
                "y DOUBLE PRECISION NOT NULL" +
                ");";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Table creation failed: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // === Window Logic ===
    @FXML private void closeWindow(ActionEvent event) { ((Stage)((Node)event.getSource()).getScene().getWindow()).close(); }
    @FXML private void handleWindowPress(MouseEvent event) { Stage s = (Stage)((Node)event.getSource()).getScene().getWindow(); xOffset = s.getX() - event.getScreenX(); yOffset = s.getY() - event.getScreenY(); }
    @FXML private void handleWindowDrag(MouseEvent event) { Stage s = (Stage)((Node)event.getSource()).getScene().getWindow(); s.setX(event.getScreenX() + xOffset); s.setY(event.getScreenY() + yOffset); }
}