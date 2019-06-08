package me.sieric.cannon;

import javafx.scene.shape.Circle;

/** Class for graphical representation of a flying bullet */
public class Bullet {

    /** Gravitational acceleration */
    private static final double G = 9.81;

    /** Magical time const for flight to look more normal */
    private static final int TIME_CONST = 4;

    /** Starting x coordinate*/
    private final double x0;

    /** Starting y coordinate */
    private final double y0;

    /** Current bullet type */
    private BulletType type;

    /** Graphical representation of bullet */
    private Circle body;

    /** Starting flight angle */
    private final double angle;

    /** Radius of the smallest bullet */
    private final static int bulletMinRadius = Landscape.WIDTH / 200;

    /** Speed of the largest bullet */
    private final static int bulletMinSpeed = Landscape.WIDTH / 32;

    /** Bullet types speed step */
    private final static int bulletSpeedStep = bulletMinSpeed / 2;

    public Bullet(double x0, double y0, int typeId, double angle) {
        this.angle = angle;
        this.type = BulletType.getById(typeId);
        this.x0 = x0;
        this.y0 = y0;
        body = new Circle(x0, y0, type.radius);
    }

    /**
     * Gets x coordinate after {@code time} time units
     * @param time elapsed time
     * @return x coordinate after {@code time} time units
     */
    public double getXByTime(int time) {
        return x0 + type.speed * time * Math.cos(angle) / TIME_CONST;
    }

    /**
     * Gets y coordinate after {@code time} time units
     * @param time elapsed time
     * @return y coordinate after {@code time} time units
     */
    public double getYByTime(int time) {
        return y0 - type.speed * time * Math.sin(angle) / TIME_CONST + G * time * time / 2 / TIME_CONST / TIME_CONST;
    }

    /**
     * Gets bullet types number
     * @return bullet types number
     */
    public static int getTypesNumber() {
        return BulletType.size();
    }

    /**
     * Gets acceptable distance between bullet's center and target
     * @param typeId current bullet's type
     * @return acceptable distance between bullet's center and target
     */
    public static int getTypeDist(int typeId) {
        return BulletType.getById(typeId).getTypeDist();
    }

    /**
     * Sets new bullet center x coordinate
     * @param x new x coordinate
     */
    public void setX(double x) {
        body.setCenterX(x);
    }

    /**
     * Sets new bullet center y coordinate
     * @param y new y coordinate
     */
    public void setY(double y) {
        body.setCenterY(y);
    }

    /**
     * Gets bullet radius
     * @return bullet radius
     */
    public int getRadius() {
        return type.radius;
    }

    /**
     * Gets bullet body
     * @return bullet body
     */
    public Circle getBody() {
        return body;
    }

    /**
     * Gets bullet type name
     * @param typeId bullet type
     * @return bullet type name
     */
    public static String getBulletTypeName(int typeId) {
        return BulletType.getById(typeId).toString();
    }

    /** Enum to store information about bullet types */
    private enum BulletType {

        TINY(bulletMinRadius, bulletMinSpeed + 5 * bulletSpeedStep),
        SMALL(bulletMinRadius + 1, bulletMinSpeed + 3 * bulletSpeedStep),
        NORMAL(bulletMinRadius + 2, bulletMinSpeed + 2 * bulletSpeedStep),
        BIG(bulletMinRadius + 3, bulletMinSpeed + bulletSpeedStep),
        HUGE(bulletMinRadius + 4, bulletMinSpeed);

        private final int radius;
        private final int speed;

        BulletType(int radius, int speed) {
            this.radius = radius;
            this.speed = speed;
        }

        private static BulletType getById(int id) {
            return values()[id];
        }

        private int getTypeDist() {
            return radius * radius / 4;
        }

        private static int size() {
            return values().length;
        }
    }
}
