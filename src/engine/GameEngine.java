package engine;


import javafx.scene.Node;
import models.Player;
import models.Steerable;
import models.Usable;

import java.util.Vector;

public class GameEngine {
    private Vector<Steerable> objectsToUpdate;
    private Vector<Node> collisions;
    private Vector<Usable> usables;
    public Usable toUse;

    public GameEngine() {
        objectsToUpdate = new Vector<>();
        collisions = new Vector<>();
        usables = new Vector<>();
    }

    public void run() {
        Player player = null;
        double deltaSecs = 0.016;
        for(Steerable obj: objectsToUpdate) {
            obj.update(deltaSecs,collisions);
            if(obj instanceof Player) {
                player = (Player) obj;
            }
        }
        toUse = null;
        for(Usable use: usables) {
            if(player!=null&&use.isReachable(player.getX(),player.getY())) {
                toUse = use;
                break;
            }
        }
    }
    public void addObject(Steerable object) {
        objectsToUpdate.add(object);
    }
    public void addCollisions(Node obj) {collisions.add(obj);}

    public void addUsable(Usable obj) {usables.add(obj);}
}
