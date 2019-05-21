package test;

import javafx.scene.Node;
import models.Player;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @org.junit.jupiter.api.Test
    void update() {
        Player tester = new Player(1);
        double x, y;
        x = tester.getX();
        y = tester.getY();
        tester.update(0.5,new Vector<Node>());
        assertEquals(x,tester.getX());
        assertEquals(y,tester.getY());
    }

    @org.junit.jupiter.api.Test
    void getX() {
        Player tester = new Player(1);
        assertEquals(960.0,tester.getX());
    }

    @org.junit.jupiter.api.Test
    void getY() {
        Player tester = new Player(1);
        assertEquals(540.0,tester.getY());
    }
}