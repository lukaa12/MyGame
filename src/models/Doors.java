package models;

import javafx.scene.shape.Rectangle;

public class Doors implements Usable {
    public Rectangle collisionBox;
    boolean closed;
    @Override
    public boolean isReachable() {
        return false;
    }

    @Override
    public void use() {

    }
}
