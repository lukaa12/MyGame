package test;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Doors;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

class DoorsTest {

    @Test
    void notReachableTest() {

        ImageView door = new ImageView(new Image(this.getClass().getResource("/resources/doors.png").toString()));
        door.setX(200);
        door.setY(300);
        Doors tester = new Doors(door);
        boolean reachable = tester.isReachable(1000.0,500.0);
        assertFalse(reachable);
    }
}