package models;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.Vector;

public class Player implements Steerable {
    private final static int WALKSPEED = 125;
    private final static int RUNSPEED = 456;
    private double x = 960.0;
    private double y = 540.0;
    private int rotation = 180;
    private boolean  isRunning, isSquating, isJumping;
    private boolean up, down, left, right, colision;
    private ImageView playerTransform;
    private Image stand, walkr, walkl, runr, runl, cl;
    private double step =0.0;
    private Rectangle bounds;
    public Pane scene;

    public Player(ImageView aPlayerTransform) {
        stand = new Image(this.getClass().getResource("/resources/player.png").toString());
        cl = new Image(this.getClass().getResource("/resources/playerCl.png").toString());
        walkl = new Image(this.getClass().getResource("/resources/walkl.png").toString());
        walkr = new Image(this.getClass().getResource("/resources/walkr.png").toString());
        runl = new Image(this.getClass().getResource("/resources/runl.png").toString());
        runr = new Image(this.getClass().getResource("/resources/runr.png").toString());
        playerTransform = aPlayerTransform;
        playerTransform.setImage(stand);
        playerTransform.setX(x);
        playerTransform.setY(y);
        playerTransform.setRotate(rotation);
        bounds = new Rectangle();
    }
    public Player(int test) {
        bounds = new Rectangle();
        playerTransform = new ImageView();
    }

    public synchronized void drawMe() {
        this.setImage();
        playerTransform.setY(y);
        playerTransform.setX(x);
        scene.setTranslateY(540.0-y);
        scene.setTranslateX(960.0-x);
        playerTransform.setRotate(rotation);
    }



    @Override
    public synchronized void setImage() {
        if((!up&&!down&&!right&&!left)||colision) {
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
            if(playerTransform.getImage().equals(stand)||playerTransform.getImage().equals(cl)) {
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
        double oldX = x, oldY = y;
        boolean inMotion = false;
        colision = false;
        if(right&&!left) {
            if(up&&!down) {
                inMotion = true;
                rotation = 45;
            } else if(down&&!up) {
                rotation = 135;
                inMotion = true;
            } else {
                rotation = 90;
                inMotion = true;
            }
        }
        else if(!right&&left) {
            if(up&&!down) {
                rotation = 315;
                inMotion = true;
            } else if(down&&!up) {
                rotation = 225;
                inMotion = true;
            } else {
                rotation = 270;
                inMotion = true;
            }
        }
        else {
            if(up&&!down) {
                rotation = 0;
                inMotion = true;
            } else if(down&&!up) {
                rotation = 180;
                inMotion = true;
            }
        }
        if(this.isRunning&&inMotion) {
            x += Math.sin(Math.toRadians(rotation))*RUNSPEED*deltaTime;
            y -= Math.cos(Math.toRadians(rotation))*RUNSPEED*deltaTime;
        }
        else if(inMotion) {
            x += Math.sin(Math.toRadians(rotation))*WALKSPEED*deltaTime;
            y -= Math.cos(Math.toRadians(rotation))*WALKSPEED*deltaTime;
        }
        this.step +=deltaTime;
        bounds.setX(x);
        bounds.setY(y);
        bounds.setHeight(playerTransform.getFitHeight());
        bounds.setWidth(playerTransform.getFitWidth());
        bounds.setRotate(rotation);
        for(Node i: collide) {
            if(i.getBoundsInParent().intersects(bounds.getBoundsInParent())) {
                colision = true;
            }
        }
        if(colision) {
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

//    public int getRotation() {
//        return rotation;
//    }

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

    public boolean isUp() {return up;}
    public boolean isLeft() {return left;}
}
