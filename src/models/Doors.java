package models;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point3D;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class Doors implements Usable {
    private Logger logger = Logger.getLogger(Doors.class);
    public Rectangle collisionBox;
    private boolean closed;
//    private double x,y;
//    private int rotation;
    private ImageView doorPicture;

    public Doors(ImageView aDoorPicture) {
        DOMConfigurator.configure("log4j2.xml");
        doorPicture = aDoorPicture;
        closed = true;
        logger.info(doorPicture.toString()+doorPicture.getX()+doorPicture.getY());
        collisionBox = new Rectangle(doorPicture.getX()-30,doorPicture.getY()-40,doorPicture.getFitWidth(),doorPicture.getFitHeight());
//        Point3D rotationAxis = doorPicture.getRotationAxis();
//        logger.info(rotationAxis.toString());
//        Point3D newRotationAxis = rotationAxis.add(-(doorPicture.getFitWidth())/2,0.0,0.0);
//        logger.info(newRotationAxis.toString());
//        doorPicture.setRotationAxis(newRotationAxis);
    }

    @Override
    public boolean isReachable(double x, double y) {
        return ((x-doorPicture.getX())*(x-doorPicture.getX())+(y-doorPicture.getY())*(y-doorPicture.getY())<=10000.0);
    }

    @Override
    public void use() {
        if(closed) {
            logger.info("Otwieram drzwi");
            closed = false;
            collisionBox.setWidth(0.0);
            collisionBox.setHeight(0.0);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> {
                Rotate rotation = new Rotate(doorPicture.getRotate()+0.75,doorPicture.getX(),doorPicture.getY()
                        +(doorPicture.getFitHeight()/2));
                doorPicture.getTransforms().add(rotation);
            }));
            timeline.setCycleCount(120);
            timeline.play();
        } else {
            logger.info("Zamykam drzwi");
            closed = true;
            collisionBox.setWidth(doorPicture.getFitWidth());
            collisionBox.setHeight(doorPicture.getFitHeight());
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> {
                Rotate rotation = new Rotate(doorPicture.getRotate()-0.75,doorPicture.getX(),doorPicture.getY()
                        +(doorPicture.getFitHeight()/2));
                doorPicture.getTransforms().add(rotation);
            }));
            timeline.setCycleCount(120);
            timeline.play();
        }
    }
}
