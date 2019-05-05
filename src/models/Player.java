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
    private Image stand, walkr, walkl;
    public double step;

    public Player() {
        stand = new Image(this.getClass().getResource("/resources/player.png").toString());
        walkl = new Image(this.getClass().getResource("/resources/walkl.png").toString());
        walkr = new Image(this.getClass().getResource("/resources/walkr.png").toString());
        playerTransform = new ImageView(stand);
//        playerTransform.setImage(walkl);
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

    public void setX(double x) {
//        System.out.println("old x:"+this.x+"new x:"+x);
        this.x = x;
    }

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
//        System.out.println(step);
        this.setImage();
        playerTransform.setY(y);
        playerTransform.setX(x);
        playerTransform.setRotate(rotation);
    }

    @Override
    public synchronized void setImage() {
       // boolean right = false;
        if(!up&&!down&&!right&&!left) {
            playerTransform.setImage(stand);
        }
        if(step>60.0) {
            System.out.println("Step");
            step = 0;
            if(!playerTransform.getImage().equals(walkl)) {
                System.out.println("Lewa");
                playerTransform.setImage(walkl);
            }
            else if(!playerTransform.getImage().equals(walkr)) {
                System.out.println("Prawa");
                playerTransform.setImage(walkr);
            }
        }
    }
}
