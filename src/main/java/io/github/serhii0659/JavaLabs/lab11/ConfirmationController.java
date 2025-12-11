package io.github.serhii0659.JavaLabs.lab11;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Контролер для модального вікна підтвердження дій.
 */
public class ConfirmationController {

    @FXML private Label messageLabel;

    private boolean confirmed = false;
    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Конструктор за замовчуванням.
     */
    public ConfirmationController() {
    }

    /**
     * Встановлює текст повідомлення у вікні.
     * @param text текст запитання
     */
    public void setMessage(String text) {
        messageLabel.setText(text);
    }

    /**
     * Повертає результат вибору користувача.
     * @return true якщо підтверджено, false якщо скасовано
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    @FXML
    private void onConfirm() {
        confirmed = true;
        closeWindow();
    }

    @FXML
    private void onCancel() {
        confirmed = false;
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.close();
    }

    /** Фіксація позиції вікна. */
    @FXML private void handleWindowPress(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    /** Перетягування вікна. */
    @FXML private void handleWindowDrag(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }
}