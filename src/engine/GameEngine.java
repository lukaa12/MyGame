package engine;


import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import models.Steerable;

import java.util.Vector;

public class GameEngine implements Runnable {
    private volatile boolean isPaused = false;
    private volatile boolean exit = false;
    private Vector<Steerable> objectsToUpdate;
    private Vector<Node> collisions;

    public GameEngine() {
        objectsToUpdate = new Vector<>();
    }
    public void endGame() {
        exit = true;
    }
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        while (true) {
//            Timeline
            long currentTime = System.nanoTime();
            double deltaSecs = Math.abs(currentTime-lastTime)/1000000000.0;
            lastTime = currentTime;

            while (isPaused) Thread.onSpinWait();

            if(exit) {
                break;
            }
            for(Steerable obj: objectsToUpdate) {
                obj.update(deltaSecs,collisions);
            }
        }
    }
    public void addObject(Steerable object) {
        objectsToUpdate.add(object);
    }
    public void addCollisions(Node obj) {collisions.add(obj);}
}
