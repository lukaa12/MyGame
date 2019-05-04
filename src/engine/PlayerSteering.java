package engine;

import models.Player;

public class PlayerSteering implements Updater {
    private Player model;
    private final static int WALKSPEED = 125;
    private final static int RUNSPEED = 556;

    @Override
    public void update(double deltaTime) {
        int direction = model.getRotation();
        boolean inMotion = false;
        if(model.isRight()&&!model.isLeft()) {
            if(model.isUp()&&!model.isDown()) {
                inMotion = true;
                direction = 45;
            } else if(model.isDown()&&!model.isUp()) {
                direction = 135;
                inMotion = true;
            } else {
                direction = 90;
                inMotion = true;
            }
        }
        else if(!model.isRight()&&model.isLeft()) {
            if(model.isUp()&&!model.isDown()) {
                direction = 315;
                inMotion = true;
            } else if(model.isDown()&&!model.isUp()) {
                direction = 225;
                inMotion = true;
            } else {
                direction = 270;
                inMotion = true;
            }
        }
        else {
            if(model.isUp()&&!model.isDown()) {
                direction = 0;
                inMotion = true;
            } else if(model.isDown()&&!model.isUp()) {
                direction = 180;
                inMotion = true;
            }
        }
        model.setRotation(direction);
        if(model.isRunning()&&inMotion) {
            model.setX((int) (model.getX()+Math.cos(Math.toRadians(direction))*RUNSPEED*deltaTime));
            model.setY((int) (model.getY()-Math.sin(Math.toRadians(direction))*RUNSPEED*deltaTime));
        }
    }
}
