package engine;


import controllers.GameController;
import javafx.scene.Node;
import models.Player;
import models.Steerable;
import models.Usable;
import models.Vechicle;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.util.Vector;

public class GameEngine {
    private Logger logger = Logger.getLogger(GameEngine.class);
    private Vector<Steerable> objectsToUpdate;
    private Vector<Node> collisions;
    private Vector<Usable> usables;
    public Usable toUse;
    public boolean nowUse = false;
    public GameController gameController;

    public GameEngine() {
        DOMConfigurator.configure("log4j2.xml");
        objectsToUpdate = new Vector<>();
        collisions = new Vector<>();
        usables = new Vector<>();
    }

    public void run() {
        double deltaSecs = 0.016;

        for(Steerable obj: objectsToUpdate) {
            obj.update(deltaSecs,collisions);
        }
        toUse = null;
        for(Usable use: usables) {
            if(use.isReachable(gameController.getPlayer().getX(),gameController.getPlayer().getY())) {
                toUse = use;
//                logger.info("Can use: "+ toUse.toString());
                break;
            }
        }
    }

    public void interaction() {

        if(!nowUse&&toUse!=null) {
            logger.info("Using object: "+toUse.toString());
            nowUse = true;
            toUse.use();
            if(gameController.getPlayer() instanceof Vechicle) {
                gameController.getOutCar();
                nowUse = false;
                return;
            }
            if(toUse instanceof Vechicle) {
                Vechicle auto = (Vechicle) toUse;
                gameController.getInCar(auto);
            }
            nowUse = false;
        }
    }

    public void addObject(Steerable object) {
        objectsToUpdate.add(object);
    }
    public void removeObject(Steerable object) {objectsToUpdate.removeElement(object);}
    public void addCollisions(Node obj) {collisions.add(obj);}
    public void removeCollisions(Node obj) {collisions.removeElement(obj);}
    public void addUsable(Usable obj) {usables.add(obj);}
}
