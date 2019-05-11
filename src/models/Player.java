package models;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Vector;

public class Player implements Steerable {
    private final static int WALKSPEED = 125;
    private final static int RUNSPEED = 456;
    private double x = 960.0;
    private double y = 540.0;
    private int rotation = 180;
    private boolean  isRunning, isSquating, isJumping;
    private boolean up, down, left, right;
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
        }
        else if(step>0.4&&!isRunning) {
            step = 0;
            if(!playerTransform.getImage().equals(walkr)&&!playerTransform.getImage().equals(walkl)) {
                playerTransform.setImage(walkr);
            }
            else if(playerTransform.getImage().equals(walkl)) {
                playerTransform.setImage(walkr);
            }
            else if(playerTransform.getImage().equals(walkr)) {
                playerTransform.setImage(walkl);
            }
        }
        else if(step>0.24&&isRunning) {
            step = 0;
            if(playerTransform.getImage().equals(stand)) {
                playerTransform.setImage(runr);
            }
            else if(playerTransform.getImage().equals(runr)) {
                playerTransform.setImage(walkl);
            }
            else if(playerTransform.getImage().equals(walkl)) {
                playerTransform.setImage(runl);
            }
            else if(playerTransform.getImage().equals(runl)) {
                playerTransform.setImage(walkr);
            }
            else if(playerTransform.getImage().equals(walkr)) {
                playerTransform.setImage(runr);
            }
        }
    }

    @Override
    public void update(double deltaTime, Vector<Node> collide) {
        int direction = rotation;
        double oldX = x, oldY = y;
        boolean inMotion = false, collision = false;
        if(this.isRight()&&!this.isLeft()) {
            if(this.isUp()&&!this.isDown()) {
                inMotion = true;
                direction = 45;
            } else if(this.isDown()&&!this.isUp()) {
                direction = 135;
                inMotion = true;
            } else {
                direction = 90;
                inMotion = true;
            }
        }
        else if(!this.isRight()&&this.isLeft()) {
            if(this.isUp()&&!this.isDown()) {
                direction = 315;
                inMotion = true;
            } else if(this.isDown()&&!this.isUp()) {
                direction = 225;
                inMotion = true;
            } else {
                direction = 270;
                inMotion = true;
            }
        }
        else {
            if(this.isUp()&&!this.isDown()) {
                direction = 0;
                inMotion = true;
            } else if(this.isDown()&&!this.isUp()) {
                direction = 180;
                inMotion = true;
            }
        }
        rotation = direction;
        if(this.isRunning()&&inMotion) {
            x += Math.sin(Math.toRadians(direction))*RUNSPEED*deltaTime;
            y -= Math.cos(Math.toRadians(direction))*RUNSPEED*deltaTime;
        }
        else if(inMotion) {
            x += Math.sin(Math.toRadians(direction))*WALKSPEED*deltaTime;
            y -= Math.cos(Math.toRadians(direction))*WALKSPEED*deltaTime;
        }
        this.step +=deltaTime;
        for(Node i: collide) {
            if(i.getBoundsInParent().intersects(playerTransform.getBoundsInParent()))
                collision = true;
        }
        if(collision) {
            x= oldX;
            y= oldY;
        }
    }

    public ImageView getPlayerTransform() {
        return playerTransform;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getRotation() {
        return rotation;
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

}
