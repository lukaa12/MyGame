package models;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import view.Main;

import java.util.Vector;

public class Vechicle implements Steerable, Usable {
    double acceleration;
    final double maxTurn;
    final double maxSpeed;
    final double brakeForce;
    double speed = 0.0;
    double turn = 0.0;
    double rotation;
    double x, y;
    ImageView carTransform;
    Rectangle bounds;
    boolean forward, brake, left, right, handbrake, colision;
    public boolean prevForward, prevBrake, isUsed=false;
    public Pane scene;

    public Vechicle() {
        maxTurn = 30.0;
        brakeForce = 4.0;
        maxSpeed = 40.0;
        bounds = new Rectangle();
    }

    public void setImage(ImageView aCarTransform) {
        carTransform = aCarTransform;
        x = carTransform.getX();
        y = carTransform.getY();
        rotation = aCarTransform.getRotate();
        bounds.setX(x);
        bounds.setY(y);
        bounds.setHeight(carTransform.getFitHeight());
        bounds.setWidth(carTransform.getFitWidth());
        bounds.setRotate(rotation);
    }

    @Override
    public void setUp(boolean set) {
        forward = set;
    }

    @Override
    public void setDown(boolean set) {
        brake = set;
    }

    @Override
    public void setLeft(boolean set) {
        left = set;
    }

    @Override
    public void setRight(boolean set) {
        right = set;
    }

    @Override
    public void setJump(boolean set) {
        handbrake = set;
    }

    @Override
    public void setSprint(boolean set) {
        return;
    }

    @Override
    public void setSquat(boolean set) {
        return;
    }

    @Override
    public void drawMe() {
        carTransform.setY(y);
        carTransform.setX(x);
        scene.setTranslateY(540.0-y);
        scene.setTranslateX(960.0-x);
        carTransform.setRotate(rotation);
    }

    @Override
    public void setImage() {

    }

    @Override
    public void update(double deltaTime, Vector<Node> collide) {
        double oldX = x, oldY = y;
        colision = false;
        if(forward) {
            if (speed<0.0&&speed+acceleration>0.0) {
                speed = 0.0;
            } else if(speed != 0.0 || prevForward!=true) {
                speed += acceleration;
            }
            if(speed>maxSpeed)
                speed = maxSpeed;
        }
        if(brake) {
            if(speed>0.0&&speed-brakeForce<0.0) {
                speed = 0.0;
            } else if(speed != 0.0 || prevBrake!=true) {
                speed -= brakeForce;
            }
            if(speed == -20.0) {
                speed = -20.0;
            }
        }
        if(left) {
            turn -= 0.25;
            if(Math.abs(turn)>maxTurn) {
                turn = -maxTurn;
            }

        }
        if(right) {
            turn += 0.25;
            if(Math.abs(turn)>maxTurn) {
                turn = maxTurn;
            }
        }
        rotation += turn*speed;
        x += Math.sin(Math.toRadians(rotation))*speed*deltaTime;
        y -= Math.cos(Math.toRadians(rotation))*speed*deltaTime;
        bounds.setX(x);
        bounds.setY(y);
        bounds.setHeight(carTransform.getFitHeight());
        bounds.setWidth(carTransform.getFitWidth());
        bounds.setRotate(rotation);
        for(Node i: collide) {
            if(i.getBoundsInParent().intersects(bounds.getBoundsInParent())) {
                colision = true;
            }
        }
        if(colision) {
            x= oldX;
            y= oldY;
            speed = 0.0;
        }
        prevForward = forward;
        prevBrake = brake;
    }

    @Override
    public double getX() {
        return 0;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public double getY() {
        return 0;
    }

    @Override
    public boolean isReachable(double x, double y) {
        if((carTransform.getX()-x)*(carTransform.getX()-x)+(carTransform.getY()-y)*(carTransform.getY()-y)<40000) {
            return true;
        }
        return false;
    }

    @Override
    public void use() {
        isUsed = !isUsed;
    }
}
