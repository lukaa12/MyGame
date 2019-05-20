package models;

import javafx.scene.Node;

import java.util.Vector;

public class Vechicle implements Steerable {
    double[] accelerationMap;
    final double maxTurn;
    final double brakeForce;
    double speed;
    double turn;

    public Vechicle() {
        maxTurn = 0.0;
        brakeForce = 0.0;
    }

    @Override
    public void setUp(boolean set) {

    }

    @Override
    public void setDown(boolean set) {

    }

    @Override
    public void setLeft(boolean set) {

    }

    @Override
    public void setRight(boolean set) {

    }

    @Override
    public void setJump(boolean set) {

    }

    @Override
    public void setSprint(boolean set) {

    }

    @Override
    public void setSquat(boolean set) {

    }

    @Override
    public void drawMe() {

    }

    @Override
    public void setImage() {

    }

    @Override
    public void update(double deltaTime, Vector<Node> collide) {

    }

    @Override
    public double getX() {
        return 0;
    }

    @Override
    public double getY() {
        return 0;
    }
}
