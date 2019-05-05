package engine;

import javafx.collections.ObservableList;

import java.util.Vector;

public class GameEngine implements Runnable {
    private volatile boolean isPaused = false;
    private volatile boolean exit = false;
    private Vector<Updater> objectsToUpdate;

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
            long currentTime = System.nanoTime();
            double deltaSecs = Math.abs(currentTime-lastTime)/1000000000.0;
//            System.out.println(deltaSecs);
            lastTime = currentTime;

            while (isPaused) Thread.onSpinWait();

            if(exit) {
                break;
            }
            for(Updater obj: objectsToUpdate) {
                obj.update(deltaSecs);
            }
        }
    }
    public void addObject(Updater object) {
        objectsToUpdate.add(object);
    }
}
