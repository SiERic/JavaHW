package me.sieric.cannon;

import javafx.scene.shape.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LandscapeTest {

    private Landscape landscape;
    private List<Line> mounts;

    @BeforeEach
    void setUp() {
        landscape = new Landscape();
        mounts = landscape.getMounts();
    }

    @Test
    void testLandscapeIsCorrect() {
        for (int i = 0; i < mounts.size() - 1; i++) {
            assertEquals(mounts.get(i).getEndX(), mounts.get(i + 1).getStartX());
            assertEquals(mounts.get(i).getEndY(), mounts.get(i + 1).getStartY());
        }
        assertEquals(0, mounts.get(0).getStartX());
        assertEquals(Landscape.WIDTH, mounts.get(mounts.size() - 1).getEndX());
    }

    @Test
    void testYOnCornersAreCorrect() {
        for (int i = 0; i < mounts.size(); i++) {
            assertEquals(mounts.get(i).getStartY(), landscape.getYByX((int) mounts.get(i).getStartX()));
            assertEquals(mounts.get(i).getEndY(), landscape.getYByX((int) mounts.get(i).getEndX()));
        }
    }
}