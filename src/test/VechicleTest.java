package test;

import models.Vechicle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VechicleTest {

    @Test
    void setUp() {
        Vechicle test = new Vechicle();
        test.setUp(true);
        assertTrue(test.isUp());
    }

    @Test
    void setDown() {
        Vechicle test = new Vechicle();
        test.setUp(true);
        assertFalse(test.isDown());
        test.setDown(true);
        assertTrue(test.isDown());
    }

    @Test
    void getX() {
        Vechicle test = new Vechicle();
        test.loadCoords(1.0,2.0,30);
        assertEquals(1.0,test.getX());
    }

    @Test
    void getY() {
        Vechicle test = new Vechicle();
        test.loadCoords(1.0,4.0,30);
        assertEquals(4.0,test.getY());
    }

    @Test
    void use() {
        Vechicle test = new Vechicle();
        assertFalse(test.isUsed);
        test.use();
        assertTrue(test.isUsed);
        test.use();
        assertFalse(test.isUsed);
    }
}