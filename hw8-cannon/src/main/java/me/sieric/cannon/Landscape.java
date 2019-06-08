package me.sieric.cannon;

import javafx.scene.shape.Line;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

/**
 * Class for current game landscape
 * Generates random mountains in constructor
 */
public class Landscape {

    /** Game screen width */
    public static final int WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.5);

    /** Game screen height */
    public static final int HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.5);

    /** Lines list representing mountains */
    private ArrayList<Line> mounts = new ArrayList<>();

    /** Generates random mountains */
    Landscape() {
        Random rnd = new Random(System.currentTimeMillis());
        int x0 = 0;
        int y0 = rnd.nextInt(HEIGHT / 4) + HEIGHT / 4;
        while (x0 < WIDTH) {
            int newX = x0 + WIDTH / 16 + rnd.nextInt(WIDTH / 16);
            int newY = y0 - (HEIGHT / 8) + rnd.nextInt(HEIGHT / 4);
            newX = min(newX, WIDTH);
            newY = min(newY, HEIGHT * 3 / 4);
            newY = max(newY, HEIGHT / 4);
            mounts.add(new Line(x0, invertY(y0), newX, invertY(newY)));
            x0 = newX;
            y0 = newY;
        }
    }

    /**
     * Gets generated mountains
     * @return list of generated mountains (Lines)
     */
    public List<Line> getMounts() {
        return mounts;
    }

    /**
     * Gets y coordinate on mountains by x coordinate
     * @param x given x coordinate
     * @return y coordinate on mountains
     */
    public double getYByX(double x) {
        for (int i = 0; i < mounts.size(); i++) {
            if (x >= mounts.get(i).getStartX() && x <= mounts.get(i).getEndX()) {
                return mounts.get(i).getStartY() +
                        (mounts.get(i).getEndY() - mounts.get(i).getStartY()) *
                                (x - mounts.get(i).getStartX()) / (mounts.get(i).getEndX() - mounts.get(i).getStartX());
            }
        }
        return HEIGHT;
    }

    /**
     * Inverts y coordinate
     * @param y given y coordinate
     * @return the inverted one
     */
    private static double invertY(double y) {
        return HEIGHT - y;
    }
}