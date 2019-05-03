package engine;

import models.Player;

public class PlayerSteering implements Updater {
    private Player model;

    @Override
    public void update(double deltaTime) {
        int direction;
        if(model.isRight()&&!model.isLeft()) {
            if(model.isUp()&&!model.isDown())
                direction = 45;
            else if(model.isDown()&&!model.isUp())
                direction = 135;
            else
                direction = 90;
        }

    }
}
