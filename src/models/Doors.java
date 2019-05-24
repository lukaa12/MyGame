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
    private MediaPlayer doorOpen = new MediaPlayer(new Media(this.getClass().getResource("/resources/openDoor.mp3").toString()));
    private MediaPlayer doorClose = new MediaPlayer(new Media(this.getClass().getResource("/resources/closeDoor.mp3").toString()));
    private ImageView doorPicture;

    public Doors(ImageView aDoorPicture) {
        DOMConfigurator.configure("log4j2.xml");
        doorPicture = aDoorPicture;
        closed = true;
        logger.info(doorPicture.toString()+doorPicture.getX()+doorPicture.getY());
        collisionBox = new Rectangle(doorPicture.getX()-30,doorPicture.getY()-40,doorPicture.getFitWidth(),doorPicture.getFitHeight());
    }

    public Doors(ImageView aDoorPicture, boolean isRightDoors) {
        DOMConfigurator.configure("log4j2.xml");
        doorPicture = aDoorPicture;
        closed = true;
        logger.info(doorPicture.toString()+doorPicture.getX()+doorPicture.getY());
        collisionBox = new Rectangle(doorPicture.getX()-30,doorPicture.getY()-40,doorPicture.getFitWidth(),doorPicture.getFitHeight());
        typeRight = isRightDoors;
    }

    @Override
    public boolean isReachable(double x, double y) {
        return ((x-doorPicture.getX())*(x-doorPicture.getX())+(y-doorPicture.getY())*(y-doorPicture.getY())<=40000.0);
    }

    @Override
    public void use() {
        double deltaGrad;
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
}
