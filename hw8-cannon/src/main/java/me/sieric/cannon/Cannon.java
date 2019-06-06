package me.sieric.cannon;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/** Class for graphical representation of a cannon */
public class Cannon {

    /** Cannon body */
    private Circle body = new Circle();

    /** Cannon barrel */
    private Line barrel = new Line();

    /** Barrel's angel (in [0; pi] ) */
    private double angle;

    /** X coordinate of body center (and the beginning of the barrel) */
    private double x;

    /** Y coordinate of body center (and the beginning of the barrel) */
    private double y;

    /** Body radius (depending on screen size) */
    private final static int BODY_RADIUS = Landscape.WIDTH / 80;

    /** Barrel length (depending on screen size) */
    private final static int BARREL_LENGTH = Landscape.WIDTH / 40;

    /** Length of one cannon movement */
    public final static int STEP = BODY_RADIUS / 2;

    /** Angle step of barrel movement*/
    public final static double ANGLE_STEP = Math.PI / 30;

    /** Current landscape */
    private Landscape landscape;

    Cannon(double x, double y, Landscape landscape) {
        this.x = x;
        this.y = y;
        this.landscape = landscape;
        angle = Math.PI / 2;
        body.setRadius(BODY_RADIUS);
        updateBody();
        updateBarrel();
    }

    /**
     * Gets cannon body
     * @return cannon body
     */
    public Circle getBody() {
        return body;
    }

    /**
     * Gets barrel
     * @return barrel
     */
    public Line getBarrel() {
        return barrel;
    }

    /** Moves cannon one step to the left */
    public void moveLeft() {
        if (x - Cannon.STEP >= 0) {
            x = x - STEP;
            y = landscape.getYByX(x);
            updateBody();
            updateBarrel();
        }
    }

    /** Moves cannon one step to the right */
    public void moveRight() {
        if (x + Cannon.STEP <= Landscape.WIDTH) {
            x = x + STEP;
            y = landscape.getYByX(x);
            updateBody();
            updateBarrel();
        }
    }

    /** Increases barrel angle by one angle step */
    public void increaseAngle() {
        if (angle + ANGLE_STEP <= Math.PI) {
            angle += ANGLE_STEP;
            updateBarrel();
        }
    }

    /** Decreases barrel angle by one angle step */
    public void decreaseAngle() {
        if (angle - ANGLE_STEP >= 0) {
            angle -= ANGLE_STEP;
            updateBarrel();
        }
    }

    /**
     * Gets current barrel angle
     * @return current barrel angle
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Gets current barrel end x coordinate
     * @return current barrel end x coordinate
     */
    public double getBarrelEndX() {
        return x + BARREL_LENGTH * Math.cos(angle);
    }

    /**
     * Gets current barrel end y coordinate
     * @return current barrel end y coordinate
     */
    public double getBarrelEndY() {
        return y - BARREL_LENGTH * Math.sin(angle);
    }

    /** Updates body position with current coordinates */
    private void updateBody() {
        body.setCenterX(x);
        body.setCenterY(y);
    }

    /** Updates barrel position with current coordinates and angle */
    private void updateBarrel() {
        barrel.setStartX(x);
        barrel.setStartY(y);
        barrel.setEndX(getBarrelEndX());
        barrel.setEndY(getBarrelEndY());
    }
}
