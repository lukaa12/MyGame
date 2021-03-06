package models;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class Doors implements Usable {
    private Logger logger = Logger.getLogger(Doors.class);
    private boolean typeRight =true;
    public Rectangle collisionBox;
    private boolean closed;
    private MediaPlayer doorOpen;
    private MediaPlayer doorClose;
    private ImageView doorPicture;

    public Doors(ImageView aDoorPicture) {
        DOMConfigurator.configure("log4j2.xml");
        doorOpen = new MediaPlayer(new Media(this.getClass().getResource("/resources/openDoor.mp3").toString()));
        doorClose = new MediaPlayer(new Media(this.getClass().getResource("/resources/closeDoor.mp3").toString()));
        doorPicture = aDoorPicture;
        closed = true;
        logger.info(doorPicture.toString()+doorPicture.getX()+doorPicture.getY());
        collisionBox = new Rectangle(doorPicture.getX()-30,doorPicture.getY()-40,doorPicture.getFitWidth(),doorPicture.getFitHeight());
    }

    public  Doors(int test) {
        DOMConfigurator.configure("log4j2.xml");
        closed = true;
    }

    public Doors(ImageView aDoorPicture, boolean isRightDoors, int orientation) {
        DOMConfigurator.configure("log4j2.xml");
        doorPicture = aDoorPicture;
        closed = true;
        doorOpen = new MediaPlayer(new Media(this.getClass().getResource("/resources/openDoor.mp3").toString()));
        doorClose = new MediaPlayer(new Media(this.getClass().getResource("/resources/closeDoor.mp3").toString()));
        logger.info(doorPicture.toString()+doorPicture.getX()+doorPicture.getY());
        collisionBox = new Rectangle(doorPicture.getX()-30,doorPicture.getY(),doorPicture.getFitWidth()+10,doorPicture.getFitHeight()+20);
        typeRight = isRightDoors;
        if(orientation!=0) {
            Rotate rotation = new Rotate(doorPicture.getRotate()+orientation*90,doorPicture.getX(),doorPicture.getY()
                    +(doorPicture.getFitHeight()/2));
            doorPicture.getTransforms().add(rotation);
            collisionBox.getTransforms().add(rotation);
        }
    }

    public void adjustCollision(double x, double y) {
        collisionBox.setX(collisionBox.getX()+x);
        collisionBox.setY(collisionBox.getY()+y);
    }

    @Override
    public boolean isReachable(double x, double y) {
        return ((x-doorPicture.getX()-doorPicture.getFitWidth()/2)*(x-doorPicture.getX()-doorPicture.getFitWidth()/2)+(y-doorPicture.getY()-doorPicture.getFitHeight()/2)*(y-doorPicture.getY()-doorPicture.getFitHeight()/2)<=40000.0);
    }

    @Override
    public void use() {
        double deltaGrad;
        double width = doorPicture.getFitWidth(), height = doorPicture.getFitHeight();
        if(width < height) {
            width = height;
            height = doorPicture. getFitWidth();
        }
        if(typeRight)
            deltaGrad = 0.75;
        else
            deltaGrad = -0.75;
        if(closed) {
            logger.info("Otwieram drzwi");
            closed = false;
            collisionBox.setWidth(0.0);
            collisionBox.setHeight(0.0);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> {
                Rotate rotation = new Rotate(doorPicture.getRotate()+deltaGrad,doorPicture.getX(),doorPicture.getY()
                        +(doorPicture.getFitHeight()/2));
                doorPicture.getTransforms().add(rotation);
            }));
            timeline.setCycleCount(120);
            doorOpen.seek(Duration.ZERO);
            doorOpen.play();
            timeline.play();
        } else {
            logger.info("Zamykam drzwi");
            closed = true;
            collisionBox.setWidth(doorPicture.getFitWidth());
            collisionBox.setHeight(doorPicture.getFitHeight());
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> {
                Rotate rotation = new Rotate(doorPicture.getRotate()-deltaGrad,doorPicture.getX(),doorPicture.getY()
                        +(doorPicture.getFitHeight()/2));
                doorPicture.getTransforms().add(rotation);
            }));
            timeline.setCycleCount(120);
            timeline.play();
            doorClose.seek(Duration.ZERO);
            doorClose.play();
        }
    }

    public boolean isClosed() {return  closed;}
    public boolean isTypeRight() {return typeRight; }
    public void setType(boolean set) {
        typeRight = set;
    }
}
