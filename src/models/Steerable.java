package models;

import javafx.scene.Node;

import java.util.Vector;

public interface Steerable {
    void setUp(boolean set);
    void setDown(boolean set);
    void setLeft(boolean set);
    void setRight(boolean set);
    void setJump(boolean set);
    void setSprint(boolean set);
    void setSquat(boolean set);
    void drawMe();
    void setImage();
    void update(double deltaTime, Vector<Node> collide);
    double getX();
    double getY();
}
