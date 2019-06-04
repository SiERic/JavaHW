package me.sieric.findpairs;

import javafx.scene.control.Button;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    private Button[][] field;
    private final int N = 4;
    private me.sieric.findpairs.Controller controller;

    @BeforeEach
    void setUp() {
        field = new Button[N][N];
        controller = new Controller(field);
        controller.setUp();
    }

    @Test
    void setUpTest() {
        int[] cntNumbers = new int[N * N / 2];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int value = Integer.parseInt(field[i][j].getText());
                assertTrue(value >= 0 && value < N * N / 2);
                cntNumbers[value]++;
            }
        }
        for (int i = 0; i < N * N / 2; i++) {
            assertEquals(2, cntNumbers[i]);
        }
    }
}