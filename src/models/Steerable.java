package models;

import engine.Updater;

public interface Steerable {
    void setUp(boolean set);
    void setDown(boolean set);
    void setLeft(boolean set);
    void setRight(boolean set);
    void setJump(boolean set);
    void setSprint(boolean set);
    void setSquat(boolean set);
    Updater getSteering();
    void drawMe();
    void setImage();
//    void update(double deltaTime);

}
