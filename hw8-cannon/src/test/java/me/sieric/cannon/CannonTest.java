package me.sieric.cannon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CannonTest {

    private Cannon cannon;
    private Landscape landscape = new Landscape();

    @Test
    void testGet() {
        cannon = new Cannon(20, 40, new Landscape());
        assertEquals(20, cannon.getBody().getCenterX());
        assertEquals(40, cannon.getBody().getCenterY());
        assertEquals(cannon.getBarrel().getEndX(), cannon.getBarrelEndX());
        assertEquals(cannon.getBarrel().getEndY(), cannon.getBarrelEndY());
        assertEquals(Math.PI / 2, cannon.getAngle());
    }

    @Test
    void testMoves() {
        cannon = new Cannon(0, landscape.getYByX(0), landscape);
        cannon.moveLeft();
        assertEquals(0, cannon.getBody().getCenterX());
        cannon.moveRight();
        assertTrue(cannon.getBody().getCenterX() > 0);
        cannon.moveLeft();
        assertEquals(0, cannon.getBody().getCenterX());

        cannon.increaseAngle();
        assertTrue(cannon.getAngle() > Math.PI / 2);
        cannon.decreaseAngle();
        assertEquals(Math.PI / 2, cannon.getAngle());
        cannon.decreaseAngle();
        assertTrue(cannon.getAngle() < Math.PI / 2);
        cannon.increaseAngle();
        assertEquals(Math.PI / 2, cannon.getAngle());
    }
}