package models;

import engine.PlayerSteering;
import engine.Updater;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player implements Steerable {
    private double x = 960.0;
    private double y = 540.0;
    private int rotation = 180;
    private boolean  isRunning, isSquating, isJumping;
    private boolean up, down, left, right;
    private PlayerSteering steering;
    private ImageView playerTransform;
    private Image stand, walkr, walkl, runr, runl;
    public double step =59.5;

    public Player() {
        stand = new Image(this.getClass().getResource("/resources/player.png").toString());
        walkl = new Image(this.getClass().getResource("/resources/walkl.png").toString());
        walkr = new Image(this.getClass().getResource("/resources/walkr.png").toString());
        runl = new Image(this.getClass().getResource("/resources/runl.png").toString());
        runr = new Image(this.getClass().getResource("/resources/runr.png").toString());
        playerTransform = new ImageView(stand);
        playerTransform.setX(x);
        playerTransform.setY(y);
        playerTransform.setRotate(rotation);
        steering = new PlayerSteering(this,playerTransform);
    }

    public ImageView getPlayerTransform() {
        return playerTransform;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) { this.x = x; }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void addX(double x) {
        this.x += x;
    }

    public void addY(double y) {
        this.y += y;
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

    public synchronized void drawMe() {
        this.setImage();
        playerTransform.setY(y);
        playerTransform.setX(x);
        playerTransform.setRotate(rotation);
    }

    @Override
    public synchronized void setImage() {
        if(!up&&!down&&!right&&!left) {
            playerTransform.setImage(stand);
            step =59.5;
        }
        else if(step>60.0) {
            step = 0;
            if(!playerTransform.getImage().equals(walkl)) {
                playerTransform.setImage(walkl);
            }
            else if(!playerTransform.getImage().equals(walkr)) {
                playerTransform.setImage(walkr);
            }
        }
    }
}
