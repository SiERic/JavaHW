package me.sieric.cannon;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/** Class for graphical representation of a cannon */
public class Cannon {

    /** Cannon body */
    private Circle body = new Circle();

    /** Cannon barrel */
    private Line barrel = new Line();

    /** Barrel's angel (in [-pi; pi] ) */
    private double angle;

    /** X coordinate of body center (and the beginning of tje barrel) */
    private int x;

    /** Y coordinate of body center (and the beginning of tje barrel) */
    private int y;

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

    Cannon(int x, int y, Landscape landscape) {
        this.x = x;
        this.y = y;
        this.landscape = landscape;
        angle = Math.PI / 2;
        body.setRadius(BODY_RADIUS);
        updateBody();
        updateBarrel();
    }

    /**
     * Updates cannon center coordinates
     * @param newX new x coordinate
     * @param newY new y coordinate
     */
    public void updateCoordinates(int newX, int newY) {
        x = newX;
        y = newY;
        updateBody();
        updateBarrel();
    }

    /**
     * Changes barrel angle by delta
     * @param delta angle change
     */
    public void updateAngle(double delta) {
        angle = angle + delta;
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
            updateCoordinates(x - STEP, landscape.getYByX(x - Cannon.STEP));
        }
    }

    /** Moves cannon one step to the right */
    public void moveRight() {
        if (x + Cannon.STEP <= Landscape.WIDTH) {
            updateCoordinates(x + Cannon.STEP, landscape.getYByX(x + Cannon.STEP));
        }
    }

    /** Increases barrel angle by one angle step */
    public void increaseAngle() {
        if (angle + Cannon.ANGLE_STEP <= Math.PI) {
            updateAngle(Cannon.ANGLE_STEP);
        }
    }

    /** Decreases barrel angle by one angle step */
    public void decreaseAngle() {
        if (angle - Cannon.ANGLE_STEP >= 0) {
            updateAngle(-Cannon.ANGLE_STEP);
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
    public int getBarrelEndX() {
        return (int) (x + BARREL_LENGTH * Math.cos(angle));
    }

    /**
     * Gets current barrel end y coordinate
     * @return current barrel end y coordinate
     */
    public int getBarrelEndY() {
        return (int) (y - BARREL_LENGTH * Math.sin(angle));
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
        barrel.setEndX(x + BARREL_LENGTH * Math.cos(angle));
        barrel.setEndY(y - BARREL_LENGTH * Math.sin(angle));
    }

}
