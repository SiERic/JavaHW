package me.sieric.findpairs;

import javafx.application.Platform;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Game controller class
 */
public class Controller {
    private int[][] numbersField;
    private Button[][] field;

    private int size;
    private int lastOpenedButtonI = -1;
    private int lastOpenedButtonJ = -1;
    private int pairsFound;

    /**
     * Creates new game controller
     * @param field given buttons
     */
    Controller(Button[][] field) {
        this.size = field.length;
        this.field = field;
    }

    /**
     * Starts new game
     */
    public void setUp() {
        pairsFound = 0;

        setUpField();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = new Button();
                Button button = field[i][j];
                int finalI = i;
                int finalJ = j;
                button.setOnMouseClicked(event -> {
                    try {
                        openCard(finalI, finalJ);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private synchronized void openCard(int i, int j) throws InterruptedException {
        field[i][j].setText(String.valueOf(numbersField[i][j]));
        if (lastOpenedButtonI == -1) {
            lastOpenedButtonI = i;
            lastOpenedButtonJ = j;
            return;
        }
        Button lastOpened = field[lastOpenedButtonI][lastOpenedButtonJ];
        if (field[i][j] == lastOpened) {
            field[i][j].setText("");
            lastOpenedButtonI = -1;
            lastOpenedButtonJ = -1;
            return;
        }
        if (numbersField[i][j] == numbersField[lastOpenedButtonI][lastOpenedButtonJ]) {
            field[i][j].setDisable(true);
            lastOpened.setDisable(true);
            pairsFound++;
            lastOpenedButtonI = -1;
            lastOpenedButtonJ = -1;
            return;
        }
        Thread change = new Thread(() -> {
            synchronized (this) {
                try {
                    wait(500);
                } catch (InterruptedException ignored) {

                }
                Platform.runLater(() -> {
                    field[i][j].setText("");
                    lastOpened.setText("");
                });
            }
        });
        change.start();

        lastOpenedButtonI = -1;
        lastOpenedButtonJ = -1;
    }


    private void setUpField() {
        List<Integer> perm = new ArrayList<>();
        for (int i = 0; i < size * size / 2; i++) {
            perm.add(i);
            perm.add(i);
        }
        Collections.shuffle(perm);
        numbersField = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                numbersField[i][j] = perm.get(i * size + j);
            }
        }
    }

    public boolean isFinished() {
        return pairsFound == field.length * field.length;
    }
}