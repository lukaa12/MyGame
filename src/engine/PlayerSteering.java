package engine;

import models.Player;

public class PlayerSteering implements Updater {
    private Player model;
    private final static int WALKSPEED = 125;
    private final static int RUNSPEED = 556;

    public PlayerSteering(Player aModel) {
        model = aModel;
    }
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
            model.addX(Math.sin(Math.toRadians(direction))*RUNSPEED*deltaTime);
            model.addY(-Math.cos(Math.toRadians(direction))*RUNSPEED*deltaTime);
        }
        else if(inMotion) {
            model.addX(Math.sin(Math.toRadians(direction))*WALKSPEED*deltaTime);
            model.addY(-Math.cos(Math.toRadians(direction))*WALKSPEED*deltaTime);
        }
//        model.drawMe();
    }
}
