package engine;

import java.util.Vector;

public class GameEngine implements Runnable {
    private boolean isPaused = false;
    private boolean exit = false;
    private Vector<Updater> objectsToUpdate;
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        while (true) {
            long currentTime = System.nanoTime();
            double deltaSecs = (currentTime-lastTime)/1000000000.0;
            lastTime = currentTime;

            while (isPaused);

            if(exit) {
                break;
            }
            for(Updater obj: objectsToUpdate) {
                obj.update(deltaSecs);
            }
        }
    }
}
