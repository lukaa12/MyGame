package test;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Doors;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

class DoorsTest {

    @Test
    void newDoorsTest() {
        Doors tester = new Doors(1);
        assertTrue(tester.isClosed());
    }
    @Test
    void typeOfDoors() {
        Doors tester = new Doors(1);
        assertTrue(tester.isClosed());
        assertTrue(tester.isTypeRight());
        tester.setType(false);
        assertFalse(tester.isTypeRight());
    }
}