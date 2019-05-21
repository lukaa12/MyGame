package models;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
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
        collisionBox = new Rectangle(doorPicture.getX(),doorPicture.getY(),doorPicture.getFitWidth(),doorPicture.getFitHeight());
    }

    @Override
    public boolean isReachable(double x, double y) {
        return ((x-doorPicture.getX())*(x-doorPicture.getX())+(y-doorPicture.getY())*(y-doorPicture.getY())<=10000.0);
    }

    @Override
    public void use() {
        if(closed) {
            logger.info("Otwieram drzwi");
        } else {
            logger.info("Zamykam drzwi");
        }
    }
}
