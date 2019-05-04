package models;

import engine.PlayerSteering;
import engine.Updater;
import javafx.scene.image.ImageView;

public class Player implements Steerable {
    private int x = 960;
    private int y = 540;
    private int rotation = 180;
    private boolean  isRunning, isSquating, isJumping;
    private boolean up, down, left, right;
    private PlayerSteering steering;
    private ImageView playerTransform;

    public Player() {
        steering = new PlayerSteering(this);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        System.out.println("old x:"+this.x+"new x:"+x);
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        System.out.println("old y:"+this.y+"new y:"+y);
        this.y = y;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    @Override
    public void setUp(boolean set) {
        up= set;
    }

    @Override
    public void setDown(boolean set) {
        down= set;
    }

    @Override
    public void setLeft(boolean set) {
        left= set;
    }

    @Override
    public void setRight(boolean set) {
        right= set;
    }

    @Override
    public void setJump(boolean set) {
        isJumping= set;
    }

    @Override
    public void setSprint(boolean set) {
        isRunning= set;
    }

    @Override
    public void setSquat(boolean set) {
        isSquating= set;
    }

    @Override
    public Updater getSteering() {
        return this.steering;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isSquating() {
        return isSquating;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isUp() {
        return up;
    }

    public void setPlayerTransform(ImageView playerTransform) {
        this.playerTransform = playerTransform;
    }
    public void drawMe() {
        playerTransform.setY(y);
        playerTransform.setX(x);
        playerTransform.setRotate(rotation);
    }
}
