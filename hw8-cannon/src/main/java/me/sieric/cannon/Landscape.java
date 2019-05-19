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

    /** List of the ends of the mountain slopes */
    private ArrayList<Point> points = new ArrayList<>();

    /** Generates random mountains */
    Landscape() {
        Random rnd = new Random(System.currentTimeMillis());
        int x0 = 0;
        int y0 = rnd.nextInt(HEIGHT / 4) + HEIGHT / 4;
        points.add(new Point(x0, y0));
        while (x0 < WIDTH) {
            int newX = x0 + WIDTH / 16 + rnd.nextInt(WIDTH / 16);
            int newY = y0 - (HEIGHT / 8) + rnd.nextInt(HEIGHT / 4);
            newX = min(newX, WIDTH);
            newY = min(newY, HEIGHT * 3 / 4);
            newY = max(newY, HEIGHT / 4);
            mounts.add(new Line(x0, invertY(y0), newX, invertY(newY)));
            points.add(new Point(newX, newY));
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
    public int getYByX(int x) {
        for (int i = 0; i < points.size(); i++) {
            if (x >= points.get(i).x && x <= points.get(i + 1).x) {
                return invertY(points.get(i).y +
                        (points.get(i + 1). y - points.get(i).y) *
                                (x - points.get(i).x) / (points.get(i + 1).x - points.get(i).x));
            }
        }
        return HEIGHT;
    }

    /**
     * Inverts y coordinate
     * @param y given y coordinate
     * @return the inverted one
     */
    private static int invertY(int y) {
        return HEIGHT - y;
    }

    /** Class to store 2D points */
    private static class Point {
        private int x;
        private int y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}