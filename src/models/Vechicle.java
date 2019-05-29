package models;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import view.Main;

import java.util.Vector;

public class Vechicle implements Steerable, Usable {
    private Logger logger = Logger.getLogger(Vechicle.class);
    double acceleration = 60.0;
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

    public Vechicle(ImageView aCarTransform) {
        DOMConfigurator.configure("log4j2.xml");
        carTransform = aCarTransform;
        x = carTransform.getX();
        y = carTransform.getY();
        rotation = aCarTransform.getRotate();
        bounds = new Rectangle();
        bounds.setX(x);
        bounds.setY(y);
        bounds.setHeight(0);
        bounds.setWidth(0);
        bounds.setRotate(rotation);
        maxTurn = 1.2;
        brakeForce = 100.0;
        maxSpeed = 400.0;
    }

    public Vechicle() {
        DOMConfigurator.configure("log4j2.xml");
        maxTurn = 1.2;
        brakeForce = 100.0;
        maxSpeed = 400.0;
        bounds = null;
    }

    @Override
    public void setUp(boolean set) {
        forward = set;
        logger.info("Accererate car: "+forward);
    }

    public boolean isUp() {
        return forward;
    }

    public boolean isDown() { return brake;}

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
        scene.setTranslateY(290.0-y);
        scene.setTranslateX(860.0-x);
        carTransform.setRotate(rotation);
    }

    @Override
    public void setImage() {

    }

    @Override
    public void loadCoords(double aX, double aY, int aRotation) {
        x = aX;
        y = aY;
        rotation = aRotation;
        if(carTransform==null)
            return;
        carTransform.setX(x);
        carTransform.setY(y);
        carTransform.setRotate(rotation);
    }

    @Override
    public Vector<String> stateToSave() {
        Vector<String> state = new Vector<>();
        state.add(String.valueOf(rotation));
        state.add(String.valueOf(x));
        state.add(String.valueOf(y));
        return state;
    }

    @Override
    public void update(double deltaTime, Vector<Node> collide) {
        double oldX = x, oldY = y;
        colision = false;
        if(forward) {
            if (speed<0.0&&speed+acceleration*deltaTime>0.0) {
                speed = 0.0;
            } else if(speed<0.0 || !prevForward) {
                speed += brakeForce*deltaTime;
            }
            if(speed>0.0)
                speed+=acceleration*deltaTime;
            if(speed>maxSpeed)
                speed = maxSpeed;
        }
        if(brake) {
            if(speed>0.0&&speed-brakeForce*deltaTime<0.0) {
                speed = 0.0;
            } else if(speed != 0.0 || !prevBrake) {
                speed -= brakeForce*deltaTime;
            }
            if(speed <= -200.0) {
                speed = -200.0;
            }
        }
        if(!forward&&!brake) {
            speed = speed - (speed/2)*deltaTime;
        }
        if(left) {
            turn -= 1.0*deltaTime;
            if(Math.abs(turn)>maxTurn) {
                turn = -maxTurn;
            }

        }
        if(right) {
            turn += 1.0*deltaTime;
            if(Math.abs(turn)>maxTurn) {
                turn = maxTurn;
            }
        }
        if(!right&&!left) {
            turn = turn - turn*deltaTime*10;
        }
        if(speed!=0.0) {
            rotation += turn*speed/maxSpeed;
        }
        x += Math.sin(Math.toRadians(rotation))*speed*deltaTime;
        y -= Math.cos(Math.toRadians(rotation))*speed*deltaTime;
//        bounds.setX(x);
//        bounds.setY(y);
//        bounds.setHeight(carTransform.getFitHeight()/2);
//        bounds.setWidth(carTransform.getFitWidth()/2);
//        bounds.setRotate(rotation);
        for(Node i: collide) {
            if(i.getBoundsInParent().intersects(bounds.getBoundsInParent())) {
                colision = true;
            }
        }
//        if(colision) {
//            logger.info("Car collision!");
//            x= oldX;
//            y= oldY;
//            speed = 0.0;
//        }
        prevForward = forward;
        prevBrake = brake;
    }

    @Override
    public double getX() {
        return x;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public double getY() {
        return y;
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
