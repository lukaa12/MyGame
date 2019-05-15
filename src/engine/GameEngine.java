package engine;


import javafx.scene.Node;
import models.Steerable;

import java.util.Vector;

public class GameEngine {
    private Vector<Steerable> objectsToUpdate;
    private Vector<Node> collisions;

    public GameEngine() {
        objectsToUpdate = new Vector<>();
        collisions = new Vector<>();
    }

    public void run() {
            double deltaSecs = 0.016;
            for(Steerable obj: objectsToUpdate) {
                obj.update(deltaSecs,collisions);
            }
    }
    public void addObject(Steerable object) {
        objectsToUpdate.add(object);
    }
    public void addCollisions(Node obj) {collisions.add(obj);}
}
