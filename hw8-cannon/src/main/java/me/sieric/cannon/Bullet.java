package me.sieric.cannon;

import javafx.scene.shape.Circle;

import java.util.ArrayList;

/** Class for graphical representation of a flying bullet */
public class Bullet {

    /** Gravitational acceleration */
    private static final double G = 9.81;

    /** Magical time const for flight to look more normal */
    private static final int TIME_CONST = 4;

    /** Starting x coordinate*/
    private final int x0;

    /** Starting y coordinate */
    private final int y0;

    /** Bullet radius (depends on type) */
    private final int radius;

    /** Bullet speed (depends on type) */
    private final double speed;

    /** Graphical representation of bullet */
    private Circle body;

    /** Starting flight angle */
    private final double angle;

    /** List of bullet types (speed and size differ) */
    private static ArrayList<BulletType> types = new ArrayList<>();
    static {
        int minRadius = Landscape.WIDTH / 200;
        int minSpeed = Landscape.WIDTH / 32;
        int speedStep = minSpeed / 2;
        types.add(new BulletType(minRadius, minSpeed + 5 * speedStep));
        types.add(new BulletType(minRadius + 1, minSpeed + 3 * speedStep));
        types.add(new BulletType(minRadius + 2, minSpeed + 2 * speedStep));
        types.add(new BulletType(minRadius + 3, minSpeed + speedStep));
        types.add(new BulletType(minRadius + 4, minSpeed));
    }

    public Bullet(int x0, int y0, int type, double angle) {
        this.radius = types.get(type).radius;
        this.angle = angle;
        this.speed = types.get(type).speed;
        this.x0 = x0;
        this.y0 = y0;
        body = new Circle(x0, y0, radius);
    }

    /**
     * Gets x coordinate after {@code time} time units
     * @param time elapsed time
     * @return x coordinate after {@code time} time units
     */
    public int getXByTime(int time) {
        return (int) (x0 + speed * time * Math.cos(angle) / TIME_CONST);
    }

    /**
     * Gets y coordinate after {@code time} time units
     * @param time elapsed time
     * @return y coordinate after {@code time} time units
     */
    public int getYByTime(int time) {
        return (int) (y0 - speed * time * Math.sin(angle) / TIME_CONST + G * time * time / 2 / TIME_CONST / TIME_CONST);
    }

    /**
     * Gets acceptable distance between bullet's center and target
     * @param type current bullet's type
     * @return acceptable distance between bullet's center and target
     */
    public static int getTypeDist(int type) {
        return types.get(type).radius * types.get(type).radius / 4;
    }

    /** Gets bullet types number */
    public static int getTypesNumber() {
        return types.size();
    }

    /**
     * Sets new bullet center x coordinate
     * @param x new x coordinate
     */
    public void setX(int x) {
        body.setCenterX(x);
    }

    /**
     * Sets new bullet center y coordinate
     * @param y new y coordinate
     */
    public void setY(int y) {
        body.setCenterY(y);
    }

    /**
     * Gets bullet radius
     * @return bullet radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Gets bullet body
     * @return bullet body
     */
    public Circle getBody() {
        return body;
    }

    /** Data class ti store information about bullet type (size and speed) */
    private static class BulletType {
        private final int radius;
        private final int speed;

        private BulletType(int radius, int speed) {
            this.radius = radius;
            this.speed = speed;
        }
    }
}
